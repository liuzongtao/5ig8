/**
 * 
 */
package cn.guba.igu8.core.sms;

import java.io.IOException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;

/**
 * @author zongtao liu
 *
 */
public class SmsWebChinese {

	private static Log log = Logs.get();

	private String account = "";

	private String key = "";

	public SmsWebChinese(String account, String key) {
		this.account = account;
		this.key = key;
	}

	public void sendSms(String phoneNumerArrStr, String content) {
		if (Strings.isBlank(phoneNumerArrStr) || Strings.isBlank(content)) {
			return;
		}
		try {
			HttpClient client = new HttpClient();
			PostMethod post = new PostMethod("http://gbk.sms.webchinese.cn");
			post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=gbk");// 在头文件中设置转码
			NameValuePair[] data = { new NameValuePair("Uid", account), new NameValuePair("Key", key),
					new NameValuePair("smsMob", String.valueOf(phoneNumerArrStr)),
					new NameValuePair("smsText", content) };
			post.setRequestBody(data);
			client.executeMethod(post);
			Header[] headers = post.getResponseHeaders();
			int statusCode = post.getStatusCode();
			log.debug("statusCode:" + statusCode);
			for (Header h : headers) {
				log.debug("Header == " + h.toString());
			}
			String result = new String(post.getResponseBodyAsString().getBytes("gbk"));
			log.info("phoneNumerArrStr == " + phoneNumerArrStr + " ;content == " + content +  " ;result == " + result);// 打印返回消息状态
			post.releaseConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
