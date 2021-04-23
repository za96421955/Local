//package middle.cc.SMS.thread;
//
//import middle.cc.SMS.SMSContent;
//
///**
// * 邮件线程
// * @author 陈晨
// */
//public class MailThread extends Thread {
//	private SMSContent sms = SMSContent.getSMSContent();
//
//	public void run() {
//		while (true) {
//			if (sms.getMailList().size() > 0) {
//				//一次发送1条
//				try {
//					//发送邮件
//					sms.getMailList().get(0).sendMail();
//					//将发送的短信从队列内删除
//					sms.getMailList().remove(0);
//				} catch (Exception e) {
//					System.out.println("== 邮件发送异常 ==");
//					e.printStackTrace();
//				}
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
//}
