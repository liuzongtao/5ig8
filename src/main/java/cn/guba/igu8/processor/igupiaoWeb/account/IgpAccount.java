/**
 * 
 */
package cn.guba.igu8.processor.igupiaoWeb.account;

import java.util.HashMap;
import java.util.Map;

import org.nutz.http.Cookie;
import org.nutz.http.Response;
import org.nutz.json.Json;

import cn.guba.igu8.core.constants.Constant;
import cn.guba.igu8.core.utils.HttpUtil;
import cn.guba.igu8.processor.igupiaoWeb.msg.beans.IgpWebStatusMsgBean;

/**
 * @author zongtao liu
 *
 */
public class IgpAccount {

	private String account;

	private String passwd;

	public IgpAccount(String account, String passwd) {
		this.account = account;
		this.passwd = passwd;
	}

	public Cookie login4Cokie(Cookie cookie) {
		String url = Constant.URL_IGP_LOGIN;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", account);
		params.put("passwd", passwd);
		params.put("backurl", "https://www.5igupiao.com/live/detail_new.php?id=91&act=");
		Response res = HttpUtil.httpsPost(url, params, cookie);
//		System.out.println(res.getContent());
		return res.getCookie();
	}

	public int getUid(Cookie cookie) {
		Response httpsGet = HttpUtil.httpsGet(Constant.URL_IGP_USER_STATUS, null, cookie);
		return getUid(httpsGet.getContent("UTF-8"));
	}

	private int getUid(String result) {
		int uid = 0;
		String beginStr = "\"u_id\":\"";
		String endStr = "\"";
		try {
			IgpWebStatusMsgBean msgBean = Json.fromJson(IgpWebStatusMsgBean.class, result);
			if (msgBean.getCode() == 0) {
				String rslt = msgBean.getRslt();
				int beginIndex = rslt.indexOf(beginStr);
				String substr = rslt.substring(beginIndex + beginStr.length());
				int endIndex = substr.indexOf(endStr);
				uid = Integer.valueOf(substr.substring(0, endIndex));
			}
		} catch (Exception e) {
			if (result.contains(beginStr)) {
				int beginIndex = result.indexOf(beginStr);
				String substr = result.substring(beginIndex + beginStr.length());
				int endIndex = substr.indexOf(endStr);
				uid = Integer.valueOf(substr.substring(0, endIndex));
			}
		}

		return uid;
	}

}
