/**
 * 
 */
package cn.guba.igu8.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
	
	public static boolean isSameDay(long date1,long date2){
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(date1);
		long day1 = getDay(instance);
		instance.setTimeInMillis(date2);
		long day2 = getDay(instance);
		return day1 == day2;
	}
	
	private static long getDay(Calendar instance){
		int year = instance.get(Calendar.YEAR);
		int month = instance.get(Calendar.MONTH) + 1;
		int day = instance.get(Calendar.DAY_OF_MONTH);
		return year * 10000 + month * 100 + day;
	}
	
	public static long getSurplusTime(int hour){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		long now = System.currentTimeMillis();
		if(cal.getTimeInMillis() < now){
			cal.add(Calendar.DATE, 1);
		}
		return cal.getTimeInMillis() - now;
	}
	
	public static void main(String[] args) {
		System.out.println(getSurplusTime(8));
	}
}
