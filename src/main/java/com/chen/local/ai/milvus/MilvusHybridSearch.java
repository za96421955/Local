package com.chen.local.ai.milvus;

import com.chen.local.ai.Embedding;
import com.chen.local.ai.MilvusDemo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.milvus.v2.client.MilvusClientV2;
import io.milvus.v2.common.ConsistencyLevel;
import io.milvus.v2.common.DataType;
import io.milvus.v2.common.IndexParam;
import io.milvus.v2.service.collection.request.AddFieldReq;
import io.milvus.v2.service.collection.request.CreateCollectionReq;
import io.milvus.v2.service.vector.request.AnnSearchReq;
import io.milvus.v2.service.vector.request.HybridSearchReq;
import io.milvus.v2.service.vector.request.InsertReq;
import io.milvus.v2.service.vector.request.data.BaseVector;
import io.milvus.v2.service.vector.request.data.FloatVec;
import io.milvus.v2.service.vector.request.data.SparseFloatVec;
import io.milvus.v2.service.vector.request.ranker.BaseRanker;
import io.milvus.v2.service.vector.request.ranker.RRFRanker;
import io.milvus.v2.service.vector.request.ranker.WeightedRanker;
import io.milvus.v2.service.vector.response.InsertResp;
import io.milvus.v2.service.vector.response.SearchResp;
import lombok.Data;

import java.util.*;

/**
 * Milvus 混合搜索
 * <p>
 * </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2025/2/28
 */
public class MilvusHybridSearch {

    private final MilvusClientV2 client;

    private MilvusHybridSearch(MilvusClientV2 client) {
        this.client = client;
    }
    public static MilvusHybridSearch init(MilvusClientV2 client) {
        return new MilvusHybridSearch(client);
    }

    /**
     * @description 生成Schema
     * <p> <功能详细描述> </p>
     *
     * @author 陈晨
     * @date 2025/3/3 10:22
     */
    public CreateCollectionReq.CollectionSchema generateSchema() {
        CreateCollectionReq.CollectionSchema schema = client.createSchema();
        // id：该字段作为存储文本 ID 的主键。此字段的数据类型为 INT64
        schema.addField(AddFieldReq.builder()
                .fieldName("id")
                .dataType(DataType.Int64)
                .isPrimaryKey(true)
                .autoID(false)
                .build());
        // text：此字段用于存储文本内容。该字段的数据类型为 VARCHAR，最大长度为 1000 个字符
        schema.addField(AddFieldReq.builder()
                .fieldName("text")
                .dataType(DataType.VarChar)
                .maxLength(1000)
                .build());
        // dense：该字段用于存储文本的密集向量。此字段的数据类型为 FLOAT_VECTOR，向量维度为 768
        schema.addField(AddFieldReq.builder()
                .fieldName("dense")
                .dataType(DataType.FloatVector)
                .dimension(768)
                .build());
        // sparse：该字段用于存储文本的稀疏向量。此字段的数据类型为 SPARSE_FLOAT_VECTOR
        schema.addField(AddFieldReq.builder()
                .fieldName("sparse")
                .dataType(DataType.SparseFloatVector)
                .build());
        return schema;
    }

    /**
     * @description 生成索引
     * <p> <功能详细描述> </p>
     *
     * @author 陈晨
     * @date 2025/3/3 10:25
     */
    public List<IndexParam> generateIndex() {
        Map<String, Object> denseParams = new HashMap<>();
        denseParams.put("nlist", 128);
        // 为密集向量字段 dense IVF_FLAT 索引
        IndexParam indexParamForDenseField = IndexParam.builder()
                .fieldName("dense")
                .indexName("dense_index")
                .indexType(IndexParam.IndexType.IVF_FLAT)
                .metricType(IndexParam.MetricType.IP)
                .extraParams(denseParams)
                .build();
        // 为稀疏向量字段 sparse 创建一个SPARSE_INVERTED_INDEX
        Map<String, Object> sparseParams = new HashMap<>();
        sparseParams.put("inverted_index_algo", "DAAT_MAXSCORE");
        IndexParam indexParamForSparseField = IndexParam.builder()
                .fieldName("sparse")
                .indexName("sparse_index")
                .indexType(IndexParam.IndexType.SPARSE_INVERTED_INDEX)
                .metricType(IndexParam.MetricType.IP)
                .extraParams(sparseParams)
                .build();
        List<IndexParam> indexParams = new ArrayList<>();
        indexParams.add(indexParamForDenseField);
        indexParams.add(indexParamForSparseField);
        return indexParams;
    }

