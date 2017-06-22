/**
 * 
 */
package cn.guba.igu8.core.mail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.nutz.lang.Strings;

import com.jfinal.kit.PropKit;

import cn.guba.igu8.core.constants.Constant;

/**
 * @author zongtao liu
 *
 */
public class MailFactory {

	private List<Email> myEmailList;

	private static volatile MailFactory mailFactory;
	private static volatile int curIndex = 0;

	private MailFactory() {
		myEmailList = new ArrayList<Email>();
		String emailAcc1 = PropKit.get("emailAcc1");
		String emailKey1 = PropKit.get("emailKey1");
		if (Strings.isNotBlank(emailAcc1) && Strings.isNotBlank(emailKey1)) {
			Email email1 = new Email(emailAcc1, emailKey1, Constant.EMAIL_NAME);
			myEmailList.add(email1);
		}

		String emailAcc2 = PropKit.get("emailAcc2");
		String emailKey2 = PropKit.get("emailKey2");
		if (Strings.isNotBlank(emailAcc2) && Strings.isNotBlank(emailKey2)) {
			Email email2 = new Email(emailAcc2, emailKey2, Constant.EMAIL_NAME);
			myEmailList.add(email2);
		}
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
	 * @param emailAddrSet
	 * @param teacherName
	 * @param mailContent
	 */
	public boolean sendEmail(Set<String> emailAddrSet, String teacherName, String mailContent) {
		boolean result = false;
		if (myEmailList != null && myEmailList.size() > 0 && emailAddrSet.size() > 0) {
			Email email = myEmailList.get(curIndex);
			curIndex = (curIndex + 1) % myEmailList.size();
			List<String> emailAddrList = new ArrayList<String>(emailAddrSet);
			int limitSize = 20;
			int tmpIndex = 0;
			Set<String> tmpEmailAddrSet = getLimit(emailAddrList, tmpIndex, limitSize);
			while (tmpEmailAddrSet != null && tmpEmailAddrSet.size() > 0) {
				String mailSubject = Constant.EMAIL_NAME + ":" + teacherName;
				result = email.sendBccMessage(emailAddrSet, mailSubject, mailContent);
				if (!result) {
					break;
				}
				tmpIndex += tmpEmailAddrSet.size();
				tmpEmailAddrSet = getLimit(emailAddrList, tmpIndex, limitSize);
			}
		}
		return result;
	}

	/**
	 * 获取部分信息
	 * 
	 * @param list
	 * @param curIndex
	 * @param limit
	 * @return
	 */
	private Set<String> getLimit(List<String> list, int curIndex, int limit) {
		if (list == null || list.size() == 0) {
			return null;
		}
		int size = list.size();
		if (curIndex >= size) {
			return null;
		}
		int endIndex = curIndex + limit;
		if (endIndex > size) {
			endIndex = size;
		}
		return new HashSet<String>(list.subList(curIndex, endIndex));
	}
}
