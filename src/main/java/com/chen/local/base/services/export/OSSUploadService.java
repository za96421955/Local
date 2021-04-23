//package com.chen.local.services.export;
//
//import com.suning.logistics.jwms.entity.ResultVO;
//import net.oss.client.OSSClient;
//import net.oss.result.SdossResult;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.HashMap;
//
///**
// * @description OSS对象存储系统文件上传服务
// * <p>〈功能详细描述〉</p>
// *
// * @author 林晓咏 17091563
// */
//public class OSSUploadService {
//	private static final Logger logger = LoggerFactory.getLogger(OSSUploadService.class);
//	private static final String LOG_MARK = "OSS文件上传";
//	private static final String EXCEL_MIME_TYPE = "application/vnd.ms-excel";
//
//	/**
//	 * @description 文件上传
//	 * <p>〈功能详细描述〉</p>
//	 *
//	 * @author 林晓咏 17091563
//	 * @param   fileName
//	 */
//	public static ResultVO upload(String fileName) {
//		SdossResult authorized = OSSClientService.authorize(fileName);
//		if (!authorized.isSuccess()) {
//			logger.error("【{}】fileName={}, 授权失败", LOG_MARK, fileName);
//			return ResultVO.buildError(fileName + "授权失败");
//		}
//
//		OSSClient client = OSSClientService.get();
//		InputStream input = null;
//		try {
//			String url = getUploadUrl(authorized);
//			logger.info("【{}】url={}, 文件上传路径", LOG_MARK, url);
//
//			input = new FileInputStream(fileName);
//			SdossResult uploaded = client.putObject(
//					input
//					, fileName
//					, EXCEL_MIME_TYPE
//					, new HashMap<>()
//					, authorized.getAuthorization()
//					, authorized.getCurrent_time()
//					, url
//			);
//			if (!uploaded.isSuccess()) {
//				logger.error("【{}】url={}, 文件上传失败", LOG_MARK, url);
//				return ResultVO.buildError(fileName + "文件上传失败");
//			}
//			logger.info("【{}】url={}, 文件上传成功", LOG_MARK, url);
//			return ResultVO.buildSuccess(getUploadUrl(authorized) + fileName);
//		} catch (Exception e) {
//			logger.error("【{}】fileName={}, 文件上传异常, {}"
//					, LOG_MARK, fileName, e.getMessage(), e);
//			return ResultVO.buildError(fileName + "文件上传异常, " + e.getMessage());
//		} finally {
//			try {
//				if (input != null) {
//					input.close();
//				}
//			} catch (IOException e) {
//				logger.error("输入流关闭异常, {}", e.getMessage(), e);
//			}
//		}
//	}
//
//	private static String getUploadUrl(SdossResult authorized) {
//		return authorized.getFileraddr() + "/"
//				+ authorized.getAccount_id() + "/"
//				+ OSSClientService.OSS_BUCKET_NAME + "/"
//				+ authorized.getObjectId();
//	}
//
//}
//
//
