/**
 * 
 */
package cn.guba.igu8.web.log.beans;

/**
 * @author zongtao liu
 *
 */
public class RechargeLogViewBean {

	private long id;

	private long uid;

	private String nickname;

	private String inviterNickname;

	private String createTimeStr;

	private String phoneNumber;

	private String email;

	private String timeStr;

	private String teacherName;

	private String emailTypes;

	private String vipEndTimeStr;

	private int costMoney;

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
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * @param nickname
	 *            the nickname to set
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * @return the inviterNickname
	 */
	public String getInviterNickname() {
		return inviterNickname;
	}

	/**
	 * @param inviterNickname
	 *            the inviterNickname to set
	 */
	public void setInviterNickname(String inviterNickname) {
		this.inviterNickname = inviterNickname;
	}

	/**
	 * @return the createTimeStr
	 */
	public String getCreateTimeStr() {
		return createTimeStr;
	}

	/**
	 * @param createTimeStr
	 *            the createTimeStr to set
	 */
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber
	 *            the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

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
	 * @return the timeStr
	 */
	public String getTimeStr() {
		return timeStr;
	}

	/**
	 * @param timeStr
	 *            the timeStr to set
	 */
	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
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
	 * @return the emailTypes
	 */
	public String getEmailTypes() {
		return emailTypes;
	}

	/**
	 * @param emailTypes
	 *            the emailTypes to set
	 */
	public void setEmailTypes(String emailTypes) {
		this.emailTypes = emailTypes;
	}

	/**
	 * @return the vipEndTimeStr
	 */
	public String getVipEndTimeStr() {
		return vipEndTimeStr;
	}

	/**
	 * @param vipEndTimeStr
	 *            the vipEndTimeStr to set
	 */
	public void setVipEndTimeStr(String vipEndTimeStr) {
		this.vipEndTimeStr = vipEndTimeStr;
	}

	/**
	 * @return the costMoney
	 */
	public int getCostMoney() {
		return costMoney;
	}

	/**
	 * @param costMoney
	 *            the costMoney to set
	 */
	public void setCostMoney(int costMoney) {
		this.costMoney = costMoney;
	}

}
