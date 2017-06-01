/**
 * 
 */
package cn.guba.igu8.core.init;

import java.util.ArrayList;
import java.util.List;

import org.nutz.lang.Lang;

import cn.guba.igu8.db.dao.TeacherDao;
import cn.guba.igu8.db.dao.UserDao;
import cn.guba.igu8.db.dao.UserVipInfoDao;
import cn.guba.igu8.db.dao.VipTypeDao;
import cn.guba.igu8.db.mysqlModel.Teacher;
import cn.guba.igu8.db.mysqlModel.User;
import cn.guba.igu8.db.mysqlModel.Uservipinfo;
import cn.guba.igu8.processor.igupiaoWeb.msg.beans.EIgpKind;

/**
 * @author zongtao liu
 *
 */
public class SysInit {

	private static volatile SysInit sysInit;

	private SysInit() {
	}

	public static SysInit getInstance() {
		if (sysInit == null) {
			synchronized (SysInit.class) {
				if (sysInit == null) {
					sysInit = new SysInit();
				}
			}
		}
		return sysInit;
	}

	public void init() {
		initDb();
		initUser();
	}

	/***
	 * 初始化db
	 */
	private void initDb() {
		VipTypeDao.initInsert();
		TeacherDao.initInsert();
	}

	private void initUser() {
		long adminUid = initAdmin();
		initSubAdmin(adminUid);
	}

	/***
	 * 初始化管理员
	 * 
	 * @return
	 */
	private long initAdmin() {
		String nickName = "admin";
		User user = UserDao.getUser(nickName);
		if (user == null) {
			user = new User();
			user.setNickname(nickName);
			user.setPasswd(Lang.md5(nickName + "@2017"));
			user.setCreatTime(System.currentTimeMillis());
			user.setPhoneNumber(18701641809l);
			user.setEmail("369650047@qq.com");
			user.save();
			long uid = user.getId();
			// 插入vip信息
			initVipInfo(uid);
		}
		return user.getId();
	}

	/***
	 * 初始化副管理员
	 * 
	 * @param adminUid
	 * @return
	 */
	private long initSubAdmin(long adminUid) {
		String nickName = "subadmin";
		User user = UserDao.getUser(nickName);
		if (user == null) {
			user = new User();
			user.setNickname(nickName);
			user.setPasswd(Lang.md5(nickName + "@2017"));
			user.setCreatTime(System.currentTimeMillis());
			user.setPhoneNumber(18810569982l);
			user.setEmail("123660735@qq.com");
			user.setInviterUid(adminUid);
			user.save();
			long uid = user.getId();
			// 插入vip信息
			initVipInfo(uid);
		}

		return user.getId();
	}

	/**
	 * 初始化vip信息
	 * 
	 * @param uid
	 */
	private void initVipInfo(long uid) {
		// 插入vip信息
		List<Teacher> allTeacherList = TeacherDao.getAllTeacherList();
		List<Uservipinfo> vipInfoList = new ArrayList<Uservipinfo>();
		for (Teacher teacher : allTeacherList) {
			Uservipinfo vipInfo = new Uservipinfo();
			vipInfo.setUid(uid);
			vipInfo.setVipTypeId(teacher.getVipTypeId());
			vipInfo.setVipEndTime(Long.MAX_VALUE);
			vipInfo.setConcernedTeacherId(teacher.getId());
			vipInfo.setSendEmail(getSendEmailStr());
			vipInfo.setSendSms(1);
			vipInfoList.add(vipInfo);
		}
		UserVipInfoDao.batchInsert(vipInfoList);
	}

	private String getSendEmailStr() {
		StringBuilder sb = new StringBuilder();
		for (EIgpKind kind : EIgpKind.values()) {
			sb.append(kind.getValue()).append(",");
		}
		return sb.substring(0, sb.length() - 1);
	}

}
