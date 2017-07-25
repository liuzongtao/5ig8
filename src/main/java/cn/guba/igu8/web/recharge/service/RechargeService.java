/**
 * 
 */
package cn.guba.igu8.web.recharge.service;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Page;

import cn.guba.igu8.core.utils.Util;
import cn.guba.igu8.db.dao.RechargeLogDao;
import cn.guba.igu8.db.dao.TeacherDao;
import cn.guba.igu8.db.dao.UserDao;
import cn.guba.igu8.db.dao.VipTypeDao;
import cn.guba.igu8.db.mysqlModel.Rechargelog;
import cn.guba.igu8.db.mysqlModel.Teacher;
import cn.guba.igu8.db.mysqlModel.User;
import cn.guba.igu8.db.mysqlModel.Uservipinfo;
import cn.guba.igu8.db.mysqlModel.Viptype;
import cn.guba.igu8.web.log.beans.RechargeLogViewBean;
import cn.guba.igu8.web.user.service.UserService;

/**
 * @author zongtao liu
 *
 */
public class RechargeService {

	private static volatile RechargeService rechargeService;

	private RechargeService() {
	}

	public static RechargeService getInstance() {
		if (rechargeService == null) {
			synchronized (UserService.class) {
				if (rechargeService == null) {
					rechargeService = new RechargeService();
				}
			}
		}
		return rechargeService;
	}

	public void saveRechargeLog(Rechargelog rechargelog) {
		RechargeLogDao.insert(rechargelog);
	}

	public void saveRechargeLog(Uservipinfo vipInfo, long oldVipEndTime, int costMoney) {
		long uid = vipInfo.getUid();
		Rechargelog rechargelog = new Rechargelog();
		rechargelog.setUid(uid);
		User user = UserDao.getUser(uid);
		rechargelog.setNickname(user.getNickname());
		Long inviterUid = user.getInviterUid();
		if (inviterUid != null && inviterUid != 0) {
			rechargelog.setInviterUid(inviterUid);
			User inviterUser = UserDao.getUser(inviterUid);
			rechargelog.setInviterNickname(inviterUser.getNickname());
		}
		rechargelog.setCreatTime(user.getCreatTime());
		rechargelog.setEmail(user.getEmail());
		rechargelog.setPhoneNumber(user.getPhoneNumber());
		rechargelog.setCurTime(System.currentTimeMillis());
		rechargelog.setOldVipEndTime(oldVipEndTime);
		rechargelog.setNewVipEndTime(vipInfo.getVipEndTime());
		rechargelog.setConcernedTeacherId(vipInfo.getConcernedTeacherId());
		rechargelog.setEmailTypes(vipInfo.getSendEmail());
		rechargelog.setIsSendSms(vipInfo.getSendSms());
		rechargelog.setIsShowUrl(vipInfo.getShowUrl());
		Integer vipTypeId = vipInfo.getVipTypeId();
		Viptype vipType = VipTypeDao.getVipType(vipTypeId);
		rechargelog.setVipTypeId(Long.valueOf(vipTypeId.toString()));
		rechargelog.setVipTypeDescr(vipType.getDescr());
		rechargelog.setCostMoney(costMoney);
		RechargeLogDao.insert(rechargelog);
	}

	/**
	 * 获取分页显示信息
	 * 
	 * @param uid
	 * @param pageNumber
	 * @return
	 */
	public Page<RechargeLogViewBean> getPageList(long uid, int pageNumber) {
		int pageSize = 10;
		Page<Rechargelog> page = null;
		if (uid == 0) {
			page = RechargeLogDao.paginate(pageNumber, pageSize);
		} else {
			page = RechargeLogDao.paginateByUid(uid, pageNumber, pageSize);
		}
		List<RechargeLogViewBean> list = new ArrayList<RechargeLogViewBean>();
		for (Rechargelog info : page.getList()) {
			RechargeLogViewBean view = getUserVipViewBean(info);
			list.add(view);
		}
		return new Page<RechargeLogViewBean>(list, page.getPageNumber(), page.getPageSize(), page.getTotalPage(),
				page.getTotalRow());
	}

	/**
	 * 转换成显示bean
	 * 
	 * @param info
	 * @return
	 */
	private RechargeLogViewBean getUserVipViewBean(Rechargelog rechargelog) {
		RechargeLogViewBean view = new RechargeLogViewBean();
		view.setId(rechargelog.getId());
		view.setUid(rechargelog.getUid());
		view.setNickname(rechargelog.getNickname());
		view.setInviterNickname(rechargelog.getInviterNickname());
		view.setPhoneNumber(String.valueOf(rechargelog.getPhoneNumber()));
		view.setEmail(rechargelog.getEmail());
		view.setCreateTimeStr(Util.dateformat(rechargelog.getCreatTime()));
		view.setTimeStr(Util.dateformat(rechargelog.getCurTime()));
		view.setVipEndTimeStr(Util.dateformat(rechargelog.getNewVipEndTime()));
		view.setEmailTypes(rechargelog.getEmailTypes());
		long teacherId = rechargelog.getConcernedTeacherId();
		Teacher teacher = TeacherDao.getTeacher(teacherId);
		if (teacher != null) {
			view.setTeacherName(teacher.getName());
		}
		view.setCostMoney(rechargelog.getCostMoney());
		return view;
	}

}
