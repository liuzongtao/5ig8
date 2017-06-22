/**
 * 
 */
package cn.guba.igu8.web.vip.beans;

/**
 * @author zongtao liu
 *
 */
public class UserVipViewBean {

	public long id;
	public long uid;
	public String vipPf;
	public int vipPfType;
	public String endTime;
	public int periodNum = 1;
	public int periodType;
	public String teacherName;
	public long teacherId;
	public String emailType;
	public int isSendSms;
	public int isShowUrl;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the uid
	 */
	public long getUid() {
		return uid;
	}

	/**
	 * @param uid
	 *            the uid to set
	 */
	public void setUid(long uid) {
		this.uid = uid;
	}

	/**
	 * @return the vipPf
	 */
	public String getVipPf() {
		return vipPf;
	}

	/**
	 * @param vipPf
	 *            the vipPf to set
	 */
	public void setVipPf(String vipPf) {
		this.vipPf = vipPf;
	}

	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the teacherName
	 */
	public String getTeacherName() {
		return teacherName;
	}

	/**
	 * @param teacherName
	 *            the teacherName to set
	 */
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	/**
	 * @return the emailType
	 */
	public String getEmailType() {
		return emailType;
	}

	/**
	 * @param emailType
	 *            the emailType to set
	 */
	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}

	/**
	 * @return the isSendSms
	 */
	public int getIsSendSms() {
		return isSendSms;
	}

	/**
	 * @param isSendSms
	 *            the isSendSms to set
	 */
	public void setIsSendSms(int isSendSms) {
		this.isSendSms = isSendSms;
	}

	/**
	 * @return the isShowUrl
	 */
	public int getIsShowUrl() {
		return isShowUrl;
	}

	/**
	 * @param isShowUrl
	 *            the isShowUrl to set
	 */
	public void setIsShowUrl(int isShowUrl) {
		this.isShowUrl = isShowUrl;
	}

	/**
	 * @return the vipPfType
	 */
	public int getVipPfType() {
		return vipPfType;
	}

	/**
	 * @param vipPfType
	 *            the vipPfType to set
	 */
	public void setVipPfType(int vipPfType) {
		this.vipPfType = vipPfType;
	}

	/**
	 * @return the periodNum
	 */
	public int getPeriodNum() {
		return periodNum;
	}

	/**
	 * @param periodNum
	 *            the periodNum to set
	 */
	public void setPeriodNum(int periodNum) {
		this.periodNum = periodNum;
	}

	/**
	 * @return the periodType
	 */
	public int getPeriodType() {
		return periodType;
	}

	/**
	 * @param periodType
	 *            the periodType to set
	 */
	public void setPeriodType(int periodType) {
		this.periodType = periodType;
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

}
