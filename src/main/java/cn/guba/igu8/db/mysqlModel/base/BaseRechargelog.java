package cn.guba.igu8.db.mysqlModel.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseRechargelog<M extends BaseRechargelog<M>> extends Model<M> implements IBean {

	public M setId(java.lang.Long id) {
		set("id", id);
		return (M)this;
	}
	
	public java.lang.Long getId() {
		return getLong("id");
	}

	public M setUid(java.lang.Long uid) {
		set("uid", uid);
		return (M)this;
	}
	
	public java.lang.Long getUid() {
		return getLong("uid");
	}

	public M setNickname(java.lang.String nickname) {
		set("nickname", nickname);
		return (M)this;
	}
	
	public java.lang.String getNickname() {
		return getStr("nickname");
	}

	public M setInviterUid(java.lang.Long inviterUid) {
		set("inviterUid", inviterUid);
		return (M)this;
	}
	
	public java.lang.Long getInviterUid() {
		return getLong("inviterUid");
	}

	public M setInviterNickname(java.lang.String inviterNickname) {
		set("inviterNickname", inviterNickname);
		return (M)this;
	}
	
	public java.lang.String getInviterNickname() {
		return getStr("inviterNickname");
	}

	public M setCreatTime(java.lang.Long creatTime) {
		set("creatTime", creatTime);
		return (M)this;
	}
	
	public java.lang.Long getCreatTime() {
		return getLong("creatTime");
	}

	public M setEmail(java.lang.String email) {
		set("email", email);
		return (M)this;
	}
	
	public java.lang.String getEmail() {
		return getStr("email");
	}

	public M setPhoneNumber(java.lang.Long phoneNumber) {
		set("phoneNumber", phoneNumber);
		return (M)this;
	}
	
	public java.lang.Long getPhoneNumber() {
		return getLong("phoneNumber");
	}

	public M setCurTime(java.lang.Long curTime) {
		set("curTime", curTime);
		return (M)this;
	}
	
	public java.lang.Long getCurTime() {
		return getLong("curTime");
	}

	public M setOldVipEndTime(java.lang.Long oldVipEndTime) {
		set("oldVipEndTime", oldVipEndTime);
		return (M)this;
	}
	
	public java.lang.Long getOldVipEndTime() {
		return getLong("oldVipEndTime");
	}

	public M setNewVipEndTime(java.lang.Long newVipEndTime) {
		set("newVipEndTime", newVipEndTime);
		return (M)this;
	}
	
	public java.lang.Long getNewVipEndTime() {
		return getLong("newVipEndTime");
	}

	public M setConcernedTeacherId(java.lang.Long concernedTeacherId) {
		set("concernedTeacherId", concernedTeacherId);
		return (M)this;
	}
	
	public java.lang.Long getConcernedTeacherId() {
		return getLong("concernedTeacherId");
	}

	public M setEmailTypes(java.lang.String emailTypes) {
		set("emailTypes", emailTypes);
		return (M)this;
	}
	
	public java.lang.String getEmailTypes() {
		return getStr("emailTypes");
	}

	public M setIsSendSms(java.lang.Boolean isSendSms) {
		set("isSendSms", isSendSms);
		return (M)this;
	}
	
	public java.lang.Boolean getIsSendSms() {
		return get("isSendSms");
	}

	public M setIsShowUrl(java.lang.Boolean isShowUrl) {
		set("isShowUrl", isShowUrl);
		return (M)this;
	}
	
	public java.lang.Boolean getIsShowUrl() {
		return get("isShowUrl");
	}

	public M setDisCountInfosStr(java.lang.String disCountInfosStr) {
		set("disCountInfosStr", disCountInfosStr);
		return (M)this;
	}
	
	public java.lang.String getDisCountInfosStr() {
		return getStr("disCountInfosStr");
	}

	public M setVipTypeId(java.lang.Long vipTypeId) {
		set("vipTypeId", vipTypeId);
		return (M)this;
	}
	
	public java.lang.Long getVipTypeId() {
		return getLong("vipTypeId");
	}

	public M setVipTypeDescr(java.lang.String vipTypeDescr) {
		set("vipTypeDescr", vipTypeDescr);
		return (M)this;
	}
	
	public java.lang.String getVipTypeDescr() {
		return getStr("vipTypeDescr");
	}

	public M setCostMoney(java.lang.Integer costMoney) {
		set("costMoney", costMoney);
		return (M)this;
	}
	
	public java.lang.Integer getCostMoney() {
		return getInt("costMoney");
	}

}