    @Data
    private static class Text {
        private final Embedding embedding;
        private String text;
        private List<Float> denseVectors;
        private Map<Integer, Float> sparseVectors;

        public Text(String text) {
            this.embedding = new Embedding();
            this.text = text;
            this.denseVectors = embedding.getTextDenseVectors(this.text);
            this.sparseVectors = embedding.toSparseVector(this.denseVectors);
            if (this.denseVectors.size() != 768) {
                throw new IllegalArgumentException("Dense vector dimension must be 768!");
            }
        }

    }

    /**
     * @description 插入数据
     * <p> <功能详细描述> </p>
     *
     * @author 陈晨
     * @date 2025/3/3 10:47
     */
    public InsertResp insertData(String collectionName) {
        Gson gson = new Gson();
        JsonObject row1 = new JsonObject();
        Text text1 = new Text("Artificial intelligence was founded as an academic discipline in 1956.");
        row1.addProperty("id", 1);
        row1.addProperty("text", text1.getText());
        row1.add("dense", gson.toJsonTree(text1.getDenseVectors()));
        row1.add("sparse", gson.toJsonTree(text1.getSparseVectors()));

        JsonObject row2 = new JsonObject();
        Text text2 = new Text("Alan Turing was the first person to conduct substantial research in AI.");
        row2.addProperty("id", 2);
        row2.addProperty("text", text2.getText());
        row2.add("dense", gson.toJsonTree(text2.getDenseVectors()));
        row2.add("sparse", gson.toJsonTree(text2.getSparseVectors()));

        JsonObject row3 = new JsonObject();
        Text text3 = new Text("Born in Maida Vale, London, Turing was raised in southern England.");
        row3.addProperty("id", 3);
        row3.addProperty("text", text3.getText());
        row3.add("dense", gson.toJsonTree(text3.getDenseVectors()));
        row3.add("sparse", gson.toJsonTree(text3.getSparseVectors()));

        List<JsonObject> data = Arrays.asList(row1, row2, row3);
        InsertReq insertReq = InsertReq.builder()
                .collectionName(collectionName)
                .data(data)
                .build();
        return client.insert(insertReq);
    }

