/**
 * 
 */
package cn.guba.igu8.db.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.http.Response;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

import cn.guba.igu8.core.constants.Constant;
import cn.guba.igu8.core.utils.HttpUtil;
import cn.guba.igu8.core.utils.Util;
import cn.guba.igu8.db.mysqlModel.Igpcontent;
import cn.guba.igu8.db.mysqlModel.Teacher;
import cn.guba.igu8.processor.igupiaoWeb.msg.beans.EIgpKind;
import cn.guba.igu8.processor.igupiaoWeb.msg.beans.IgpDetailBean;
import cn.guba.igu8.processor.igupiaoWeb.msg.beans.IgpDetailMsgBean;
import cn.guba.igu8.processor.igupiaoWeb.msg.beans.IgpWebMsgBean;

/**
 * @author zongtao liu
 *
 */
public class IgpcontentDao {

	static Log log = Logs.get();

	private static Map<String, Igpcontent> contentMap = new HashMap<String, Igpcontent>();

	public static Igpcontent getNewestIgpcontent(long teacherId) {
		return Igpcontent.dao
				.findFirst("select * from igpcontent where teacherId=" + teacherId + " order by id desc limit 1");
	}

	public static long getMaxId(long teacherId) {
		Long queryLong = Db.queryLong("select ifnull(max(id),0) from igpcontent where teacherId=" + teacherId);
		return queryLong;
	}

	public static Igpcontent getIgpcontent(long teacherId, long id) {
		Igpcontent content = null;
		String key = getMapKey(teacherId, id);
		if (contentMap.containsKey(key)) {
			content = contentMap.get(key);
		} else {
			content = Igpcontent.dao
					.findFirst("select * from igpcontent where teacherId=" + teacherId + " and id=" + id + " limit 1");
			if (content != null) {
				contentMap.put(key, content);
			}
		}
		// 清除超时的信息
		clearMap();
		return content;
	}

	public static Igpcontent getIgpcontent4Detail(long teacherId, long id) {
		Igpcontent content = null;
		String key = getMapKey(teacherId, id);
		if (contentMap.containsKey(key)) {
			content = contentMap.get(key);
		} else {
			content = Igpcontent.dao
					.findFirst("select * from igpcontent where teacherId=" + teacherId + " and id=" + id + " limit 1");
			if (content != null) {
				long now = System.currentTimeMillis() / 1000;
				Long recTime = content.getRecTime();
				// 48小时前的信息需要清除
				if (now <= (recTime + 60 * 60 * 48)) {
					contentMap.put(key, content);
				}
			}
		}
		// 清除超时的信息
		clearMap();
		return content;
	}

	/***
	 * 清除超时的信息
	 */
	private static void clearMap() {
		long now = System.currentTimeMillis() / 1000;
		for (Igpcontent content : contentMap.values()) {
			Long recTime = content.getRecTime();
			// 48小时前的信息需要清除
			if (now > (recTime + 60 * 60 * 48)) {
				String key = getMapKey(content.getTeacherId(), content.getId());
				contentMap.remove(key);
			}
		}
	}

	private static String getMapKey(long teacherId, long id) {
		return String.valueOf(id) + "@" + String.valueOf(teacherId);
	}

	/***
	 * 批量插入记录
	 * 
	 * @param teacherId
	 * @param list
	 */
	public static void batchInserMsg(long teacherId, List<IgpWebMsgBean> list) {
		List<Igpcontent> contentList = new ArrayList<Igpcontent>();
		for (IgpWebMsgBean msg : list) {
			Igpcontent content = getIgpcontent(teacherId, msg);
			log.debug(Json.toJson(content, JsonFormat.compact()));
			contentList.add(content);
		}
		Db.batchSave(contentList, contentList.size());
	}

	/***
	 * 插入消息
	 * 
	 * @param teacherId
	 * @param msg
	 * @return
	 */
	public static long insertMsg(long teacherId, IgpWebMsgBean msg) {
		Igpcontent content = getIgpcontent(teacherId, msg);
		content.save();
		return content.getLid();
	}

