/**
 * 
 */
package cn.guba.igu8.core.utils;

import java.util.HashMap;
import java.util.Map;

import org.nutz.http.Http;
import org.nutz.json.Json;
import org.nutz.lang.Lang;

/**
 * @author zongtao liu
 *
 */
public class UrlUtil {

	public static String shortUrl(String url) {
		String[] aResult = shortUrls(url);
		if (aResult.length > 0) {
			// 任意一个都可以作为短链接码
			return aResult[0];
		}
		return null;
	}

	private static String[] shortUrls(String url) {
		// 可以自定义生成 MD5 加密字符传前的混合 KEY
		String key = "libotao";
		// 要使用生成 URL 的字符
		String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p",
				"q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
				"B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
				"W", "X", "Y", "Z" };
		// 对传入网址进行 MD5 加密
		String sMD5EncryptResult = (Lang.md5(key + url));
		String hex = sMD5EncryptResult;
		String[] resUrl = new String[4];
		// 得到 4组短链接字符串
		for (int i = 0; i < 4; i++) {
			// 把加密字符按照 8 位一组 16 进制与 0x3FFFFFFF 进行位与运算
			String sTempSubString = hex.substring(i * 8, i * 8 + 8);
			// 这里需要使用 long 型来转换，因为 Inteper .parseInt() 只能处理 31 位 , 首位为符号位 , 如果不用
			// long ，则会越界
			long lHexLong = 0x3FFFFFFF & Long.parseLong(sTempSubString, 16);
			String outChars = "";
			// 循环获得每组6位的字符串
			for (int j = 0; j < 6; j++) {
				// 把得到的值与 0x0000003D 进行位与运算，取得字符数组 chars 索引
				// (具体需要看chars数组的长度 以防下标溢出，注意起点为0)
				long index = 0x0000003D & lHexLong;
				// 把取得的字符相加
				outChars += chars[(int) index];
				// 每次循环按位右移 5 位
				lHexLong = lHexLong >> 5;
			}
			// 把字符串存入对应索引的输出数组
			resUrl[i] = outChars;
		}
		return resUrl;
	}

	/**
	 * 生成短网址字符串（已经去除"http://dwz.cn/"）
	 */
	public static String createShortUrl(String longUrl) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("url", longUrl);
		params.put("access_type", "web");
		String url = DwzURLInfo.URL_CREATE;
		String post = Http.post(url, params, 3000);
		DwzURLInfo fromJson = Json.fromJson(DwzURLInfo.class, post);
		return fromJson.getTinyurl();
	}

	/**
	 * 生成短网址字符串
	 */
	public static String unShortUrl(String shortUrl) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tinyurl", shortUrl);
		params.put("access_type", "web");
		String url = DwzURLInfo.URL_QUERY;
		String longUrl = null;
		try {
			String post = Http.post(url, params, 3000);
			DwzURLInfo fromJson = Json.fromJson(DwzURLInfo.class, post);
			longUrl = fromJson.getLongurl();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return longUrl;
	}

}
