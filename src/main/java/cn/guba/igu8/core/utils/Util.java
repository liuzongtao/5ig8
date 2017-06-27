/**
 * 
 */
package cn.guba.igu8.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zongtao liu
 *
 */
public class Util {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
	
	private static SimpleDateFormat timeSdf = new SimpleDateFormat("HH时mm分");

	public static String dateSecondFormat(long dateSencond) {
		return dateformat(dateSencond * 1000);
	}
	
	public static String timeFormat(long dateSencond) {
		return timeSdf.format(new Date(dateSencond * 1000));
	}
	
	public static String dateformat(long date) {
		return sdf.format(new Date(date));
	}
	
	public static Date parseDate(String dateStr,String formate) throws ParseException {
		SimpleDateFormat mySdf = new SimpleDateFormat(formate);
		return mySdf.parse(dateStr);
	}
}
