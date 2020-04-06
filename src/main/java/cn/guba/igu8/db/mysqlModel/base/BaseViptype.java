package cn.guba.igu8.db.mysqlModel.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseViptype<M extends BaseViptype<M>> extends Model<M> implements IBean {

	public M setId(java.lang.Integer id) {
		set("id", id);
		return (M)this;
	}
	
	public java.lang.Integer getId() {
		return getInt("id");
	}

	public M setDescr(java.lang.String descr) {
		set("descr", descr);
		return (M)this;
	}
	
	public java.lang.String getDescr() {
		return getStr("descr");
	}

	public M setCostMoney(java.lang.Integer costMoney) {
		set("costMoney", costMoney);
		return (M)this;
	}
	
	public java.lang.Integer getCostMoney() {
		return getInt("costMoney");
	}

}
