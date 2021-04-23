//package com.chen.local.services.export;
//
//import com.alibaba.excel.context.WriteContext;
//import com.alibaba.excel.exception.ExcelGenerateException;
//import com.alibaba.excel.support.ExcelTypeEnum;
//import com.alibaba.excel.write.ExcelBuilderImpl;
//import org.apache.poi.xssf.streaming.SXSSFWorkbook;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.lang.reflect.Field;
//
//public class ExcelBuilderImplByJwms extends ExcelBuilderImpl {
//    protected Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    public ExcelBuilderImplByJwms(InputStream templateInputStream, OutputStream out, ExcelTypeEnum excelType, boolean needHead) {
//        super(templateInputStream, out, excelType, needHead);
//    }
//
//    @Override
//    public void finish() {
//        try {
//            WriteContext context = getContext();
//            context.getWorkbook().write(context.getOutputStream());
//            if (context.getWorkbook() instanceof SXSSFWorkbook) {
//                SXSSFWorkbook workbook = (SXSSFWorkbook) context.getWorkbook();
//                workbook.dispose();
//                logger.info("【新版报表导出】,临时文件删除成功");
//            }
//            context.getWorkbook().close();
//        } catch (Exception var2) {
//            logger.error("【新版报表导出】,临时文件删除失败, message={}", var2.getMessage(), var2);
//            throw new ExcelGenerateException("IO error", var2);
//        }
//    }
//
//    /**
//     *
//     * 功能描述:
//     *
//     * @param:
//     * @return:
//     * @auther: Panda (18066659)
//     * @date: 2019/10/21 21:03
//     */
//    public WriteContext getContext() throws NoSuchFieldException, IllegalAccessException {
//        Field field =  this.getClass().getSuperclass().getDeclaredField("context");
//        field.setAccessible(true);
//        return (WriteContext) field.get(this);
//    }
//
//}
//
//
