//package middle.cc.SMS.thread;
//
//import middle.cc.SMS.SMSContent;
//import middle.cc.SMS.SMSEntity;
//
///**
// * 招财宝短信线程
// * @author 陈晨
// */
//public class ZCBThread extends Thread {
//	private SMSContent sms = SMSContent.getSMSContent();
//
//	public void run() {
//		while (true) {
//			if (sms.getZcbList().size() > 0) {
//				//一次发送1条
//				//发送招财宝短信
//				sendZCB(sms.getZcbList().get(0));
//				//将发送的短信从队列内删除
//				sms.getZcbList().remove(0);
//			}
//
//			//休眠1秒
//			try {
//				Thread.sleep(1000);
//			} catch (Exception e) {
//				System.out.println("== ZCBThread:休眠异常 ==");
//			}
//		}
//	}
//
//	private String sendZCB(SMSEntity entity) {
//		//前号码为目标，后号码为发送
//		String rtn = sms.getZcbClient().sendProxyMsgMobile(sms.getZcbClient(), 0, entity.getTargetPhone(),
//				entity.getMessage(), 1, entity.getPhone());
//		return rtn;
//	}
//}
