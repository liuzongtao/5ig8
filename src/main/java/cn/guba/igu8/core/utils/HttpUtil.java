/**
 * 
 */
package cn.guba.igu8.core.utils;

import java.util.Map;

import org.nutz.http.Cookie;
import org.nutz.http.Http;
import org.nutz.http.Request;
import org.nutz.http.Request.METHOD;
import org.nutz.http.Response;
import org.nutz.http.Sender;
import org.nutz.lang.Strings;

/**
 * @author zongtao liu
 *
 */
public class HttpUtil {

	public static Response httpsPost(String url, Map<String, Object> params) {
		return httpsPost(url, params, null);
	}

	public static Response httpsPost(String url, Map<String, Object> params, Cookie cookie) {
		Request req = Request.create(url, METHOD.POST);
		if(params != null){
			req.setParams(params);
		}
		Response response = null;
		try {
			Sender sender = Sender.create(req);
			sender.setSSLSocketFactory(Http.nopSSLSocketFactory());
			if (cookie != null) {
				sender.setInterceptor(cookie);
			}
			response = sender.send();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	
	public static Response get(String url) {
		if(Strings.isBlank(url) || !url.contains("http")){
			return null;
		}
		if(url.startsWith("https")){
			return httpsGet(url);
		}else{
			return Http.get(url);
		}
	}
	

	public static Response httpsGet(String url) {
		return httpsGet(url, null, null);
	}

	public static Response httpsGet(String url, Map<String, Object> params, Cookie cookie) {
		Request req = Request.create(url, METHOD.GET);
		if (params != null) {
			req.setParams(params);
		}
		Response response = null;
		try {
			Sender sender = Sender.create(req);
			sender.setSSLSocketFactory(Http.nopSSLSocketFactory());
			if (cookie != null) {
				sender.setInterceptor(cookie);
			}
			response = sender.send();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

}
