/**
 * 
 */
package cn.guba.igu8.core.mail;

import java.util.Set;

import com.jfinal.kit.PropKit;

import cn.guba.igu8.core.constants.Constant;

/**
 * @author zongtao liu
 *
 */
public class MailFactory {

	private Email email;

	private static volatile MailFactory mailFactory;

	private MailFactory() {
		this.email = new Email(PropKit.get("emailAcc"), PropKit.get("emailKey"), Constant.EMAIL_NAME);
	}

	public static MailFactory getInstance() {
		if (mailFactory == null) {
			synchronized (MailFactory.class) {
				if (mailFactory == null) {
					mailFactory = new MailFactory();
				}
			}
		}
		return mailFactory;
	}

	/**
	 * 发送邮件
	 * 
	 * @param emailList
	 * @param teacherName
	 * @param mailContent
	 */
	public void sendEmail(Set<String> emailList, String teacherName, String mailContent) {
		if (email != null && emailList.size() > 0) {
			String mailSubject = Constant.EMAIL_NAME + ":" + teacherName;
			email.sendBccMessage(emailList, mailSubject, mailContent);
		}
	}
}