    /**
     * @description 生成查询对象
     * <p> <功能详细描述> </p>
     *
     * @author 陈晨
     * @date 2025/3/3 10:53
     */
    public List<AnnSearchReq> generateSearch() {
        float[] dense = new float[] {-0.04770946875214577f,0.006615452468395233f,0.027526365593075752f,0.0023355046287178993f,-0.08260082453489304f,-0.052098046988248825f,-0.07973303645849228f,0.03565174713730812f,0.048448141664266586f,0.049882031977176666f,-0.008543601259589195f,-0.05935440585017204f,-0.019726866856217384f,0.004866539966315031f,0.021421466022729874f,-0.012535684742033482f,0.0355648472905159f,0.002765943529084325f,5.3329614456743E-4f,-0.09915574640035629f,-0.04021412879228592f,-0.010797634720802307f,9.430278441868722E-4f,0.03006826341152191f,0.049621324986219406f,0.11253873258829117f,-0.07369331270456314f,-0.019379256293177605f,0.03313157707452774f,-0.10158901661634445f,-0.01912941224873066f,-0.060614489018917084f,0.03780258446931839f,-0.010569515638053417f,-0.04371195659041405f,0.006284136790782213f,-0.011449404060840607f,-0.059093695133924484f,-0.12765976786613464f,-0.03971444070339203f,0.005034913308918476f,-0.01628335565328598f,-0.020704519003629684f,-0.025744864717125893f,0.026201102882623672f,-0.011112656444311142f,-0.04151766747236252f,-0.028178134933114052f,-0.025657961145043373f,0.01474083587527275f,0.03117627091705799f,0.010243631899356842f,0.028743000701069832f,0.016468022018671036f,0.014469265937805176f,-0.03419613093137741f,0.01789105124771595f,-0.01780414953827858f,-0.029134061187505722f,-0.022268764674663544f,-0.02177993766963482f,-2.2014167916495353E-4f,-0.029590299353003502f,-0.049013007432222366f,0.008320913650095463f,0.03056795336306095f,-0.029286140576004982f,-0.01589229330420494f,-0.04779637232422829f,-0.024854114279150963f,-0.01438236329704523f,0.019878946244716644f,-0.010466319508850574f,0.013382984325289726f,-0.08577276021242142f,0.03695528581738472f,0.0021467632614076138f,0.020682793110609055f,-0.005648662336170673f,-0.02010706439614296f,0.08034135401248932f,-0.04310363903641701f,0.030328970402479172f,-0.03810674324631691f,-0.007935283705592155f,-0.001091033685952425f,-0.04840468987822533f,0.07521411031484604f,-0.017119791358709335f,0.03239290416240692f,-0.021682173013687134f,-0.04449407756328583f,0.027222206816077232f,-0.06999996304512024f,-0.011297324672341347f,-0.025397254154086113f,-0.012633450329303741f,0.06522032618522644f,0.049404069781303406f,-0.005339072085916996f,0.025918669998645782f,0.06821846216917038f,-0.03841090202331543f,-0.050968315452337265f,0.07912471890449524f,-0.029698926955461502f,0.027548091486096382f,0.055965207517147064f,-0.01532742753624916f,0.014067341573536396f,0.054835475981235504f,0.0342613086104393f,0.016315942630171776f,0.03993169590830803f,0.006371039431542158f,-0.047014251351356506f,0.007951578125357628f,0.00906501617282629f,1.3901005149818957E-4f,0.03997514769434929f,-0.026092473417520523f,0.027330834418535233f,0.01869489997625351f,-0.015262250788509846f,0.04136558622121811f,-0.03558657318353653f,0.09767840802669525f,-0.014469265937805176f,0.007571380119770765f,0.01632680557668209f,0.007728890515863895f,0.048795752227306366f,-0.03745497390627861f,-0.015837980434298515f,-0.048491593450307846f,-0.0630912110209465f,-0.01587056741118431f,-0.010710733011364937f,-0.015772802755236626f,-0.012687764130532742f,0.06283050775527954f,-0.023376772180199623f,-0.024658583104610443f,-0.01073788944631815f,0.023463673889636993f,-0.047839824110269547f,-0.020356910303235054f,0.023333320394158363f,0.058919891715049744f,0.06973925232887268f,0.008103657513856888f,-0.04258222132921219f,-0.04395093768835068f,-0.0396927148103714f,-0.028504017740488052f,0.05900679528713226f,-0.01823866181075573f,0.014154244214296341f,0.012296702712774277f,-0.025266900658607483f,0.03536931425333023f,0.012416194193065166f,-0.03106764145195484f,0.02104126662015915f,0.011438541114330292f,0.03945373371243477f,-0.028199858963489532f,0.04345124959945679f,-0.020921776071190834f,0.026114199310541153f,-0.026418358087539673f,-0.03591245785355568f,-0.0394754596054554f,0.005469425581395626f,0.011818739585578442f,0.06539412587881088f,0.0052413069643080235f,0.04112660512328148f,-0.03565174713730812f,-0.06556793302297592f,-0.06348226964473724f,0.014632207341492176f,-0.022051507607102394f,-0.011666660197079182f,-0.04136558622121811f,-0.027743620797991753f,-0.019227176904678345f,0.057138390839099884f,0.00832634512335062f,0.019878946244716644f,-5.241306498646736E-4f,-0.013524200767278671f,0.0017027457943186164f,-0.051620081067085266f,-0.0037123658694326878f,-0.024962741881608963f,0.006653472315520048f,0.03037242218852043f,-0.023181241005659103f,-0.051489729434251785f,-0.04351642355322838f,0.05870263651013374f,-0.03788948804140091f,0.012807255610823631f,-0.009385469369590282f,-0.0056649562902748585f,-0.012828980572521687f,-0.03243635594844818f,0.028981981799006462f,0.027243932709097862f,-0.03723771870136261f,0.014078204520046711f,0.06747978925704956f,-0.01483860146254301f,-0.012807255610823631f,-0.02995963580906391f,0.049143362790346146f,0.005681250710040331f,-0.03074175864458084f,-0.027678444981575012f,-0.024636857211589813f,0.09116071462631226f,0.0018113738624379039f,0.06017997860908508f,0.0700434148311615f,-0.019596513360738754f,0.07690870761871338f,0.008739132434129715f,-0.025657961145043373f,-0.0022418128792196512f,0.005735564511269331f,-0.004404870327562094f,-0.001902349991723895f,-0.026374908164143562f,0.029177512973546982f,-0.013306944631040096f,-0.009097605012357235f,0.01980290561914444f,-0.004768774379044771f,-0.006066880654543638f,-0.03291432186961174f,0.026049023494124413f,-0.0011453477200120687f,-0.015772802755236626f,-0.009363743476569653f,-0.049099911004304886f,-0.013350395485758781f,-0.03180631250143051f,-0.008815172128379345f,0.0665673092007637f,0.03943200781941414f,-0.01438236329704523f,-6.134093855507672E-4f,-0.01700030080974102f,0.03815019503235817f,0.03434821218252182f,0.0013354469556361437f,0.026679066941142082f,-0.001556097762659192f,-0.013687143102288246f,-0.014078204520046711f,-0.048230886459350586f,-0.007011944893747568f,0.03943200781941414f,-0.006045154761523008f,-0.03476099669933319f,-0.051924239844083786f,0.01956392452120781f,0.028808176517486572f,-0.011883916333317757f,0.00137075106613338f,-0.016250766813755035f,-0.03091556206345558f,0.023159515112638474f,0.054661668837070465f,-0.0018751928582787514f,-0.025332078337669373f,-0.03008998930454254f,0.006164645776152611f,0.04797017574310303f,-0.03767223283648491f,-0.001386366318911314f,0.006077743135392666f,0.011710111051797867f,-0.007082553580403328f,-0.050837960094213486f,0.023094337433576584f,-0.0301334410905838f,-0.026353182271122932f,-0.014903778210282326f,-0.045841068029403687f,-0.055834852159023285f,0.07412783056497574f,-0.051055215299129486f,-0.03167596086859703f,-0.03864988312125206f,-0.006479667499661446f,-0.1970948576927185f,-0.013926125131547451f,-0.028112957254052162f,0.0031420683953911066f,-0.0014013027539476752f,0.001735334168188274f,-0.006322156637907028f,-0.022029781714081764f,-0.04319053888320923f,0.08234011381864548f,0.019096823409199715f,-0.057181842625141144f,-0.04536310210824013f,-0.0294816717505455f,-0.006436216179281473f,0.01839074119925499f,-0.00479864701628685f,-0.03156733140349388f,-0.03463064506649971f,-0.02195374295115471f,0.01880352757871151f,-0.02919923886656761f,0.022073233500123024f,-0.00994490459561348f,0.04597141966223717f,0.006740374956279993f,-0.012991922907531261f,-0.04173492267727852f,-0.0362166166305542f,-0.025940394029021263f,0.04379885643720627f,0.009173644706606865f,-0.02206237055361271f,0.04627557843923569f,0.007071690633893013f,0.04093107581138611f,0.03113281913101673f,0.01988980919122696f,0.06495961546897888f,-0.054227158427238464f,-0.04618867486715317f,-0.007603968493640423f,0.03456546738743782f,0.008864054456353188f,0.03337055817246437f,0.02188856527209282f,0.0031828039791435003f,0.020747970789670944f,0.020117927342653275f,0.028221584856510162f,-0.020117927342653275f,-0.030328970402479172f,0.06517687439918518f,-0.05905024707317352f,0.01771724596619606f,-0.04775292053818703f,-0.024680308997631073f,-0.0018779085949063301f,2.2370603983290493E-4f,-0.010297945700585842f,0.016815632581710815f,-0.029394768178462982f,-0.04388576000928879f,-0.019965847954154015f,0.03669457882642746f,0.0025948542170226574f,-0.006555707193911076f,-0.021095581352710724f,0.01563158631324768f,-0.022703276947140694f,0.013046236708760262f,0.021399740129709244f,-0.011938230134546757f,3.099296009168029E-4f,-0.004741617478430271f,0.01882525347173214f,-0.03215392306447029f,-0.005355366505682468f,-0.04175664857029915f,-0.05965856462717056f,0.017815012484788895f,-0.009320292621850967f,-0.01529483962804079f,0.005610642489045858f,-0.04308191314339638f,0.005225012544542551f,-0.012535684742033482f,-0.021595269441604614f,0.0717814639210701f,0.22351321578025818f,-0.01749999076128006f,3.167612885590643E-5f,0.006984787993133068f,0.008788014762103558f,-0.012948472052812576f,-0.049925483763217926f,-0.006365607958287001f,0.03378334641456604f,-0.008983545005321503f,0.028677823022007942f,-0.00758767407387495f,5.417827633209527E-4f,0.0027890270575881004f,-0.025353802368044853f,0.03178458660840988f,-0.03267533704638481f,-4.742296296171844E-4f,0.06474235653877258f,0.06283050775527954f,0.04206080734729767f,0.0037639643996953964f,0.09029169380664825f,0.03741152584552765f,-0.020834872499108315f,-0.02091091312468052f,0.03973616659641266f,0.053749192506074905f,0.0011799728963524103f,0.012828980572521687f,0.014404088258743286f,0.022203586995601654f,-0.03254498541355133f,0.049925483763217926f,0.053445033729076385f,0.026874596253037453f,-0.01684822142124176f,-0.03623833879828453f,4.8746870015747845E-4f,0.007647419814020395f,-0.0029383907094597816f,-0.007185750175267458f,0.0175651665776968f,-0.0017923639388754964f,0.03261016309261322f,0.03943200781941414f,0.055704500526189804f,0.01628335565328598f,-0.03291432186961174f,-0.03308812528848648f,-0.028699548915028572f,-0.012894157320261002f,-0.026135925203561783f,0.024854114279150963f,-0.03413095325231552f,0.013480749912559986f,-0.021399740129709244f,0.008559895679354668f,-0.011764425784349442f,-0.013665417209267616f,0.023029161617159843f,0.025462431833148003f,0.03908439725637436f,0.0388236902654171f,0.03749842569231987f,-0.008511013351380825f,-0.027352560311555862f,-0.04303846135735512f,0.04036621004343033f,-0.03239290416240692f,0.07577897608280182f,-0.0024916573893278837f,-0.012926746159791946f,-0.02980755642056465f,-0.018531957641243935f,0.026744242757558823f,0.007804930210113525f,0.09724389016628265f,0.03161078318953514f,0.006094037555158138f,0.0431688129901886f,-0.04892610386013985f,-0.03754187747836113f,-0.01904250867664814f,0.021855978295207024f,-0.06356917321681976f,-0.016359394416213036f,0.013317807577550411f,-0.02106299251317978f,-0.023050887510180473f,-0.006213528569787741f,-0.018053993582725525f,-0.04797017574310303f,0.023333320394158363f,-0.06882677972316742f,-0.03391369804739952f,0.0693916454911232f,0.019205451011657715f,-0.03397887572646141f,0.03756360337138176f,-0.0026288004592061043f,-0.01542519312351942f,-0.030003085732460022f,0.08585966378450394f,0.03567347303032875f,-0.021682173013687134f,0.04375540465116501f,0.049013007432222366f,0.020617617294192314f,0.02919923886656761f,0.04203908145427704f,-0.002894939389079809f,-0.01682649552822113f,-0.03354436159133911f,0.041213508695364f,0.009037859737873077f,-0.0049452949315309525f,-0.022594649344682693f,0.0014013027539476752f,0.0011297324672341347f,0.012839843519032001f,-0.03132835030555725f,0.013111414387822151f,0.03634696826338768f,0.008310050703585148f,0.009575569070875645f,0.003188235219568014f,-0.027678444981575012f,-0.021725624799728394f,-0.014175969175994396f,0.029612025246024132f,-0.010183886624872684f,0.055791400372982025f,0.026722516864538193f,0.03237117826938629f,0.04064864292740822f,-0.03376162052154541f,0.023615753278136253f,-0.03593418002128601f,0.03356608748435974f,0.025006193667650223f,-0.09767840802669525f,-0.00935831293463707f,0.023767832666635513f,0.016163863241672516f,-0.0024712898302823305f,-0.008945525623857975f,-0.0026885459665209055f,-0.03602108359336853f,-0.006322156637907028f,0.017532577738165855f,-0.014056478627026081f,-0.007468183059245348f,-0.049447521567344666f,-0.03667285293340683f,-0.048752300441265106f,0.006284136790782213f,0.006479667499661446f,0.049360617995262146f,-0.03995342180132866f,0.017510853707790375f,-0.014273734763264656f,0.08138418942689896f,0.09594035148620605f,-0.005979978013783693f,0.002452279906719923f,-0.03526068851351738f,5.804814863950014E-4f,0.028438841924071312f,-0.03154560551047325f,0.052793264389038086f,-0.014643070288002491f,-0.009537548758089542f,0.04143076390028f,0.01841246709227562f,-0.056747328490018845f,-0.025788314640522003f,0.03810674324631691f,0.03250153362751007f,0.008505581878125668f,0.021247660741209984f,0.018640585243701935f,-0.010384848341345787f,0.02967720292508602f,-0.03445683792233467f,-0.012361880391836166f,0.04397266358137131f,-0.03980134427547455f,0.03515205904841423f,-0.022442569956183434f,-0.019987573847174644f,0.11636244505643845f,0.03695528581738472f,-0.01697857491672039f,-0.018836116418242455f,-0.04423337057232857f,-0.03006826341152191f,-0.020117927342653275f,0.051055215299129486f,0.014719109982252121f,6.218960043042898E-4f,-0.03058967925608158f,0.011427678167819977f,-0.013046236708760262f,-0.0038617297541350126f,-2.829423174262047E-4f,-0.04979512840509415f,-0.012198938056826591f,-0.020226554945111275f,0.03743325173854828f,-0.0033837659284472466f,-0.0029302434995770454f,-0.04303846135735512f,0.01732618547976017f,-0.04049656167626381f,0.004119721241295338f,-0.005871349945664406f,0.04631903022527695f,0.014404088258743286f,-0.03806329146027565f,-0.007082553580403328f,0.013730593957006931f,-0.022486019879579544f,-0.07690870761871338f,-0.027613267302513123f,0.06335192173719406f,-0.010102415457367897f,-0.050924863666296005f,0.04684044420719147f,0.06326501816511154f,-0.004738901741802692f,-0.09072620421648026f,-0.049751680344343185f,0.057790160179138184f,0.013111414387822151f,0.018032267689704895f,-0.006202665623277426f,0.03610798716545105f,0.012883295305073261f,-0.005974546540528536f,0.012666039168834686f,-0.020791422575712204f,0.058615732938051224f,0.024245796725153923f,0.03135007619857788f,0.006034291815012693f,0.06013652682304382f,-0.03232772648334503f,0.005260316655039787f,0.023420222103595734f,-0.00903242826461792f,-0.021095581352710724f,-0.01721755601465702f,-0.011720973998308182f,0.056530073285102844f,0.017923640087246895f,-0.010797634720802307f,-0.03213219717144966f,2.6036801864393055E-4f,0.008815172128379345f,-0.006099469028413296f,0.01676131971180439f,-0.04653628543019295f,-0.00740843778476119f,-0.03239290416240692f,0.049968935549259186f,0.08907505869865417f,0.0212585236877203f,-0.03593418002128601f,0.008587053045630455f,0.03710736706852913f,-0.004529792349785566f,-0.06039723381400108f,-0.02047640085220337f,-0.01096600852906704f,0.04605832323431969f,-0.03098073974251747f,0.0027102716267108917f,0.012220663018524647f,0.04279948025941849f,-0.0059202322736382484f,0.049882031977176666f,0.019987573847174644f,0.005860486999154091f,-0.03450028970837593f,-0.004269084893167019f,0.03067658096551895f,0.029720652848482132f,0.008364365436136723f,-0.07499685138463974f,0.01723928190767765f,-0.0012953903060406446f,-0.026852870360016823f,0.024897564202547073f,-0.03630351647734642f,-0.013317807577550411f,-0.03058967925608158f,-0.01765207014977932f,-0.017836736515164375f,0.03293604403734207f,-0.028873354196548462f,0.052315302193164825f,-0.01005353219807148f,0.0030959013383835554f,-0.018119169399142265f,-0.011416815221309662f,0.01721755601465702f,-0.055443793535232544f,-0.025266900658607483f,-0.018032267689704895f,-0.03858470916748047f,-0.027765346691012383f,0.013198316097259521f,0.04201735556125641f,-0.06326501816511154f,-0.0303506962954998f,-0.0035738651640713215f,-0.03567347303032875f,0.017337048426270485f,0.007859244011342525f,0.07256358116865158f,0.04210425913333893f,0.0019811054226011038f,-0.024810662493109703f,-0.0071314359083771706f,0.0357821024954319f,-0.008891211822628975f,0.02995963580906391f,0.04134386405348778f,-0.07847295701503754f,-0.0329577699303627f,0.02036777324974537f,-0.01715238019824028f,8.187844650819898E-4f,0.03332710638642311f,0.03499998152256012f,-0.04240841791033745f,-0.022985709831118584f,0.001082207658328116f,0.013209179043769836f,-0.005882212892174721f,0.011069205589592457f,0.03421785682439804f,-0.024767210707068443f,-0.015837980434298515f,-0.04130041226744652f,0.027743620797991753f,-0.011427678167819977f,-0.014773423783481121f,0.019857220351696014f,-0.050229642540216446f,0.028112957254052162f,-0.018336426466703415f,-0.05905024707317352f,-0.009396332316100597f,0.030046537518501282f,-0.014034752734005451f,0.004182182718068361f,0.01595747098326683f,-0.01819521002471447f,-0.025853492319583893f,0.03308812528848648f,0.02998136170208454f,0.023485399782657623f,-0.020161379128694534f,0.049621324986219406f,0.011427678167819977f,-0.052836716175079346f,0.002970979083329439f,0.03167596086859703f,-0.023941637948155403f,0.029612025246024132f,0.03126317262649536f,-0.058355025947093964f,-0.01474083587527275f,-0.019422708079218864f,-0.008114520460367203f,0.0199549850076437f,0.03228427842259407f,0.010710733011364937f,-0.019270628690719604f,-0.03695528581738472f};
        SortedMap<Long, Float> sparse = new TreeMap<>() {
            private static final long serialVersionUID = 2612517152150937157L;
            {
                put(3573L, 0.34701499f);
                put(5263L, 0.263937551f);
            }
        };
//        sparse.put(3573L, 0.34701499f);
//        sparse.put(5263L, 0.263937551f);
        List<BaseVector> queryDenseVectors = Collections.singletonList(new FloatVec(dense));
        List<BaseVector> querySparseVectors = Collections.singletonList(new SparseFloatVec(sparse));

        List<AnnSearchReq> searchRequests = new ArrayList<>();
        searchRequests.add(AnnSearchReq.builder()
                .vectorFieldName("dense")
                .vectors(queryDenseVectors)
                .metricType(IndexParam.MetricType.IP)
//                .params("{\"nprobe\": 10}")
                .topK(2)
                .build());
        searchRequests.add(AnnSearchReq.builder()
                .vectorFieldName("sparse")
                .vectors(querySparseVectors)
                .metricType(IndexParam.MetricType.IP)
//                .params("")
                .topK(2)
                .build());
        return searchRequests;
    }

