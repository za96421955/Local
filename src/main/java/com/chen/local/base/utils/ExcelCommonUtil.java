package com.chen.local.base.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Excel辅助工具类
 */
public abstract class ExcelCommonUtil {

	private static Logger LOGGER = LoggerFactory.getLogger(ExcelCommonUtil.class);

	private ExcelCommonUtil() {}
	
	/**
	 * 判断字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str){
		if(str!=null && !"".equals(str)){
			return true;
		}
		return false;
	}
	
	/**
	   * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
	   * 
	   * @param dateDate
	   * @return
	   */
	public static String dateToStr(Date dateDate, String format) {
	   SimpleDateFormat formatter = new SimpleDateFormat(format);
	   return formatter.format(dateDate);
	}
	
	
	/**
	 * 判断是否为老版本的excel
	 * @Title: isExcel2003
	 * @param filePath   *.xls
	 * @return
	 * boolean
	 * @author wangqinghua 
	 * @date 2017-3-21 上午11:05:19
	 */
	public static boolean isExcel2003(String filePath){
        return isNotEmpty(filePath) && filePath.matches("^.+\\.(?i)(xls)$");
    }
	
	/**
	 * 判断是否为新版本的excel
	 * @Title: isExcel2007
	 * @param filePath   *.xlsx
	 * @return
	 * boolean
	 * @author wangqinghua 
	 * @date 2017-3-21 上午11:05:43
	 */
    public static boolean isExcel2007(String filePath){
        return isNotEmpty(filePath) && filePath.matches("^.+\\.(?i)(xlsx)$");
    }
	
	/**
	 * 根据单元格获取内容
	 * @Title: getCellFormatValue
	 * @param cell
	 * @return
	 * String
	 * @author wangqinghua 
	 * @date 2017-3-10 下午10:19:04
	 */
	public static String getCellFormatValue(Cell cell) {
		String result;
		switch (cell.getCellType()) {
        case HSSFCell.CELL_TYPE_STRING:
        	result = cell.getRichStringCellValue().getString();
            break;
        case HSSFCell.CELL_TYPE_NUMERIC:
        	if(HSSFDateUtil.isCellDateFormatted(cell)){  
                Date date = cell.getDateCellValue();
                result =  dateToStr(date,"yyyy-MM-dd HH:mm:ss");
	        }else{  
	        	result = String.valueOf(BigDecimal.valueOf(cell.getNumericCellValue()));
	        }
            break;
        case HSSFCell.CELL_TYPE_FORMULA:
        	result = String.valueOf(cell.getNumericCellValue());
            break;
        default:
        	result = "";
            break;
        }
		return result;
    }
	
	/*   
     * 列数据信息单元格样式 
     */    
    public static CellStyle getStyle(Workbook workbook) {
          // 设置字体  
          Font font = workbook.createFont();
          //设置字体名字   
          font.setFontName("Courier New");  
          //设置样式;   
          CellStyle style = workbook.createCellStyle();  
          //设置底边框;   
          style.setBorderBottom(BorderStyle.THIN);
          //设置底边框颜色;    
          style.setBottomBorderColor(HSSFColor.BLACK.index);  
          //设置左边框;     
          style.setBorderLeft(BorderStyle.THIN);
          //设置左边框颜色;   
          style.setLeftBorderColor(HSSFColor.BLACK.index);  
          //设置右边框;   
          style.setBorderRight(BorderStyle.THIN);
          //设置右边框颜色;   
          style.setRightBorderColor(HSSFColor.BLACK.index);  
          //设置顶边框;   
          style.setBorderTop(BorderStyle.THIN);
          //设置顶边框颜色;    
          style.setTopBorderColor(HSSFColor.BLACK.index);  
          //在样式用应用设置的字体;    
          style.setFont(font);  
          //设置自动换行;   
          style.setWrapText(false);  
          //设置水平对齐的样式为居中对齐;    
          style.setAlignment(HorizontalAlignment.CENTER);
          //设置垂直对齐的样式为居中对齐;   
          style.setVerticalAlignment(VerticalAlignment.CENTER);
           
          return style;  
      
    }  
	
