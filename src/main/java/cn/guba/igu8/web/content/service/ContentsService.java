/**
 * 
 */
package cn.guba.igu8.web.content.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.nutz.json.Json;
import org.nutz.lang.Strings;

import com.jfinal.plugin.activerecord.Page;

import cn.guba.igu8.db.dao.IgpcontentDao;
import cn.guba.igu8.db.dao.TeacherDao;
import cn.guba.igu8.db.mysqlModel.Igpcontent;
import cn.guba.igu8.db.mysqlModel.Teacher;
import cn.guba.igu8.processor.igupiaoWeb.msg.beans.EIgpKind;
import cn.guba.igu8.processor.igupiaoWeb.msg.beans.IgpStockBean;
import cn.guba.igu8.processor.igupiaoWeb.service.IgpMsgService;
import cn.guba.igu8.web.content.beans.IgpWebDetailBean;
import cn.guba.igu8.web.content.beans.KindViewBean;

/**
 * @author zongtao liu
 *
 */
public class ContentsService {

	private static volatile ContentsService contentsService;

	private ContentsService() {
	}

	public static ContentsService getInstance() {
		if (contentsService == null) {
			synchronized (ContentsService.class) {
				if (contentsService == null) {
					contentsService = new ContentsService();
				}
			}
		}
		return contentsService;
	}

	/***
	 * 获取详细信息
	 * 
	 * @param teacherId
	 * @return
	 */
	public IgpWebDetailBean getNewestDetail(long teacherId) {
		Igpcontent igpcontent = IgpcontentDao.getNewestIgpcontent(teacherId);
		IgpWebDetailBean detail = getIgpWebDetailBean(teacherId, igpcontent);
		return detail;
	}

	/***
	 * 获取分页的内容
	 * 
	 * @param teacherId
	 * @param pageNumber
	 * @return
	 */
	public Page<IgpWebDetailBean> getDetailPageList(long teacherId, int pageNumber) {
		int pageSize = 6;
		Page<Igpcontent> igpcontents = IgpcontentDao.paginateByTeacher(teacherId, pageNumber, pageSize);
		List<IgpWebDetailBean> detailList = new ArrayList<IgpWebDetailBean>();
		for (Igpcontent igpcontent : igpcontents.getList()) {
			IgpWebDetailBean tmpIgpWebDetailBean = getIgpWebDetailBean(teacherId, igpcontent);
			detailList.add(tmpIgpWebDetailBean);
		}
		return new Page<IgpWebDetailBean>(detailList, igpcontents.getPageNumber(), igpcontents.getPageSize(),
				igpcontents.getTotalPage(), igpcontents.getTotalRow());
	}

	/***
	 * 为获取所有的vip信息
	 * 
	 * @param teacherId
	 * @param pageNumber
	 * @return
	 */
	public Page<IgpWebDetailBean> getDetailPageList4Vip(long teacherId, int pageNumber) {
		int pageSize = 6;
		Page<Igpcontent> igpcontents = IgpcontentDao.paginateByTeacher4Vip(teacherId, pageNumber, pageSize);
		List<IgpWebDetailBean> detailList = new ArrayList<IgpWebDetailBean>();
		for (Igpcontent igpcontent : igpcontents.getList()) {
			IgpWebDetailBean tmpIgpWebDetailBean = getIgpWebDetailBean(teacherId, igpcontent);
			detailList.add(tmpIgpWebDetailBean);
		}
		return new Page<IgpWebDetailBean>(detailList, igpcontents.getPageNumber(), igpcontents.getPageSize(),
				igpcontents.getTotalPage(), igpcontents.getTotalRow());
	}

	/****
	 * 生成前端需求
	 * 
	 * @param teacherId
	 * @param igpcontent
	 * @return
	 */
	private IgpWebDetailBean getIgpWebDetailBean(long teacherId, Igpcontent igpcontent) {
		IgpWebDetailBean detail = new IgpWebDetailBean();
		Teacher teacher = TeacherDao.getTeacher(teacherId);
		if (teacher == null || igpcontent == null) {
			return detail;
		}
		detail.setName(teacher.getName());
		detail.setTimeDesc(igpcontent.getRecTimeDesc());
		detail.setKindDescr(
				IgpMsgService.getInstance().getKindDescr(igpcontent.getKind(), igpcontent.getVipGroupInfo()));
		detail.setDetail(getContentDetail(igpcontent));
		return detail;
	}

	public String getContentDetail(Igpcontent igpcontent) {
		return getContentDetail(igpcontent.getBrief(), igpcontent.getKind(), igpcontent.getContent(),
				igpcontent.getContentNew(), Json.fromJsonAsArray(String.class, igpcontent.getImage()),
				Json.fromJsonAsArray(String.class, igpcontent.getImageThumb()));
	}

	/**
	 * 获取详细内容
	 * 
	 * @param brief
	 * @param kind
	 * @param content
	 * @param contentNew
	 * @param imageArr
	 * @param imageThumbArr
	 * @return
	 */
	public String getContentDetail(String brief, String kind, String content, String contentNew, String[] imageArr,
			String[] imageThumbArr) {
		StringBuilder sb = new StringBuilder();
		if (Strings.isNotBlank(brief)) {
			sb.append(brief).append("<br />");
		}
		if (kind != null && kind.equals(EIgpKind.CHARGE.getValue())) {
			sb.append(content).append("<br />");
		}
		if (Strings.isNotBlank(contentNew)) {
			sb.append(contentNew).append("<br />");
		}
		String[] showImageArr = getShowImageArr(imageArr, imageThumbArr);
		if (showImageArr.length > 0) {
			sb.append("<br />");
			for (String image : showImageArr) {
				sb.append("<img src='" + image + "'><br />");
			}
		}
		return sb.toString().replaceAll("<br /><br />", "<br />");
	}

	/**
	 * 获取该显示的图像数组
	 * 
	 * @param imageArr
	 * @param imageThumbArr
	 * @return
	 */
	private String[] getShowImageArr(String[] imageArr, String[] imageThumbArr) {
		if (imageArr.length > 0) {
			return imageArr;
		}
		return imageThumbArr;
	}

	/***
	 * 获取股票信息
	 * 
	 * @param info
	 * @return
	 */
	private String getStockInfoStr(String info) {
		if (Strings.isBlank(info)) {
			return "";
		}
		Set<String> nameSet = new HashSet<String>();
		StringBuilder sb = new StringBuilder();
		List<IgpStockBean> beanList = Json.fromJsonAsList(IgpStockBean.class, info);
		if (beanList != null && beanList.size() > 0) {
			sb.append("股票信息: ");
			for (IgpStockBean bean : beanList) {
				if (bean.getKind().equals("stock")) {
					String stockName = bean.getName();
					if (!nameSet.contains(stockName)) {
						sb.append(stockName).append("(").append(bean.getCode()).append(")").append(" ; ");
						nameSet.add(stockName);
					}
				}
			}
		}
		return sb.toString();
	}

	public List<KindViewBean> getKindList() {
		List<KindViewBean> list = new ArrayList<KindViewBean>();
		for (EIgpKind kind : EIgpKind.values()) {
			KindViewBean bean = new KindViewBean();
			bean.setKind(kind.getValue());
			bean.setDescr(kind.getDescr());
			list.add(bean);
		}
		return list;
	}

	/**
	 * 删除多少天前的数据
	 * 
	 * @param day
	 * @return
	 */
	public int delContentsByDay(int day) {
		long now = System.currentTimeMillis();
		long time = now - (long) day * 24 * 60 * 60 * 1000;
		int res = IgpcontentDao.delByTime(time / 1000);
		return res;
	}
}
