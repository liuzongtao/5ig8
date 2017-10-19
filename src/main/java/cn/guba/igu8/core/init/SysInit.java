/**
 * 
 */
package cn.guba.igu8.core.init;

import java.util.List;
import java.util.Map;

import org.nutz.http.Response;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.jfinal.kit.PropKit;

import cn.guba.igu8.core.constants.Constant;
import cn.guba.igu8.core.utils.HttpUtil;
import cn.guba.igu8.db.dao.TeacherDao;
import cn.guba.igu8.db.dao.UserDao;
import cn.guba.igu8.db.dao.VipTypeDao;
import cn.guba.igu8.db.mysqlModel.Teacher;
import cn.guba.igu8.db.mysqlModel.User;
import cn.guba.igu8.db.mysqlModel.Uservipinfo;
import cn.guba.igu8.web.teacher.beans.EIgpTeacher;
import cn.guba.igu8.web.teacher.service.TeacherService;
import cn.guba.igu8.web.user.beans.ERoleType;
import cn.guba.igu8.web.vip.service.VipService;

/**
 * @author zongtao liu
 *
 */
public class SysInit {

	private static Log log = Logs.get();

	private static volatile SysInit sysInit;

	public static final String adminNickname = "admin";

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
		// 初始化参数
		initParam();
	}

	private void initParam() {
		Constant.IGP_URL_PARAM_MD = PropKit.get("md");
		// 从网络获取，如果获取的到新的，则用最新的
		initIgpUrlParam();
		log.info("Constant.IGP_URL_PARAM_MD == " + Constant.IGP_URL_PARAM_MD);
	}

	/**
	 * 初始化股票参数
	 */
	public void initIgpUrlParam() {
		String igpUrlParamMd = getIgpUrlParamMdFromNet();
		if (Strings.isNotBlank(igpUrlParamMd)) {
			Constant.IGP_URL_PARAM_MD = igpUrlParamMd;
		}
	}

	/**
	 * 从网上获取网络参数
	 * 
	 * @return
	 */
	private String getIgpUrlParamMdFromNet() {
		String igpUrlParamMd = "";
		Response response = HttpUtil.get(Constant.URL_IGP_BASE);
		String content = response.getContent();
		String mdBegin = "var _Libs=";
		if (content.contains(mdBegin)) {
			int begin = content.indexOf(mdBegin) + mdBegin.length();
			content = content.substring(begin, content.length());
			int end = content.indexOf("}");
			String substring = content.substring(0, end + 1);
			Map<String, String> fromJsonAsMap = Json.fromJsonAsMap(String.class, substring);
			igpUrlParamMd = fromJsonAsMap.get("md");
		}
		return igpUrlParamMd;
	}

	/***
	 * 初始化db
	 */
	private void initDb() {
		VipTypeDao.initInsert();
		TeacherService.getInstance().initTeacher();
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
		String nickName = adminNickname;
		User user = UserDao.getUser(nickName);
		if (user == null) {
			user = new User();
			user.setNickname(nickName);
			user.setPasswd(nickName + "@2017");
			user.setCreatTime(System.currentTimeMillis());
			user.setPhoneNumber(18701641809l);
			user.setEmail("369650047@qq.com");
			user.setRoleType(ERoleType.ADMIN.getValue());
			UserDao.saveUser(user);
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
			user.setPasswd(nickName + "@2017");
			user.setCreatTime(System.currentTimeMillis());
			user.setPhoneNumber(18810569982l);
			user.setEmail("123660735@qq.com");
			user.setInviterUid(adminUid);
			user.setRoleType(ERoleType.ADMIN.getValue());
			UserDao.saveUser(user);
			long uid = user.getId();
			// 插入vip信息
			insertIgpVipInfo(uid, EIgpTeacher.niuguqimeng.getValue());
		}

		return user.getId();
	}

	/**
	 * 插入vip信息
	 * 
	 * @param uid
	 * @param teacher
	 * @param isSendSms
	 */
	public void insertVipInfo(long uid, Teacher teacher, boolean isSendSms) {
		insertVipInfo(uid, teacher, VipService.getInstance().getSendVipEmailStr(), isSendSms);
	}

	/**
	 * 插入vip信息
	 * 
	 * @param uid
	 * @param teacher
	 * @param sendEmail
	 * @param isSendSms
	 */
	public void insertVipInfo(long uid, Teacher teacher, String sendEmail, boolean isSendSms) {
		Uservipinfo vipInfo = new Uservipinfo();
		vipInfo.setUid(uid);
		vipInfo.setVipTypeId(teacher.getVipTypeId());
		vipInfo.setVipEndTime(Long.MAX_VALUE);
		vipInfo.setConcernedTeacherId(teacher.getId());
		vipInfo.setSendEmail(sendEmail);
		if (isSendSms) {
			vipInfo.setSendSms(true);
		}
		vipInfo.save();
	}

	/**
	 * 插入爱股票信息
	 * 
	 * @param uid
	 * @param teacherPfId
	 */
	private void insertIgpVipInfo(long uid, int teacherPfId) {
		Teacher igpTeacher = TeacherDao.getIgpTeacher(teacherPfId);
		insertVipInfo(uid, igpTeacher, VipService.getInstance().getSendAllEmailStr(), true);
	}

	/**
	 * 初始化vip信息
	 * 
	 * @param uid
	 */
	private void initVipInfo(long uid) {
		// 插入vip信息
		List<Teacher> allTeacherList = TeacherDao.getAllTeacherList();
		for (Teacher teacher : allTeacherList) {
			boolean isSendSms = false;
			String sendEmail = VipService.getInstance().getSendVipEmailStr();
			if (teacher.getPfId() == EIgpTeacher.niuguqimeng.getValue()) {
				isSendSms = true;
				sendEmail = VipService.getInstance().getSendAllEmailStr();
			}
			if (teacher.getPfId() == EIgpTeacher.niepanchongsheng.getValue()) {
				sendEmail = VipService.getInstance().getSendAllEmailStr();
			}
			insertVipInfo(uid, teacher, sendEmail, isSendSms);
		}
	}

}