    /**
     * @description 生成重新排名策略
     * <p> <功能详细描述> </p>
     *
     * @author 陈晨
     * @date 2025/3/3 11:02
     */
    public BaseRanker generateReranker() {
        // 要合并两组 ANN 搜索结果并对其进行重新排序，必须选择适当的重新排序策略
        // Zilliz 支持两种类型的重新排名策略：WeightedRanker 和 RRFRanker
        // 在选择重新排名策略时，要考虑的一件事是是否强调向量字段上的一个或多个基本 ANN 搜索

        // WeightedRanker：如果您要求结果强调特定的向量字段，建议使用此策略
        // WeightedRanker允许您为某些向量字段分配更高的权重，从而更加强调它们
        // 例如，在多模态搜索中，图像的文本描述可能被认为比图像中的颜色更重要。
        BaseRanker weighted = new WeightedRanker(Arrays.asList(0.8f, 0.3f));

        // RRFRanker（互惠等级融合排名）：在没有具体强调的情况下推荐这种策略
        // RRF可以有效地平衡每个向量场的重要性
        BaseRanker rrf = new RRFRanker(100);
        return rrf;
    }

    public static void main(String[] args) {
        MilvusClientV2 client = MilvusClient.connect(MilvusDemo.CLUSTER_ENDPOINT);
        MilvusCollection collection = MilvusCollection.init(client);
        MilvusHybridSearch hybrid = new MilvusHybridSearch(client);

        String collectionName = "hybrid_search_collection";
        // 删除集合
        collection.drop(collectionName);
        // 生成Schema
        CreateCollectionReq.CollectionSchema schema = hybrid.generateSchema();
        // 生成索引
        List<IndexParam> indexParams = hybrid.generateIndex();
        // 创建集合
        collection.create(collectionName, schema, indexParams);
        // 插入数据
        InsertResp result = hybrid.insertData(collectionName);
        System.out.println("\n\n插入数据：");
        System.out.println(result);
        // 生成查询对象
        List<AnnSearchReq> searchRequests = hybrid.generateSearch();
        // 生成重新排名策略
        BaseRanker reranker = hybrid.generateReranker();

        // 查询
        HybridSearchReq hybridSearchReq = HybridSearchReq.builder()
                .collectionName(collectionName)
                .searchRequests(searchRequests)
                .ranker(reranker)
                .topK(2)
                .consistencyLevel(ConsistencyLevel.BOUNDED)
                .build();
        SearchResp searchResp = client.hybridSearch(hybridSearchReq);
        System.out.println("\n\n查询数据：");
        System.out.println(searchResp);
    }

}


