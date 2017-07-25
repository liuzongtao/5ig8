/**
 * 
 */
package cn.guba.igu8.web.vip.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.jfinal.plugin.activerecord.Page;

import cn.guba.igu8.core.utils.Util;
import cn.guba.igu8.db.dao.UserVipInfoDao;
import cn.guba.igu8.db.mysqlModel.Uservipinfo;
import cn.guba.igu8.processor.igupiaoWeb.msg.beans.EIgpKind;
import cn.guba.igu8.web.mail.service.MailService;
import cn.guba.igu8.web.recharge.service.RechargeService;
import cn.guba.igu8.web.teacher.service.TeacherService;
import cn.guba.igu8.web.vip.beans.EVipType;
import cn.guba.igu8.web.vip.beans.UserVipViewBean;
import cn.guba.igu8.web.vip.beans.VipTypeViewBean;

/**
 * @author zongtao liu
 *
 */
public class VipService {

	private static volatile VipService vipService;

	private VipService() {
	}

	public static VipService getInstance() {
		if (vipService == null) {
			synchronized (VipService.class) {
				if (vipService == null) {
					vipService = new VipService();
				}
			}
		}
		return vipService;
	}

	/**
	 * 获取发送邮件信息
	 * 
	 * @return
	 */
	public String getSendVipEmailStr() {
		return EIgpKind.VIP.getValue();
	}

	public String getSendAllEmailStr() {
		StringBuilder sb = new StringBuilder();
		for (EIgpKind kind : EIgpKind.values()) {
			sb.append(kind.getValue()).append(",");
		}
		return sb.substring(0, sb.length() - 1);
	}

	/**
	 * 获取某用户的vip信息
	 * 
	 * @param uid
	 * @return
	 */
	public List<Uservipinfo> getVipList(long uid) {
		return UserVipInfoDao.getVipList(uid);
	}

	/**
	 * 根据uid进行删除
	 * 
	 * @param uid
	 */
	public void delByUid(long uid) {
		UserVipInfoDao.delByUid(uid);
	}

	/**
	 * 根据id进行删除
	 * 
	 * @param id
	 */
	public void delById(long id) {
		UserVipInfoDao.delById(id);
	}

	/**
	 * 获取分页显示信息
	 * 
	 * @param uid
	 * @param pageNumber
	 * @return
	 */
	public Page<UserVipViewBean> getPageList(long uid, int pageNumber) {
		int pageSize = 10;
		Page<Uservipinfo> page = UserVipInfoDao.paginateByUid(uid, pageNumber, pageSize);
		List<UserVipViewBean> list = new ArrayList<UserVipViewBean>();
		for (Uservipinfo info : page.getList()) {
			UserVipViewBean view = getUserVipViewBean(info);
			list.add(view);
		}
		return new Page<UserVipViewBean>(list, page.getPageNumber(), page.getPageSize(), page.getTotalPage(),
				page.getTotalRow());
	}

	/**
	 * 转换成显示bean
	 * 
	 * @param info
	 * @return
	 */
	private UserVipViewBean getUserVipViewBean(Uservipinfo info) {
		UserVipViewBean view = new UserVipViewBean();
		view.setId(info.getId());
		view.setUid(info.getUid());
		view.setVipPf(EVipType.getEVipType(info.getVipTypeId()).getDescr());
		long teacherId = info.getConcernedTeacherId();
		String teacherName = TeacherService.getInstance().getTeacherName(teacherId);
		view.setTeacherId(teacherId);
		view.setTeacherName(teacherName);
		view.setEndTime(Util.dateformat(info.getVipEndTime()));
		view.setEmailType(info.getSendEmail());
		view.setIsSendSms(info.getSendSms() ? 1 : 0);
		view.setIsShowUrl(info.getShowUrl() ? 1 : 0);
		return view;
	}

	/**
	 * 获取页面显示的vip信息
	 * 
	 * @param id
	 * @return
	 */
	public UserVipViewBean getUserVipViewBeanById(long id) {
		Uservipinfo info = UserVipInfoDao.getById(id);
		return getUserVipViewBean(info);
	}

