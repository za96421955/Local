package com.chen.local.base.services.export;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.Table;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.chen.local.base.Content;
import com.chen.local.base.Result;
import com.chen.local.base.services.AbstractService;
import com.chen.local.base.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 功能描述: 抽象导出服务类实现
 * @date: 2019/9/18 19:50
 */
public abstract class AbstractExportServiceImpl<T> implements AbstractService<ExportRequestDto> {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String getLogMark() {
        return "文件导出";
    }

    @Override
    public String getVersion() {
        return Content.VERSION.V_1_0;
    }

    @Override
    public Result execute(ExportRequestDto request) {
        if (request == null) {
            return null;
        }
        // 设置文件名
        String date = DateUtil.format(DateUtil.now(), DateUtil.DATE_ALL_SERIAL);
        int random = new Random().nextInt(900) + 100;
        String fileName = this.getFileName() + "_" + date + random + ".xlsx";

        Result uploadResult = null;
        OutputStream out = null;
        try {
            // 文件写入
            long beginTime = System.currentTimeMillis();
            out = new FileOutputStream(fileName);
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);
            this.fileWrite(request, writer);
            writer.finish();
            out.flush();
            long consumeTime = System.currentTimeMillis() - beginTime;
            logger.info("【{}】request={}, 文件写入完成, 耗时={}ms"
                    , this.getLogMark(), request, consumeTime);

            // 文件上传
            beginTime = System.currentTimeMillis();
//            uploadResult = OSSUploadService.upload(fileName);
            consumeTime = System.currentTimeMillis() - beginTime;
            logger.info("【{}】request={}, uploadResult={}, 文件上传完成, 耗时={}ms"
                    , this.getLogMark(), request, uploadResult, consumeTime);
        } catch (Exception e) {
            logger.error("【{}】request={}, 文件导出异常, {}"
                    , this.getLogMark(), request, e.getMessage(), e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    logger.error("【{}】request={}, 文件流关闭异常, {}"
                            , this.getLogMark(), request, e.getMessage(), e);
                }
            }
        }
        return uploadResult;
    }

    /**
     * @description 文件写入
     * <p>〈功能详细描述〉</p>
     *
     * @auther  陈晨
     * @date    2020/1/4 19:10
     * @param   request, writer
     */
    private void fileWrite(ExportRequestDto request, ExcelWriter writer) {
        if (request == null || writer == null) {
            return;
        }
        // 设置sheet
        Sheet sheet = new Sheet(1, 0);
        sheet.setSheetName(this.getFileName());
        // 设置标题
        Table table = new Table(1);
        setHead(table);

        for (int i = 1; i <= this.getMaxPage(); i++) {
            List<T> dataList;
            try {
                dataList = this.queryByPage(request, i);
            } catch (Exception e) {
                logger.error("【{}】request={}, currPage={}, 数据查询异常, {}"
                        , this.getLogMark(), request, i, e.getMessage(), e);
                continue;
            }
            if (CollectionUtils.isEmpty(dataList)) {
                break;
            }

            try {
                for (T data : dataList) {
                    if (data == null) {
                        continue;
                    }
                    List<List<String>> writeData = new ArrayList<>();
                    writeData.add(this.convertData2StringList(data));
                    writer.write0(writeData, sheet, table);
                }
            } catch (Exception e) {
                logger.error("【{}】request={}, currPage={}, 数据查询成功, 转换/写入异常, {}"
                        , this.getLogMark(), request, i, e.getMessage(), e);
            }
        }
    }

    private void setHead(Table table) {
        String[] titles = this.getTitles();
        List<List<String>> headList = new ArrayList<>();
        for (String title : titles) {
            headList.add(Arrays.asList(title));
        }
        table.setHead(headList);
    }

    /**
     * @description 获取文件名
     * <p>〈功能详细描述〉</p>
     *
     * @auther  陈晨
     * @date    2020/1/4 18:54
     */
    public abstract String getFileName();

    /**
     * @description 获取文件标题
     * <p>〈功能详细描述〉</p>
     *
     * @auther  陈晨
     * @date    2020/1/4 18:48
     */
    public abstract String[] getTitles();

    /**
     * @description 获取最大页数
     * <p>〈功能详细描述〉</p>
     *
     * @auther  陈晨
     * @date    2020/1/4 18:59
     */
    public abstract int getMaxPage();

    /**
     * @description 分页查询数据
     * <p>〈功能详细描述〉</p>
     *
     * @auther  陈晨
     * @date    2020/1/4 19:03
     * @param   request, currPage
     */
    public abstract List<T> queryByPage(ExportRequestDto request, int currPage);

    /**
     * @description 数据转换为List集合
     * <p>〈功能详细描述〉</p>
     *
     * @auther  陈晨
     * @date    2020/1/4 19:03
     * @param   data
     */
    public abstract List<String> convertData2StringList(T data);

}


