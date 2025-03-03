package com.chen.local.ai;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chen.local.net.HTTPClientUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 向量计算
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2025/3/3
 */
public class Embedding {

//    public void load() throws IOException, UnsupportedKerasConfigurationException, InvalidKerasConfigurationException {
//        // 示例：加载BERT-base-uncased预训练模型
//        MultiLayerNetwork model = KerasModelImport.importKerasSequentialModelAndWeights(
//                Objects.requireNonNull(getClass().getResourceAsStream("/path/to/bert_model.h5"))
//        );
//        // 加载预训练模型
////        Word2Vec vec = WordVectorSerializer.readWord2VecModel("path/to/GoogleNews-vectors-negative300.bin");
//        Word2Vec vec = WordVectorSerializer.readWord2VecModel("wiki.zh.text.vector");
//        // 文本转换为向量（取平均或加权求和）
////        String text = "Java DL4J文本处理";
//        INDArray documentVector = vec.getWordVectorMatrix(text);
//        System.out.println(documentVector);
//    }

    /**
     * @description 获取多模态密集向量
     * <p> <功能详细描述> </p>
     *
     * @author 陈晨
     * @date 2025/3/3 17:24
     */
    public List<Float> getMultimodalDenseVectors(String text) {
        String url = "https://dashscope.aliyuncs.com/api/v1/services/embeddings/multimodal-embedding/multimodal-embedding";
        // contents 列表中包含本次需要进行向量计算的所有内容，并分别以列表形式单独呈现，每一个列表可以分别是图像（image），文本（text）或者视频（video）
        // {"text": "通用多模态表征模型"}
        // {"image": "https://xxxx.com/xxx/images/xxx.jpg"}
        // {"vedio": "https://xxx.com/xxx/video/xxx.mp4"}
        JSONObject contentText = new JSONObject();
        contentText.put("text", text);
        JSONArray contents = new JSONArray();
        contents.add(contentText);
        JSONObject input = new JSONObject();
        input.put("contents", contents);
        // 构建请求体
        JSONObject body = new JSONObject();
        body.put("model", "multimodal-embedding-v1");
        body.put("input", input);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result = HTTPClientUtil.post(httpClient, url, body.toJSONString(), httpPost -> {
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Authorization", "Bearer sk-2b416845775f4f028deecc3ce219b27e");
            return null;
        });
        System.out.println(result);
        JSONObject resultJson = JSONObject.parseObject(result);
        return resultJson.getJSONObject("output")
                .getJSONArray("embeddings").getJSONObject(0)
                .getJSONArray("embedding").stream()
                .map(obj -> ((Number) obj).floatValue())
                .collect(Collectors.toList());
    }

    /**
     * @description 获取文本密集向量
     * <p> <功能详细描述> </p>
     *
     * @author 陈晨
     * @date 2025/3/3 17:24
     */
    public List<Float> getTextDenseVectors(String text) {
        String url = "https://dashscope.aliyuncs.com/compatible-mode/v1/embeddings";
        // 构建请求体
        JSONObject body = new JSONObject();
        body.put("model", "text-embedding-v3");
        body.put("input", text);
        // dimensions: 1024/768/512
        body.put("dimensions", 768);
        body.put("encoding_format", "float");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result = HTTPClientUtil.post(httpClient, url, body.toJSONString(), httpPost -> {
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Authorization", "Bearer sk-2b416845775f4f028deecc3ce219b27e");
            return null;
        });
        System.out.println(result);
        JSONObject resultJson = JSONObject.parseObject(result);
        List<Float> denseVectors = resultJson.getJSONArray("data").getJSONObject(0)
                .getJSONArray("embedding").stream()
                .map(obj -> ((Number) obj).floatValue())
                .collect(Collectors.toList());
        System.out.println("\n\ndenseVectors:");
        for (Float vector : denseVectors) {
            System.out.println(vector);
        }
        System.out.println("size: " + denseVectors.size());
        return denseVectors;
    }

    /**
     * @description 密集向量 => 稀疏向量
     * <p> <功能详细描述> </p>
     *
     * @author 陈晨
     * @date 2025/3/3 17:25
     */
    public Map<Integer, Float> toSparseVector(List<Float> denseVectors) {
        Map<Integer, Float> sparseVectors = new HashMap<>();
        for (int i = 0; i < denseVectors.size(); i++) {
            if (denseVectors.get(i) > 0) {
                sparseVectors.put(i, denseVectors.get(i));
            }
        }
        System.out.println("\n\nsparseVectors:");
        for (Map.Entry<Integer, Float> vector : sparseVectors.entrySet()) {
            System.out.println(vector.getKey() + ": " + vector.getValue());
        }
        System.out.println("size: " + sparseVectors.size());
        return sparseVectors;
    }

    public static void main(String[] args) {
        String text = "Artificial intelligence was founded as an academic discipline in 1956.";

        Embedding embedding = new Embedding();
        List<Float> denseVectors = embedding.getTextDenseVectors(text);
        Map<Integer, Float> sparseVectors = embedding.toSparseVector(denseVectors);
    }

}