	/**
	 * 获取vip类型列表
	 * 
	 * @return
	 */
	public List<VipTypeViewBean> getVipTypeList() {
		List<VipTypeViewBean> list = new ArrayList<VipTypeViewBean>();
		for (EVipType type : EVipType.values()) {
			VipTypeViewBean bean = new VipTypeViewBean();
			bean.setTypeId(type.getValue());
			bean.setTypeDescr(type.getDescr());
			list.add(bean);
		}
		return list;
	}

	/**
	 * 添加vip信息
	 * 
	 * @param viewInfo
	 * @param emailTypeArr
	 */
	public void addVipInfo(UserVipViewBean viewInfo, String[] emailTypeArr, int vipfee) {
		Uservipinfo vipInfo = new Uservipinfo();
		vipInfo.setUid(viewInfo.getUid());
		vipInfo.setVipTypeId(viewInfo.getVipPfType());
		long endTime = addTime(System.currentTimeMillis(), viewInfo.getPeriodNum(), viewInfo.getPeriodType());
		vipInfo.setVipEndTime(endTime);
		vipInfo.setConcernedTeacherId(viewInfo.getTeacherId());
		String sendEmail = getSendEmail(emailTypeArr);
		vipInfo.setSendEmail(sendEmail);
		vipInfo.setSendSms(viewInfo.getIsSendSms() == 1 ? true : false);
		vipInfo.setShowUrl(viewInfo.getIsShowUrl() == 1 ? true : false);
		UserVipInfoDao.add(vipInfo);
		// 增加日志
		RechargeService.getInstance().saveRechargeLog(vipInfo, 0, vipfee);
		// 发送邮件通知
		MailService.getInstance().sendNoticeEmail(viewInfo.getUid(), viewInfo.getTeacherId(), endTime);
	}

	/**
	 * 更新vip信息
	 * 
	 * @param viewInfo
	 * @param emailTypeArr
	 */
	public void updateVipInfo(UserVipViewBean viewInfo, String[] emailTypeArr, int vipfee) {
		long id = viewInfo.getId();
		Uservipinfo uservipinfo = UserVipInfoDao.getById(id);
		uservipinfo.setVipTypeId(viewInfo.getVipPfType());
		long newTeacherId = viewInfo.getTeacherId();
		long endTime = addTime(uservipinfo.getVipEndTime(), viewInfo.getPeriodNum(), viewInfo.getPeriodType());
		long oldVipEndTime = uservipinfo.getVipEndTime();
		uservipinfo.setVipEndTime(endTime);
		uservipinfo.setConcernedTeacherId(newTeacherId);
		String sendEmail = getSendEmail(emailTypeArr);
		uservipinfo.setSendEmail(sendEmail);
		uservipinfo.setSendSms(viewInfo.getIsSendSms() == 1 ? true : false);
		uservipinfo.setShowUrl(viewInfo.getIsShowUrl() == 1 ? true : false);
		UserVipInfoDao.update(uservipinfo);
		// 增加日志
		RechargeService.getInstance().saveRechargeLog(uservipinfo, oldVipEndTime, vipfee);
		// 发送邮件通知
		MailService.getInstance().sendNoticeEmail(viewInfo.getUid(), viewInfo.getTeacherId(), endTime);
	}

	/**
	 * 获取发送email类型
	 * 
	 * @param emailTypeArr
	 * @return
	 */
	private String getSendEmail(String[] emailTypeArr) {
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for (String emailType : emailTypeArr) {
			if (index != 0) {
				sb.append(",");
			}
			sb.append(emailType);
			index++;
		}
		return sb.toString();
	}

	/**
	 * 增加时间
	 * 
	 * @param beginTime
	 * @param amount
	 * @param type
	 * @return
	 */
	private long addTime(long beginTime, int amount, int type) {
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(beginTime);
		switch (type) {
		case 1:// 天
			instance.add(Calendar.DATE, amount);
			break;
		case 2:// 周
			instance.add(Calendar.WEEK_OF_YEAR, amount);
			break;
		case 3:// 月
			instance.add(Calendar.MONTH, amount);
			break;
		case 4:// 年
			instance.add(Calendar.YEAR, amount);
			break;
		default:
			break;
		}
		return instance.getTimeInMillis();
	}

}
