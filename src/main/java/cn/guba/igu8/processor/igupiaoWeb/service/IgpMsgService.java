/**
 * 
 */
package cn.guba.igu8.processor.igupiaoWeb.service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.jfinal.kit.PropKit;

import cn.guba.igu8.core.constants.Constant;
import cn.guba.igu8.core.mail.MailFactory;
import cn.guba.igu8.core.sms.SmsFactory;
import cn.guba.igu8.core.utils.Util;
import cn.guba.igu8.db.dao.TeacherDao;
import cn.guba.igu8.db.dao.UserDao;
import cn.guba.igu8.db.dao.UserVipInfoDao;
import cn.guba.igu8.db.mysqlModel.Teacher;
import cn.guba.igu8.db.mysqlModel.User;
import cn.guba.igu8.db.mysqlModel.Uservipinfo;
import cn.guba.igu8.processor.igupiaoWeb.msg.beans.EIgpKind;
import cn.guba.igu8.processor.igupiaoWeb.msg.beans.IgpWebMsgBean;
import cn.guba.igu8.web.content.service.ContentsService;

/**
 * @author zongtao liu
 *
 */
public class IgpMsgService {
	
	private static Log log = Logs.get();

	private static volatile IgpMsgService igpMsgService;

	private IgpMsgService() {
	}

	public static IgpMsgService getInstance() {
		if (igpMsgService == null) {
			synchronized (IgpMsgService.class) {
				if (igpMsgService == null) {
					igpMsgService = new IgpMsgService();
				}
			}
		}
		return igpMsgService;
	}

	/***
	 * 获取vip电话
	 * 
	 * @param concernedTeacherId
	 * @return
	 */
	public Set<Long> getVipPhones(long concernedTeacherId) {
		Set<Long> phoneSet = new HashSet<Long>();
		List<Uservipinfo> vipList = UserVipInfoDao.getUservipinfos(concernedTeacherId);
		if (vipList == null) {
			return phoneSet;
		}
		for (Uservipinfo vip : vipList) {
			if (vip.getSendSms() > 0) {
				long uid = vip.getUid();
				User user = UserDao.getUser(uid);
				if (user != null) {
					phoneSet.add(user.getPhoneNumber());
				}
			}
		}
		return phoneSet;
	}

	public Set<User> getVipUsers(long concernedTeacherId) {
		Set<User> userSet = new HashSet<User>();
		List<Uservipinfo> vipList = UserVipInfoDao.getUservipinfos(concernedTeacherId);
		if (vipList == null) {
			return userSet;
		}
		for (Uservipinfo vip : vipList) {
			long uid = vip.getUid();
			User user = UserDao.getUser(uid);
			if (user != null) {
				userSet.add(user);
			}
		}
		return userSet;
	}

	public Set<User> getVipUsers4Sms(long concernedTeacherId) {
		Set<User> userSet = new HashSet<User>();
		List<Uservipinfo> vipList = UserVipInfoDao.getUservipinfos(concernedTeacherId);
		if (vipList == null) {
			return userSet;
		}
		for (Uservipinfo vip : vipList) {
			if (vip.getSendSms() > 0) {
				long uid = vip.getUid();
				User user = UserDao.getUser(uid);
				if (user != null) {
					userSet.add(user);
				}
			}
		}
		return userSet;
	}

	public Set<String> getVipEmails(long concernedTeacherId, String kind) {
		Set<String> emailSet = new HashSet<String>();
		List<Uservipinfo> vipList = UserVipInfoDao.getUservipinfos(concernedTeacherId);
		if (vipList == null) {
			return emailSet;
		}
		for (Uservipinfo vip : vipList) {
			if (Strings.isNotBlank(vip.getSendEmail()) && vip.getSendEmail().contains(kind)) {
				long uid = vip.getUid();
				User user = UserDao.getUser(uid);
				if (user != null) {
					emailSet.add(user.getEmail());
				}
			}
		}
		return emailSet;
	}

	/**
	 * 发送消息
	 * 
	 * @param teacherId
	 * @param msg
	 * @param sendSms
	 */
	public void sendMsg(long teacherId, IgpWebMsgBean msg, boolean sendSms) {

		// 生成消息内容
		Teacher teacher = TeacherDao.getTeacher(teacherId);
		log.debug(teacher.getName() + " === " + msg.getRec_time_desc() + " == thread == "
				+ Thread.currentThread() + " == " + new Date());

		// 发送email
		Boolean isSendEmail = PropKit.getBoolean("sendEmail", false);
		if (isSendEmail) {
			Set<String> vipEmailSet = getVipEmails(teacherId, msg.getKind());
			MailFactory.getInstance().sendEmail(vipEmailSet, teacher.getName() + getKindDescr(msg.getKind()),
					getEmailContent(teacher, msg));
		}
		// 发送短消息消息
		Boolean isSendSms = PropKit.getBoolean("sendSms", false);
		if (sendSms && isSendSms && msg.getKind().equals(EIgpKind.VIP.getValue())) {
			// 是否加入邮箱信息发送短消息
			Boolean sendSmsWithEmail = PropKit.getBoolean("sendSmsWithEmail", false);
			if (sendSmsWithEmail) {
				Set<User> users = getVipUsers4Sms(teacherId);
				for (User user : users) {
					String smsContent = getSmsContent(teacher, msg, user.getEmail());
					SmsFactory.getInstance().sendSms(user.getPhoneNumber(), smsContent);
				}
			} else {
				Set<Long> vipPhones = getVipPhones(teacherId);
				String smsContent = getSmsContent(teacher, msg);
				SmsFactory.getInstance().sendSms(vipPhones, smsContent);
			}
		}
	}

	/***
	 * 获取短消息内容
	 * 
	 * @param teacher
	 * @param msg
	 * @param email
	 * @return
	 */
	public static String getSmsContent(Teacher teacher, IgpWebMsgBean msg, String email) {
		String sf = "尊敬的会员：%s的%s的会员指导信息，已经发送的您的会员邮箱%s,请注意查收！";
		String tName = teacher.getName().substring(0, 1) + "老师";
		String timeStr = Util.timeFormat(msg.getRec_time());
		String formatStr = String.format(sf, tName, timeStr, email);
		if (formatStr.length() >= 63) {
			formatStr = String.format(sf, tName, timeStr, "");
		}
		return formatStr;
	}

	/***
	 * 获取短消息内容
	 * 
	 * @param teacher
	 * @param msg
	 * @return
	 */
	public static String getSmsContent(Teacher teacher, IgpWebMsgBean msg) {
		return getSmsContent(teacher, msg, "");
	}

	/***
	 * 获取邮件发送内容
	 * 
	 * @param teacherName
	 * @param msg
	 * @return
	 */
	private String getEmailContent(Teacher teacher, IgpWebMsgBean msg) {
		StringBuilder sb = new StringBuilder();
		sb.append(teacher.getName()).append(getKindDescr(msg.getKind())).append("<br />");
		sb.append(Util.dateSecondFormat(msg.getRec_time())).append("<br />");
		sb.append(ContentsService.getInstance().getContentDetail(msg.getBrief(), msg.getKind(), msg.getContent(),
				msg.getContent_new(), Json.toJson(msg.getStock_info(), JsonFormat.compact()), msg.getImage_thumb()))
				.append("<br />");
		sb.append("更多信息：").append(Constant.URL_5IGU8_LIST + "?tid=" + teacher.getId());
		return sb.toString();
	}

	private String getKindDescr(String kind) {
		if (kind.equals(EIgpKind.VIP.getValue())) {
			return "【会员信息】";
		} else {
			return "【公开信息】";
		}
	}

}
