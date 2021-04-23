//package middle.cc.SMS.thread;
//
//import java.util.Date;
//
//import middle.cc.Dater;
//import middle.cc.SMS.SMSContent;
//import middle.cc.SMS.SMSEntity;
//
///**
// * 企信通短信线程
// * @author 陈晨
// */
//public class QXTThread extends Thread {
//	private SMSContent sms = SMSContent.getSMSContent();
//
//	public void run() {
//		Date date;
//		while (true) {
//			//判断是否是 9:00-21:00
//			date = new Date();
//			if (sms.getQxtList().size() > 0
//					&& Dater.getDater().getHour(date) >= 9
//					&& Dater.getDater().getHour(date) <= 21) {
//				//一次发送1条
//				//发送企信通短信
//				sendQXT(sms.getQxtList().get(0));
//				//将发送的短信从队列内删除
//				sms.getQxtList().remove(0);
//			}
//
//			//休眠1秒
//			try {
//				Thread.sleep(1000);
//			} catch (Exception e) {
//				System.out.println("== QXTThread:休眠异常 ==");
//			}
//		}
//	}
//
//	private String sendQXT(SMSEntity entity) {
//		//前号码为目标，后号码为发送
//		String rtn = sms.getQxtClient().sendMsg(sms.getQxtClient(), 0, entity.getTargetPhone(), entity.getMessage(), 1);
//		return rtn;
//	}
//}
