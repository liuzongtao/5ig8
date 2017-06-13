/**
 * 
 */
package cn.guba.igu8.core.constants;

/**
 * @author zongtao liu
 *
 */
public class Constant {

	/**
	 * 字符编码格式:UTF-8
	 */
	public static final String CHARSET_UTF8 = "utf-8";

	public static final String KEY_PARA_DES = "B1B2C3D4E5F60708";

	public static final String REDIS_NAME = "5igu8";

	public static final String REDIS_TIER_SEPARATOR = ":";
	
	public static final String EMAIL_NAME = "股⑧";

	/**************************** 爱股票相关 *********************************/
	public static final String URL_IGP_BASE = "https://www.5igupiao.com/";

	public static final String URL_IGP_LOGIN = URL_IGP_BASE + "user/login.php";

	public static final String URL_IGP_USER_STATUS = URL_IGP_BASE + "home/login/status.html?rd=24475.36465432024";

	public static final String URL_IGP_MSG_LIVER = URL_IGP_BASE + "api/liver_msg.php?act=liver_center&source=pc";
//	public static final String URL_IGP_MSG_LIVER = URL_IGP_BASE + "api/liver_msg.php?act=liver_center";
	
	public static final String URL_IGP_MSG_LIVER_DETAIL = URL_IGP_BASE + "api/live.php?act=load_detail&id=%d&oid=%s&soc&source=pc";
	
	
	public static final String URL_5IGU8_DETAIL = "https://47.92.157.16/ccc";
//	public static final String URL_5IGU8_DETAIL = "https://www.5ig8.cn/ccc";
	public static final String URL_5IGU8_LIST = "http://47.92.157.16/cc/list";
	

}