	/**
	 * 导出文件
	 * @param request
	 * @param response
	 * @param list
	 * @param fileName
	 */
	public static void exportExcel(HttpServletRequest request, HttpServletResponse response
			, List<?> list, String fileName) {
		// 限制导出数量
		if (CollectionUtils.isEmpty(list) || list.size() > 10000) {
			throw new RuntimeException("查询结果太多，请加查询条件查询，当前导出限制10000条导出");
		}
		try {
			// 删除文件
			String filePath;
			if (request.getHeader("User-Agent") != null) {
				if (request.getHeader("User-Agent").toUpperCase().contains("MSIE")) {
					filePath = URLEncoder.encode(fileName, "UTF-8");
				} else {
					filePath = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
				}
			} else {
				filePath = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
			}
			filePath += ".csv";
			File file = new File(filePath);
			file.delete();
			
			// 输出内容
			StringBuilder content = new StringBuilder();
			for (int i = 0; i < list.size(); i++) {
				Object[] obj = (Object[]) list.get(i);
				if (obj == null) continue;
				StringBuilder line = new StringBuilder();
				for (int j = 0; j < obj.length; j++) {
				    if (obj[j] == null) {
                        line.append(",");
                    } else {
                        line.append(",").append(obj[j].toString());
				    }
//					if (obj[j] == null) continue;
//					line.append(",\t").append(obj[j].toString());
				}
				if (line.length() > 0) content.append(line.substring(1)).append("\n");
			}
			FileUtil.append(content.toString(), filePath, "GBK");
			
			// 读取文件
			FileInputStream fis = new FileInputStream(new File(filePath));
			BufferedInputStream bis = new BufferedInputStream(fis);
			// 输出文件
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "inline; filename=" + filePath);
			OutputStream os = response.getOutputStream();
			BufferedOutputStream bos = new BufferedOutputStream(os);
			byte[] buffer = new byte[4096];
			int count;
			while ((count = bis.read(buffer, 0, buffer.length)) > 0) {
				bos.write(buffer, 0, count);
			}
			bos.close();
			os.close();
			bis.close();
			fis.close();
		} catch(Exception e) {
			LOGGER.error("[exportExcel] fileName={}, 文件导出异常, {}"
					, fileName, e.getMessage(), e);
		}
	}
	
	
	/**
	 * 导出xlsx
	 * @param request
	 * @param response
	 * @param list
	 * @param fileName
	 */
	public static void exportXlsxExcel(HttpServletRequest request,
                                       HttpServletResponse response, List<?> list, String fileName) {
		//创建xlsx工作文档对象     
        Workbook wb = new XSSFWorkbook();
        //创建sheet对象     
        Sheet sheet = wb.createSheet("sheet1");
		//单元格样式对象
        CellStyle style = getStyle(wb);
        for (int i = 0; i < list.size(); ++i) {
			Row row = sheet.getRow(i);
			if (row == null) {
				row = sheet.createRow(i);
			}
			row.setHeightInPoints(18.0F);
			Object[] columnNames = (Object[]) list.get(i);
			for (int j = 0; j < columnNames.length; ++j) {
				Cell cell = row.getCell(j);
				if (cell == null) {
					cell = row.createCell(j);
				}
				cell.setCellStyle(style);
				cell.setCellValue(columnNames[j]+"");
				if(columnNames[j]!=null && i==2){
					if(columnNames[j].toString().length()<=11){
						sheet.setColumnWidth(j, 15*256);
					}else{
						sheet.setDefaultColumnWidth(25);
					}
				}
			}
		}
        File file = new File("workbook.xlsx");
        FileInputStream finput =null;
        String fileNameExcel;
        try{
			OutputStream os = new FileOutputStream(file);
			wb.write(os);
			os.flush();
			os.close();
			if(request.getHeader("User-Agent")!=null){
				if (request.getHeader("User-Agent").toUpperCase().contains("MSIE")) {
					fileNameExcel = URLEncoder.encode(fileName, "UTF-8");
				} else {
					fileNameExcel = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
				}
			}else{
				fileNameExcel = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
			}
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "inline; filename=" + fileNameExcel + ".xlsx");
			finput = new FileInputStream(file);
			OutputStream output = response.getOutputStream();
			BufferedInputStream buffin = new BufferedInputStream(finput);
			BufferedOutputStream buffout = new BufferedOutputStream(output);
			byte[] buffer = new byte[4096];
			int count;
			while ((count = buffin.read(buffer, 0, buffer.length)) > 0) {
				buffout.write(buffer, 0, count);
			}
			buffin.close();
			buffout.close();
			finput.close();
			output.close();
			
        }catch(Exception e){
			LOGGER.info("异常:",e);
        }finally {
        	if(finput != null){
        		try {
					finput.close();
				} catch (IOException e) {
					LOGGER.info("异常:",e);
				}
        	}
        }
	}
	
	
}

