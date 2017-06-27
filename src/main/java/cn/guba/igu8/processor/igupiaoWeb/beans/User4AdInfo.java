/**
 * 
 */
package cn.guba.igu8.processor.igupiaoWeb.beans;

import java.text.ParseException;
import java.util.Date;

import org.nutz.lang.Strings;

import cn.guba.igu8.core.utils.Util;

/**
 * @author zongtao liu
 *
 */
public class User4AdInfo {

	private static final String FORMATE = "yyyy/MM/dd HH:mm:ss";
	private static final String FORMATE2 = "yyyy/MM/dd";

//	private String name;

	private String email;

	private String endTimeStr;

	private long endTime;

	private long teacherId;

	private String kinds;

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the endTime
	 */
	public long getEndTime() {
		try {
			if(Strings.isBlank(endTimeStr)){
				return 0;
			}
			Date parseDate = null;
			if(endTimeStr.contains(" ")){
				parseDate = Util.parseDate(endTimeStr, FORMATE);
			}else{
				parseDate = Util.parseDate(endTimeStr, FORMATE2);
			}
			endTime = parseDate.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the teacherId
	 */
	public long getTeacherId() {
		return teacherId;
	}

	/**
	 * @param teacherId
	 *            the teacherId to set
	 */
	public void setTeacherId(long teacherId) {
		this.teacherId = teacherId;
	}

	/**
	 * @return the kinds
	 */
	public String getKinds() {
		return kinds;
	}

	/**
	 * @param kinds
	 *            the kinds to set
	 */
	public void setKinds(String kinds) {
		this.kinds = kinds;
	}

	/**
	 * @return the endTimeStr
	 */
	public String getEndTimeStr() {
		return endTimeStr;
	}

	/**
	 * @param endTimeStr
	 *            the endTimeStr to set
	 */
	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

//	/**
//	 * @return the name
//	 */
//	public String getName() {
//		return name;
//	}
//
//	/**
//	 * @param name
//	 *            the name to set
//	 */
//	public void setName(String name) {
//		this.name = name;
//	}

}
