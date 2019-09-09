package cn.guba.igu8.db.mysqlModel.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseEovaMenu<M extends BaseEovaMenu<M>> extends Model<M> implements IBean {

	public M setId(java.lang.Integer id) {
		set("id", id);
		return (M)this;
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public M setCode(java.lang.String code) {
		set("code", code);
		return (M)this;
	}

	public java.lang.String getCode() {
		return get("code");
	}

	public M setName(java.lang.String name) {
		set("name", name);
		return (M)this;
	}

	public java.lang.String getName() {
		return get("name");
	}

	public M setType(java.lang.String type) {
		set("type", type);
		return (M)this;
	}

	public java.lang.String getType() {
		return get("type");
	}

	public M setIcon(java.lang.String icon) {
		set("icon", icon);
		return (M)this;
	}

	public java.lang.String getIcon() {
		return get("icon");
	}

	public M setOrderNum(java.lang.Integer orderNum) {
		set("order_num", orderNum);
		return (M)this;
	}

	public java.lang.Integer getOrderNum() {
		return get("order_num");
	}

	public M setParentId(java.lang.Integer parentId) {
		set("parent_id", parentId);
		return (M)this;
	}

	public java.lang.Integer getParentId() {
		return get("parent_id");
	}

	public M setIsCollapse(java.lang.Boolean isCollapse) {
		set("is_collapse", isCollapse);
		return (M)this;
	}

	public java.lang.Boolean getIsCollapse() {
		return get("is_collapse");
	}

	public M setBizIntercept(java.lang.String bizIntercept) {
		set("biz_intercept", bizIntercept);
		return (M)this;
	}

	public java.lang.String getBizIntercept() {
		return get("biz_intercept");
	}

	public M setUrl(java.lang.String url) {
		set("url", url);
		return (M)this;
	}

	public java.lang.String getUrl() {
		return get("url");
	}

	public M setMenuConfig(java.lang.String menuConfig) {
		set("menuConfig", menuConfig);
		return (M)this;
	}

	public java.lang.String getMenuConfig() {
		return get("menuConfig");
	}

	public M setDiyJs(java.lang.String diyJs) {
		set("diy_js", diyJs);
		return (M)this;
	}

	public java.lang.String getDiyJs() {
		return get("diy_js");
	}

	public M setIsDel(java.lang.Boolean isDel) {
		set("is_del", isDel);
		return (M)this;
	}

	public java.lang.Boolean getIsDel() {
		return get("is_del");
	}

	public M setFilter(java.lang.String filter) {
		set("filter", filter);
		return (M)this;
	}

	public java.lang.String getFilter() {
		return get("filter");
	}

}
