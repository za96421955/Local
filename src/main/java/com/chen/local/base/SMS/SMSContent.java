//package middle.cc.SMS;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import middle.cc.SMS.thread.HYThread;
//import middle.cc.SMS.thread.MailThread;
//import middle.cc.SMS.thread.QXTThread;
//import middle.cc.SMS.thread.ZCBThread;
//
//import com.gus.zc.ReceiveDemo;
//import com.linkage.netmsg.NetMsgclient;
//import com.linkage.netmsg.server.ReceiveMsg;
//
///**
// * 短信池
// * @author 陈晨
// */
//public class SMSContent {
//	public static int HY_TYPE_SYSTEM = 1;
//	public static int HY_TYPE_MOBILE = 2;
//	public static int HY_TYPE_BIND = 3;
//	public static int HY_TYPE_LOGIN = 4;
//
//	/** 企信通客户端 */
//	private NetMsgclient qxtClient;
//	/** 招财宝客户端 */
//	private NetMsgclient zcbClient;
//	/** 行业短信池 */
//	private List<SMSEntity> hyList;
//	/** 企信通短信池 */
//	private List<SMSEntity> qxtList;
//	/** 招财宝短信池 */
//	private List<SMSEntity> zcbList;
//	/** 邮件池 */
//	private List<Mail> mailList;
//
//	private SMSContent() {
//		//初始化短信客户端
//		try {
////			initQXT();
////			initZCB();
//		} catch (Exception e) {
//			System.out.println("== 企信通，招财宝 初始化异常 ==");
//			e.printStackTrace();
//		}
//		//实例化短信池
//		hyList = new ArrayList<SMSEntity>();
//		qxtList = new ArrayList<SMSEntity>();
//		zcbList = new ArrayList<SMSEntity>();
//		mailList = new ArrayList<Mail>();
//	}
//
//	private static SMSContent smsContent;
//	public synchronized static SMSContent getSMSContent() {
//		if (smsContent == null) {
//			smsContent = new SMSContent();
//
//			//启动线程
////			HYThread hy = new HYThread();
////			QXTThread qxt = new QXTThread();
////			ZCBThread zcb = new ZCBThread();
////			MailThread mail = new MailThread();
//
////			hy.start();
////			qxt.start();
////			zcb.start();
////			mail.start();
//		}
//		return smsContent;
//	}
//
//	/*****************************************************
//	 *
//	 * 开放方法
//	 *
//	 *****************************************************/
//
//	public NetMsgclient getQxtClient() {
//		return qxtClient;
//	}
//
//	public NetMsgclient getZcbClient() {
//		return zcbClient;
//	}
//
//	public List<SMSEntity> getHyList() {
//		return hyList;
//	}
//
//	public List<SMSEntity> getQxtList() {
//		return qxtList;
//	}
//
//	public List<SMSEntity> getZcbList() {
//		return zcbList;
//	}
//
//	public List<Mail> getMailList() {
//		return mailList;
//	}
//
//	/**
//	 * 将一条行业短信加入队列
//	 */
//	public void addHY(String targetPhone, String message, int type) {
//		SMSEntity e = new SMSEntity();
//		e.setTargetPhone(targetPhone);
//		e.setMessage(message);
//		e.setType(type);
//		hyList.add(e);
//	}
//
//	/**
//	 * 将一条企信通短信加入队列
//	 */
//	public void addQXT(String phone, String targetPhone, String message) {
//		SMSEntity e = new SMSEntity();
//		e.setPhone(phone);
//		e.setTargetPhone(targetPhone);
//		e.setMessage(message);
//		qxtList.add(e);
//	}
//
//	/**
//	 * 将一条招财宝短信加入队列
//	 */
//	public void addZCB(String phone, String targetPhone, String message) {
//		SMSEntity e = new SMSEntity();
//		e.setPhone(phone);
//		e.setTargetPhone(targetPhone);
//		e.setMessage(message);
//		zcbList.add(e);
//	}
//
//	/**
//	 * 将一条邮件加入队列
//	 * @param mailTo
//	 * @param mailSubject
//	 * @param mailBody
//	 */
//	public void addMail(String mailTo, String mailBody) {
//		Mail mail = new Mail(mailTo, mailBody);
//		try {
//			mail.sendMail();
//		} catch (Exception e) {
//		}
////		mailList.add(mail);
//	}
//
//	/*****************************************************
//	 *
//	 * 私有方法
//	 *
//	 *****************************************************/
//
//	/**
//	 * 初始化企信通客户端
//	 * @throws IOException
//	 */
//	private void initQXT() throws IOException {
//		ReceiveMsg receiveMsg = new ReceiveDemo();
//
//		qxtClient = new NetMsgclient();
//		/**
//		 * 初始化企信通客户端，启动后永久挂起
//		 */
//		qxtClient = qxtClient.initParameters("218.94.58.243", 9005,
//				"jseea01", "FK_0123!", receiveMsg);
//		qxtClient.anthenMsg(qxtClient);
//	}
//
//	/**
//	 * 初始化招财宝客户端
//	 * @throws IOException
//	 */
//	private void initZCB() throws IOException {
//		ReceiveMsg receiveMsg = new ReceiveDemo();
//
//		zcbClient = new NetMsgclient();
//
//		/**
//		 * 初始化招财宝客户端，启动后永久挂起
//		 */
//		zcbClient = zcbClient.initParameters("218.94.58.243", 9005,
//				"02586788862", "ycc147852", receiveMsg);
//		zcbClient.anthenMsg(zcbClient);
//	}
//}