	private static String getChargeContent(int id, String oid) {
		String detail = "";
		String chargeUrl = String.format(Constant.URL_IGP_MSG_LIVER_DETAIL, id,
				oid);
		Response response = HttpUtil.get(chargeUrl);
		if (response != null) {
			String content = response.getContent();
			IgpDetailMsgBean detailMsg = Json.fromJson(IgpDetailMsgBean.class, content);
			IgpDetailBean[] show_detail = detailMsg.getShow_detail();
			if (show_detail.length > 0) {
				IgpDetailBean igpDetailBean = show_detail[0];
				detail = igpDetailBean.getContent();
			}
		}
		return detail;
	}

	/***
	 * 获取数据库信息
	 * 
	 * @param teacherId
	 * @param msg
	 * @return
	 */
	private static Igpcontent getIgpcontent(long teacherId, IgpWebMsgBean msg) {
		Igpcontent content = new Igpcontent();
		content.setTeacherId(teacherId);
		content.setId(Long.valueOf(msg.getId()));
		content.setKind(msg.getKind());
		if (msg.getKind().equals(EIgpKind.CHARGE.getValue())) {
			Teacher teacher = TeacherDao.getTeacher(teacherId);
			String detail = getChargeContent(teacher.getPfId(), msg.getId());
			msg.setContent(detail);
		}
		content.setTitle(msg.getTitle());
		content.setBrief(msg.getBrief().replaceAll("[\\x{10000}-\\x{10FFFF}]", ""));
		content.setPrice(msg.getPrice());
		content.setVipPrice(msg.getVip_price());
		content.setContent(msg.getContent().replaceAll("[\\x{10000}-\\x{10FFFF}]", ""));
		content.setImageThumb(Json.toJson(msg.getImage_thumb(), JsonFormat.compact()));
		content.setImage(Json.toJson(msg.getImage(), JsonFormat.compact()));
		content.setImageSize(Json.toJson(msg.getImage_size(), JsonFormat.compact()));
		content.setAudio(msg.getAudio());
		content.setAudioLen(msg.getAudio_len());
		content.setRecTime(msg.getRec_time());
		content.setRecTimeDesc(Util.dateSecondFormat(msg.getRec_time()));
		content.setUrl(msg.getUrl());
		content.setState2(msg.getState2());
		content.setViewSelf(msg.isView_self());
		content.setVipShow(msg.isVip_show());
		content.setNotVipShow(msg.isNot_vip_show());
		content.setUnshowGroup(msg.getUnshow_group());
		content.setVipGroupInfo(msg.getVip_group_info());
		content.setOId(msg.getO_id());
		content.setOKind(msg.getO_kind());
		content.setZhId(msg.getZh_id());
		content.setBId(msg.getB_id());
		content.setGId(msg.getG_id());
		content.setMenusId(msg.getMenus_id());
		content.setVId(msg.getV_id());
		content.setTouid(msg.getTouid());
		content.setStockInfo(Json.toJson(msg.getStock_info(), JsonFormat.compact()));
		content.setThunimg(msg.getThunimg());
		content.setOCId(msg.getO_c_id());
		content.setLabelKind(msg.getLabel_kind());
		content.setMenus(Json.toJson(msg.getMenus(), JsonFormat.compact()));
		content.setForwardInfo(Json.toJson(msg.getForward_info(), JsonFormat.compact()));
		content.setDoForward(msg.isDo_forward() ? 1 : 0);
		content.setToInfo(Json.toJson(msg.getTo_info(), JsonFormat.compact()));
		content.setCommentNum(msg.getComment_num());
		content.setSupportNum(msg.getSupport_num());
		content.setRewardNum(msg.getReward_num());
		content.setRewardList(Json.toJson(msg.getReward_list(), JsonFormat.compact()));
		content.setNumber(msg.getNumber());
		content.setContentNew(msg.getContent_new().replaceAll("[\\x{10000}-\\x{10FFFF}]", ""));
		return content;
	}

	public static Page<Igpcontent> paginateByTeacher(long teacherId, int pageNumber, int pageSize) {
		return Igpcontent.dao.paginate(pageNumber, pageSize, "select *",
				"from igpcontent where teacherId= " + teacherId + " order by id desc");
	}

	public static Page<Igpcontent> paginateByTeacher4Vip(long teacherId, int pageNumber, int pageSize) {
		return Igpcontent.dao.paginate(pageNumber, pageSize, "select *", "from igpcontent where kind='"
				+ EIgpKind.VIP.getValue() + "' and teacherId=" + teacherId + " order by id desc");
	}

}
