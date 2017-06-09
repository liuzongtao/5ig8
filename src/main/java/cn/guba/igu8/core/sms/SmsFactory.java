/**
 * 
 */
package cn.guba.igu8.core.sms;

import java.util.HashSet;
import java.util.Set;

import com.jfinal.kit.PropKit;

/**
 * @author zongtao liu
 *
 */
public class SmsFactory {

	private static SmsWebChinese smsWebChinese;

	private static volatile SmsFactory smsFactory;

	private SmsFactory() {
		smsWebChinese = new SmsWebChinese(PropKit.get("smsAcc"), PropKit.get("smsKey"));
	}

	public static SmsFactory getInstance() {
		if (smsFactory == null) {
			synchronized (SmsFactory.class) {
				if (smsFactory == null) {
					smsFactory = new SmsFactory();
				}
			}
		}
		return smsFactory;
	}

	/***
	 * 发送短消息
	 * 
	 * @param phoneSet
	 * @param content
	 */
	public void sendSms(Set<Long> phoneSet, String content) {
		if (smsWebChinese != null) {
			int maxSize = 88;
			while (phoneSet.size() >= 88) {
				String phoneNumers = getPhoneStr(phoneSet, maxSize);
				smsWebChinese.sendSms(phoneNumers, content);
			}
			String plusPhoneNumers = getPhoneStr(phoneSet, maxSize);
			smsWebChinese.sendSms(plusPhoneNumers, content);
		}
	}

	public void sendSms(Long phone, String content) {
		if (smsWebChinese != null) {
			smsWebChinese.sendSms(String.valueOf(phone), content);
		}
	}

	/***
	 * 获取电话字符串
	 * 
	 * @param phoneSet
	 * @param length
	 * @return
	 */
	private String getPhoneStr(Set<Long> phoneSet, int length) {
		if (phoneSet == null || phoneSet.size() == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		int curIndex = 0;
		Set<Long> needRemove = new HashSet<Long>();
		for (Long phone : phoneSet) {
			if (curIndex == length) {
				break;
			}
			sb.append(phone).append(",");
			needRemove.add(phone);
			curIndex++;
		}
		phoneSet.removeAll(needRemove);
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

}
