//package com.chen.local.services.export;
//
//import com.alibaba.excel.ExcelWriter;
//import com.alibaba.excel.support.ExcelTypeEnum;
//
//import java.io.OutputStream;
//import java.lang.reflect.Field;
//
//public class ExcelWriterByJwms extends ExcelWriter {
//
//    public ExcelWriterByJwms(OutputStream outputStream, ExcelTypeEnum typeEnum)
//            throws NoSuchFieldException, IllegalAccessException {
//        this(outputStream, typeEnum, true);
//    }
//
//    public ExcelWriterByJwms(OutputStream outputStream, ExcelTypeEnum typeEnum, boolean needHead)
//            throws NoSuchFieldException, IllegalAccessException {
//        super(outputStream, typeEnum, needHead);
//        ExcelBuilderImplByJwms excelBuilderByJwms = new ExcelBuilderImplByJwms(
//                null, outputStream, typeEnum, needHead);
//
//        Field excelBuilder = this.getClass().getSuperclass().getDeclaredField("excelBuilder");
//        excelBuilder.setAccessible(true);
//        excelBuilder.set(this, excelBuilderByJwms);
//    }
//
//}
//
//
