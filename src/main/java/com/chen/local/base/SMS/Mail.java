//package middle.cc.SMS;
//
//import java.util.Date;
//import java.util.Properties;
//
//import javax.mail.Address;
//import javax.mail.Authenticator;
//import javax.mail.Message;
//import javax.mail.PasswordAuthentication;
//import javax.mail.SendFailedException;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//
//import org.springframework.mail.MailAuthenticationException;
//
//import middle.cc.Constants;
//import middle.ori.util.Base64;
//
///**
// * 邮件类
// * @author 陈晨
// *
//账号：02586588107
//密码：winroot123456
//发件时显示邮箱别名：eyun_service@189.cn
//待发送邮件排成一个队列，1秒钟1条。如果网络或服务器服务中断，待恢复后继续发送。
// */
//public class Mail {
//	/**
//	 * Email认证
//	 * @author 陈晨
//	 */
//	public class EmailAuthenticator extends Authenticator {
//		private String userName = "02586588107";//"96421955";//
//		private String password = "winroot123456";//"cimfor520626";//
//
//		public EmailAuthenticator() {
//			super();
//		}
//
//		public EmailAuthenticator(String userName, String password) {
//			this.userName = userName;
//			this.password = password;
//		}
//
//		protected PasswordAuthentication getPasswordAuthentication() {
//			return new PasswordAuthentication(userName, password);
//		}
//	}
//
//	public Mail(String mailTo, String mailBody) {
//		setMailTo(mailTo);
//		setMailBody(mailBody);
//	}
//
//	/** 邮件服务器地址 */
//	private String host = "smtp.189.cn";//"smtp.163.com";//
//	/** 邮件头 name */
//	private String mailHeadName = "翼云存储服务邮件";
//	/** 邮件头 value */
//	private String mailHeadValue = "翼云存储服务邮件";
//	/** 目标邮箱 */
//	private String mailTo = "";
//	/** 发送邮箱 */
//	private String mailFrom = "eyun_service@189.cn";//"96421955@163.com";//
//	/** 邮件主题 */
//	private String mailSubject = "翼云存储开发团队";
//	/** 邮件内容 */
//	private String mailBody = "";
//	/** 发送人 name */
//	private String personalName = "翼云服务";
//
//	public String getMailTo() {
//		return mailTo;
//	}
//	public void setMailTo(String mailTo) {
//		this.mailTo = mailTo;
//	}
//	public String getMailSubject() {
//		return mailSubject;
//	}
//	public void setMailSubject(String mailSubject) {
//		this.mailSubject = mailSubject;
//		this.mailSubject = Constants.encodeUTF8(this.mailSubject);
//	}
//	public String getMailBody() {
//		return mailBody;
//	}
//	public void setMailBody(String mailBody) {
//		this.mailBody = mailBody;
////			+ (char)13 + (char)10
////			+ (char)13 + (char)10
////			+ "http://www.189eyun.com/"
////			+ (char)13 + (char)10
////			+ (char)13 + (char)10
////			+ "该邮件为系统自动发送，请勿回复!";
//		this.mailBody = Constants.encodeUTF8(mailBody);
//		System.out.println(this.mailBody);
//	}
//
//	/**
//	 * 发送邮件
//	 * @throws SendFailedException
//	 */
//	public void sendMail() throws SendFailedException {
//		try {
//			Properties props = new Properties();	//获取系统环境
//			Authenticator auth = new EmailAuthenticator();	//进行邮件服务用户认证
//
//			props.put("mail.smtp.host", host);
//			props.put("mail.smtp.auth", "true");	//允许smtp校验
//			//设置 session,和邮件服务器进行通讯
//			Session session = Session.getInstance(props, auth);
//			MimeMessage message = new MimeMessage(session);
//			System.out.println("== 环境设置完成 ==");
//
//			message.setSubject("=?UTF-8?B?" + mailSubject + "?=");
//			message.setContent("Hello", "text/plain;charset=UTF-8");	//设置邮件格式
//			message.setSubject(mailSubject);	//设置邮件主题
//			message.setText(mailBody);	//设置邮件内容
//			message.setHeader(mailHeadName, mailHeadValue);	//设置邮件标题
//			message.setSentDate(new Date());	//设置邮件发送时间
//
//			System.out.println("== 邮件内容设置完成 ==");
//			//设置发件人
//			Address address = new InternetAddress(mailFrom, personalName);
//			message.setFrom(address);
//			//设置收件人
//			Address toAddress = new InternetAddress(mailTo);
//			message.addRecipient(Message.RecipientType.TO, toAddress);
//			System.out.println("== 邮件发件人，收件人设置完成 ==");
//
//			Transport.send(message);
//			System.out.println("== 邮件发送完成 ==");
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new SendFailedException("邮件发送失败：" + e.getMessage());
//		}
//	}
//
//	public static void main(String[] args) {
//		System.out.println("== SEND ==");
//		try {
//			Mail mail = new Mail("405348862@qq.com", "测试邮件内容！");
//			mail.sendMail();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println("== END ==");
//	}
//}
