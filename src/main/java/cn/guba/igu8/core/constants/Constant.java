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
	public static final String URL_IGP_BASE = "https://www.aigupiao.com/";

	public static final String URL_IGP_LOGIN = URL_IGP_BASE + "user/login.php";

	public static final String URL_IGP_USER_STATUS = URL_IGP_BASE + "home/login/status.html?rd=24475.36465432024";

	public static final String URL_IGP_MSG_LIVER = URL_IGP_BASE + "api/liver_msg.php?act=liver_center&source=pc";
//	public static final String URL_IGP_MSG_LIVER = URL_IGP_BASE + "api/liver_msg.php?act=liver_center";
	
	public static final String URL_IGP_MSG_LIVER_DETAIL = URL_IGP_BASE + "api/live.php?act=load_detail&id=%d&oid=%s&soc&source=pc";
	
	
	
	public static final String URL_5IGU8_BASE = "http://www.5igu8.cn";
//	public static final String URL_5IGU8_BASE = "http://47.92.157.16";
	public static final String URL_5IGU8_DETAIL = URL_5IGU8_BASE + "/ccc";
	public static final String URL_5IGU8_LIST = URL_5IGU8_BASE + "/cc/viplist";
	
	public static final String USER_PWD_INIT = "5Igu8@2017";
	
	public static final String MAIL_TEMP_UPDATE_SUBJECT = EMAIL_NAME + ":【通知】会员信息更新";
	public static final String MAIL_TEMP_UPDATE = "尊敬的会员：<br /> &nbsp;&nbsp;您关注的会员老师：%s，会员结束时间为%s。<br /> &nbsp;&nbsp;如有疑问请联系客服人员。";
	public static final String MAIL_TEMP_END = "尊敬的会员：<br /> &nbsp;&nbsp;您关注的会员老师：%s，会员服务将于%s结束。如果想继续使用，请及时续费！<br /> &nbsp;&nbsp;如有疑问请联系客服人员。";
	public static final String MAIL_TEMP_END_SUBJECT = EMAIL_NAME + ":【通知】会员信息即将结束";
	
	
	

}
