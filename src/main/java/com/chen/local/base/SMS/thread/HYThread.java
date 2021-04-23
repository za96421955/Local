//package middle.cc.SMS.thread;
//
//import java.util.Date;
//
//import com.gus.hy.MySMGPSMProxy;
//import com.huawei.smproxy.comm.smgp.message.SMGPSubmitMessage;
//import com.huawei.smproxy.comm.smgp.message.SMGPSubmitRespMessage;
//import com.huawei.smproxy.util.Args;
//import com.huawei.smproxy.util.Cfg;
//
//import middle.cc.Constants;
//import middle.cc.Dater;
//import middle.cc.SMS.SMSContent;
//import middle.cc.SMS.SMSEntity;
//
///**
// * 行业短信线程
// * @author 陈晨
// */
//public class HYThread extends Thread {
//	private SMSContent sms = SMSContent.getSMSContent();
//
//	public void run() {
//		int index;
//		SMSEntity entity;
//		Date date;
//		while (true) {
//			//一次发送10条
//			index = 0;
//			while (index < 10 && sms.getHyList().size() > 0) {
//				entity = sms.getHyList().get(0);
//				//判断短信类型是否是 系统通知 或 站内分享短信
//				if (entity.getType() == 1
//						|| entity.getType() == 2) {
//					//判断是否是 9:00-21:00
//					date = new Date();
//					if (Dater.getDater().getHour(date) < 9
//							|| Dater.getDater().getHour(date) > 21) {
//						//不在 9:00-21:00 时间段内，则将该短信延后跳过该条短信
//						sms.getHyList().add(entity);
//						sms.getHyList().remove(0);
//						continue;
//					}
//				}
//
//				//发送行业短信
//				sendHY(entity);
//				//将发送的短信从队列内删除
//				sms.getHyList().remove(0);
//				index++;
//			}
//
//			//休眠1秒
//			try {
//				Thread.sleep(1000);
//			} catch (Exception e) {
//				System.out.println("== HYThread:休眠异常 ==");
//			}
//		}
//	}
//
//	private String sendHY(SMSEntity entity) {
//		String rtn = "";
//		try {
//			Args cfgArgs = new Cfg (Constants.getPath() + "WEB-INF/classes/config.xml").getArgs ("SMGPConnect");
//
//			MySMGPSMProxy mySMProxy = new MySMGPSMProxy (cfgArgs);
//
//			String[] rcvMobile = new String[1];
//			/**
//			 * 目标号码组
//			 */
//			rcvMobile[0] = entity.getTargetPhone();
//			SMGPSubmitMessage msg = new SMGPSubmitMessage (
//					6,
//					1,
//					0,
//					"",
//					"00",
//					"00",
//					"000",
//					8,
//					null,
//					null,
//					"106592996",
//					"106592996",
//					rcvMobile,
//					entity.getMessage(),
//					"") ;
//			SMGPSubmitRespMessage respMsg = (SMGPSubmitRespMessage)mySMProxy.send (msg);
////			if ( respMsg != null ) {
////				System.out.println(
////					"HY: Get SubmitResp Message Success! The status = "
////					+ respMsg.getStatus ()) ;
////			} else {
////				System.out.println("HY: Get SubmitResp Message Fail!");
////			}
//			mySMProxy.close ();
//			rtn = "0";
//		} catch (Exception e) {
//			rtn = "1";
//			e.printStackTrace();
//		}
//		return rtn;
//	}
//}
