package cn.guba.igu8.db.mysqlModel.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseIgpcontent<M extends BaseIgpcontent<M>> extends Model<M> implements IBean {

	public M setLid(java.lang.Long lid) {
		set("lid", lid);
		return (M)this;
	}
	
	public java.lang.Long getLid() {
		return getLong("lid");
	}

	public M setTeacherId(java.lang.Long teacherId) {
		set("teacherId", teacherId);
		return (M)this;
	}
	
	public java.lang.Long getTeacherId() {
		return getLong("teacherId");
	}

	public M setId(java.lang.Long id) {
		set("id", id);
		return (M)this;
	}
	
	public java.lang.Long getId() {
		return getLong("id");
	}

	public M setKind(java.lang.String kind) {
		set("kind", kind);
		return (M)this;
	}
	
	public java.lang.String getKind() {
		return getStr("kind");
	}

	public M setTitle(java.lang.String title) {
		set("title", title);
		return (M)this;
	}
	
	public java.lang.String getTitle() {
		return getStr("title");
	}

	public M setBrief(java.lang.String brief) {
		set("brief", brief);
		return (M)this;
	}
	
	public java.lang.String getBrief() {
		return getStr("brief");
	}

	public M setPrice(java.lang.Float price) {
		set("price", price);
		return (M)this;
	}
	
	public java.lang.Float getPrice() {
		return getFloat("price");
	}

	public M setVipPrice(java.lang.Float vipPrice) {
		set("vip_price", vipPrice);
		return (M)this;
	}
	
	public java.lang.Float getVipPrice() {
		return getFloat("vip_price");
	}

	public M setContent(java.lang.String content) {
		set("content", content);
		return (M)this;
	}
	
	public java.lang.String getContent() {
		return getStr("content");
	}

	public M setImageThumb(java.lang.String imageThumb) {
		set("image_thumb", imageThumb);
		return (M)this;
	}
	
	public java.lang.String getImageThumb() {
		return getStr("image_thumb");
	}

	public M setImage(java.lang.String image) {
		set("image", image);
		return (M)this;
	}
	
	public java.lang.String getImage() {
		return getStr("image");
	}

	public M setImageSize(java.lang.String imageSize) {
		set("image_size", imageSize);
		return (M)this;
	}
	
	public java.lang.String getImageSize() {
		return getStr("image_size");
	}

	public M setAudio(java.lang.String audio) {
		set("audio", audio);
		return (M)this;
	}
	
	public java.lang.String getAudio() {
		return getStr("audio");
	}

	public M setAudioLen(java.lang.Integer audioLen) {
		set("audio_len", audioLen);
		return (M)this;
	}
	
	public java.lang.Integer getAudioLen() {
		return getInt("audio_len");
	}

	public M setRecTime(java.lang.Long recTime) {
		set("rec_time", recTime);
		return (M)this;
	}
	
	public java.lang.Long getRecTime() {
		return getLong("rec_time");
	}

	public M setRecTimeDesc(java.lang.String recTimeDesc) {
		set("rec_time_desc", recTimeDesc);
		return (M)this;
	}
	
	public java.lang.String getRecTimeDesc() {
		return getStr("rec_time_desc");
	}

	public M setUrl(java.lang.String url) {
		set("url", url);
		return (M)this;
	}
	
	public java.lang.String getUrl() {
		return getStr("url");
	}

	public M setState2(java.lang.String state2) {
		set("state2", state2);
		return (M)this;
	}
	
	public java.lang.String getState2() {
		return getStr("state2");
	}

	public M setViewSelf(java.lang.Boolean viewSelf) {
		set("view_self", viewSelf);
		return (M)this;
	}
	
	public java.lang.Boolean getViewSelf() {
		return get("view_self");
	}

	public M setVipShow(java.lang.Boolean vipShow) {
		set("vip_show", vipShow);
		return (M)this;
	}
	
	public java.lang.Boolean getVipShow() {
		return get("vip_show");
	}

	public M setNotVipShow(java.lang.Boolean notVipShow) {
		set("not_vip_show", notVipShow);
		return (M)this;
	}
	
	public java.lang.Boolean getNotVipShow() {
		return get("not_vip_show");
	}

	public M setUnshowGroup(java.lang.String unshowGroup) {
		set("unshow_group", unshowGroup);
		return (M)this;
	}
	
	public java.lang.String getUnshowGroup() {
		return getStr("unshow_group");
	}

	public M setVipGroupInfo(java.lang.String vipGroupInfo) {
		set("vip_group_info", vipGroupInfo);
		return (M)this;
	}
	
	public java.lang.String getVipGroupInfo() {
		return getStr("vip_group_info");
	}

	public M setOId(java.lang.String oId) {
		set("o_id", oId);
		return (M)this;
	}
	
	public java.lang.String getOId() {
		return getStr("o_id");
	}

	public M setOKind(java.lang.String oKind) {
		set("o_kind", oKind);
		return (M)this;
	}
	
	public java.lang.String getOKind() {
		return getStr("o_kind");
	}

	public M setZhId(java.lang.String zhId) {
		set("zh_id", zhId);
		return (M)this;
	}
	
	public java.lang.String getZhId() {
		return getStr("zh_id");
	}

	public M setBId(java.lang.String bId) {
		set("b_id", bId);
		return (M)this;
	}
	
	public java.lang.String getBId() {
		return getStr("b_id");
	}

	public M setGId(java.lang.String gId) {
		set("g_id", gId);
		return (M)this;
	}
	
	public java.lang.String getGId() {
		return getStr("g_id");
	}

	public M setMenusId(java.lang.String menusId) {
		set("menus_id", menusId);
		return (M)this;
	}
	
	public java.lang.String getMenusId() {
		return getStr("menus_id");
	}

	public M setVId(java.lang.String vId) {
		set("v_id", vId);
		return (M)this;
	}
	
	public java.lang.String getVId() {
		return getStr("v_id");
	}

	public M setTouid(java.lang.String touid) {
		set("touid", touid);
		return (M)this;
	}
	
	public java.lang.String getTouid() {
		return getStr("touid");
	}

	public M setStockInfo(java.lang.String stockInfo) {
		set("stock_info", stockInfo);
		return (M)this;
	}
	
	public java.lang.String getStockInfo() {
		return getStr("stock_info");
	}

	public M setThunimg(java.lang.String thunimg) {
		set("thunimg", thunimg);
		return (M)this;
	}
	
	public java.lang.String getThunimg() {
		return getStr("thunimg");
	}

	public M setOCId(java.lang.String oCId) {
		set("o_c_id", oCId);
		return (M)this;
	}
	
	public java.lang.String getOCId() {
		return getStr("o_c_id");
	}

	public M setLabelKind(java.lang.String labelKind) {
		set("label_kind", labelKind);
		return (M)this;
	}
	
	public java.lang.String getLabelKind() {
		return getStr("label_kind");
	}

	public M setMenus(java.lang.String menus) {
		set("menus", menus);
		return (M)this;
	}
	
	public java.lang.String getMenus() {
		return getStr("menus");
	}

	public M setForwardInfo(java.lang.String forwardInfo) {
		set("forward_info", forwardInfo);
		return (M)this;
	}
	
	public java.lang.String getForwardInfo() {
		return getStr("forward_info");
	}

	public M setDoForward(java.lang.Integer doForward) {
		set("do_forward", doForward);
		return (M)this;
	}
	
	public java.lang.Integer getDoForward() {
		return getInt("do_forward");
	}

	public M setToInfo(java.lang.String toInfo) {
		set("to_info", toInfo);
		return (M)this;
	}
	
	public java.lang.String getToInfo() {
		return getStr("to_info");
	}

	public M setCommentNum(java.lang.Integer commentNum) {
		set("comment_num", commentNum);
		return (M)this;
	}
	
	public java.lang.Integer getCommentNum() {
		return getInt("comment_num");
	}

	public M setSupportNum(java.lang.Integer supportNum) {
		set("support_num", supportNum);
		return (M)this;
	}
	
	public java.lang.Integer getSupportNum() {
		return getInt("support_num");
	}

	public M setRewardNum(java.lang.Integer rewardNum) {
		set("reward_num", rewardNum);
		return (M)this;
	}
	
	public java.lang.Integer getRewardNum() {
		return getInt("reward_num");
	}

	public M setRewardList(java.lang.String rewardList) {
		set("reward_list", rewardList);
		return (M)this;
	}
	
	public java.lang.String getRewardList() {
		return getStr("reward_list");
	}

	public M setNumber(java.lang.Integer number) {
		set("number", number);
		return (M)this;
	}
	
	public java.lang.Integer getNumber() {
		return getInt("number");
	}

	public M setContentNew(java.lang.String contentNew) {
		set("content_new", contentNew);
		return (M)this;
	}
	
	public java.lang.String getContentNew() {
		return getStr("content_new");
	}

}
