/**
 * 
 */
package cn.guba.igu8.web.user.beans;

/**
 * @author zongtao liu
 *
 */
public class UserViewInfoBean {

	public long id;

	public String nickname;

	public String createTime;

	public String phoneNumber;

	public String email;

	public String inviterNickname;
	
	public String inviter;
	
	public String vipInfo;
	
	public int roleType;

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
	 * @return the createTime
	 */
	public String getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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
	 * @return the inviterNickname
	 */
	public String getInviterNickname() {
		return inviterNickname;
	}

	/**
	 * @param inviterNickname the inviterNickname to set
	 */
	public void setInviterNickname(String inviterNickname) {
		this.inviterNickname = inviterNickname;
	}

	/**
	 * @return the vipInfo
	 */
	public String getVipInfo() {
		return vipInfo;
	}

	/**
	 * @param vipInfo the vipInfo to set
	 */
	public void setVipInfo(String vipInfo) {
		this.vipInfo = vipInfo;
	}

	/**
	 * @return the inviter
	 */
	public String getInviter() {
		return inviter;
	}

	/**
	 * @param inviter the inviter to set
	 */
	public void setInviter(String inviter) {
		this.inviter = inviter;
	}

	/**
	 * @return the roleType
	 */
	public int getRoleType() {
		return roleType;
	}

	/**
	 * @param roleType the roleType to set
	 */
	public void setRoleType(int roleType) {
		this.roleType = roleType;
	}
}
