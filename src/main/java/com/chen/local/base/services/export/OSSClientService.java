//package com.chen.local.services.export;
//
//import com.suning.logistics.jwms.exception.RuntimeException;
//import net.oss.client.OSSClient;
//import net.oss.client.OSSClientImpl;
//import net.oss.result.SdossResult;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @description OSS对象存储系统访问服务
// * <p>〈功能详细描述〉</p>
// *
// * @author 林晓咏 17091563
// */
//public class OSSClientService {
//	private static final Logger logger = LoggerFactory.getLogger(OSSClientService.class);
//	static final String OSS_BUCKET_NAME = "query-data-export";
//
//	private static OSSClient client;
//	public static synchronized OSSClient get() {
//		if (client == null) {
//			client = new OSSClientImpl(false);
//		}
//		return client;
//	}
//
//	/**
//	 * @description 访问授权
//	 * <p>〈功能详细描述〉</p>
//	 *
//	 * @author 林晓咏 17091563
//	 * @param   objectName
//	 */
//	public static SdossResult authorize(String objectName) {
//		Map<String, String> headerArgument = new HashMap<>();
//		SdossResult result;
//		try {
//			result = get().putObject(OSS_BUCKET_NAME, objectName, headerArgument);
//			logger.info("文件对象={}, 获取权限={}", objectName, result.toString());
//		} catch (RuntimeException ex) {
//			throw new RuntimeException("文件对象 " + objectName + " 获取 " + OSS_BUCKET_NAME + " bucket权限失败", ex);
//		}
//		return result;
//	}
//
//}
//
//
