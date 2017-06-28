/**
 * 
 */
package cn.guba.igu8.processor.igupiaoWeb.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;

import cn.guba.igu8.core.constants.Constant;
import cn.guba.igu8.core.mail.MailFactory;
import cn.guba.igu8.core.mail.SMTPMXLookup;
import cn.guba.igu8.core.sms.SmsFactory;
import cn.guba.igu8.core.utils.ExcelUtil;
import cn.guba.igu8.core.utils.Util;
import cn.guba.igu8.db.dao.TeacherDao;
import cn.guba.igu8.db.dao.UserDao;
import cn.guba.igu8.db.dao.UserVipInfoDao;
import cn.guba.igu8.db.mysqlModel.Teacher;
import cn.guba.igu8.db.mysqlModel.User;
import cn.guba.igu8.db.mysqlModel.Uservipinfo;
import cn.guba.igu8.processor.igupiaoWeb.beans.User4AdInfo;
import cn.guba.igu8.processor.igupiaoWeb.msg.beans.EIgpKind;
import cn.guba.igu8.processor.igupiaoWeb.msg.beans.IgpWebMsgBean;
import cn.guba.igu8.web.content.service.ContentsService;

/**
 * @author zongtao liu
 *
 */
public class IgpMsgService {

	private static Map<Long, List<User4AdInfo>> adUserMap = new ConcurrentHashMap<Long, List<User4AdInfo>>();

	private static Log log = Logs.get();

	private static volatile IgpMsgService igpMsgService;

	private IgpMsgService() {
		init();
		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(new UpadateAdUsersThread(), getInitialDelay() / 1000, 7 * 24 * 60 * 60,
				TimeUnit.SECONDS);
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

	/**
	 * 初始化
	 */
	private void init() {
		String fileName = PathKit.getRootClassPath() + File.separator + "user4Ad.xlsx";
		File file = new File(fileName);
		if (!file.exists() || !file.isFile()) {
			log.error(fileName + " is not exist or not a file");
			return;
		}
		String sheetName = getCurWeekName();
		log.info("file is " + file.getAbsolutePath() + " ; sheetName = " + sheetName);
		List<User4AdInfo> excelObjList = ExcelUtil.getExcelObjList(file, sheetName, User4AdInfo.class);
		long now = System.currentTimeMillis();
		adUserMap.clear();
		Set<String> correctSet = new HashSet<String>();
		for (User4AdInfo tmpUser4AdInfo : excelObjList) {
			long endTime = tmpUser4AdInfo.getEndTime();
			if (endTime > now) {
				Long key = tmpUser4AdInfo.getTeacherId();
				String email = tmpUser4AdInfo.getEmail();
				boolean addressValid = isCorrectEmail(correctSet, email);
				if (addressValid) {
					List<User4AdInfo> list = null;
					if (adUserMap.containsKey(key)) {
						list = adUserMap.get(key);
					} else {
						list = new ArrayList<User4AdInfo>();
						adUserMap.put(key, list);
					}
					list.add(tmpUser4AdInfo);
				}
			}
		}
	}

	/**
	 * 检验是否是合法的email
	 * 
	 * @param correctSet
	 * @param email
	 * @return
	 */
	private boolean isCorrectEmail(Set<String> correctSet, String email) {
		Boolean ischeckEmail = PropKit.getBoolean("ischeckEmail");
		if (!ischeckEmail) {
			return true;
		}
		if (correctSet.contains(email)) {
			return true;
		}
		boolean addressValid = SMTPMXLookup.isAddressValid(email);
		if (addressValid) {
			correctSet.add(email);
		} else {
			log.error(email + " is valid !");
		}
		return addressValid;
	}

	/**
	 * 获取初始的延迟时间
	 * 
	 * @return
	 */
	private long getInitialDelay() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, 1);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.add(Calendar.DATE, 7);
		return cal.getTimeInMillis() - System.currentTimeMillis();
	}

	/**
	 * 获取当前周的名字
	 * 
	 * @return
	 */
	private String getCurWeekName() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, 1);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String name = String.valueOf(year * 10000 + month * 100 + day);
		return name;
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
			if (vip.getSendSms()) {
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
			if (vip.getSendSms()) {
				long uid = vip.getUid();
				User user = UserDao.getUser(uid);
				if (user != null) {
					userSet.add(user);
				}
			}
		}
		return userSet;
	}

	/**
	 * 获取邮件列表
	 * 
	 * @param concernedTeacherId
	 * @param kind
	 * @return
	 */
	public Map<Boolean, Set<String>> getVipEmails(long concernedTeacherId, String kind) {
		Map<Boolean, Set<String>> emailMap = new HashMap<Boolean, Set<String>>();
		List<Uservipinfo> vipList = UserVipInfoDao.getUservipinfos(concernedTeacherId);
		if (vipList == null) {
			return emailMap;
		}
		for (Uservipinfo vip : vipList) {
			if (Strings.isNotBlank(vip.getSendEmail()) && vip.getSendEmail().contains(kind)) {
				long uid = vip.getUid();
				User user = UserDao.getUser(uid);
				if (user != null) {
					Boolean showUrl = vip.getShowUrl();
					Set<String> emailSet = null;
					if (emailMap.containsKey(showUrl)) {
						emailSet = emailMap.get(showUrl);
					} else {
						emailSet = new HashSet<String>();
						emailMap.put(showUrl, emailSet);
					}
					emailSet.add(user.getEmail());
				}
			}
		}
		// 加入广告邮件
		Set<String> adEmails = getAdEmails(concernedTeacherId, kind);
		if (adEmails != null && adEmails.size() > 0) {
			Set<String> emailSet = emailMap.get(false);
			if (emailSet == null) {
				emailSet = new HashSet<String>();
				emailMap.put(false, emailSet);
			}
			emailSet.addAll(adEmails);
		}

		return emailMap;
	}

	/**
	 * 获取广告邮件列表
	 * 
	 * @param concernedTeacherId
	 * @param kind
	 * @return
	 */
	private Set<String> getAdEmails(long concernedTeacherId, String kind) {
		int size = adUserMap.size();
		if (size == 0) {
			return null;
		}
		Set<String> mailSet = new HashSet<String>();
		if (adUserMap.containsKey(concernedTeacherId)) {
			List<User4AdInfo> list = adUserMap.get(concernedTeacherId);
			long now = System.currentTimeMillis();
			for (User4AdInfo tmpUser4AdInfo : list) {
				if (tmpUser4AdInfo.getEndTime() > now && tmpUser4AdInfo.getKinds().contains(kind)) {
					mailSet.add(tmpUser4AdInfo.getEmail());
				}
			}
		}
		return mailSet;
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
		log.info(teacher.getName() + " === " + msg.getRec_time_desc() + " == thread == " + Thread.currentThread()
				+ " == " + new Date());

		// 发送email
		Boolean isSendEmail = PropKit.getBoolean("sendEmail", false);
		if (isSendEmail) {
			Map<Boolean, Set<String>> emailMap = getVipEmails(teacherId, msg.getKind());
			if (emailMap.size() > 0) {
				// 将需要显示url和不需要显示url的用户分开发送邮件
				Set<Boolean> keySet = emailMap.keySet();
				String emailTitle = teacher.getName() + getKindDescr(msg.getKind(), msg.getVip_group_info());
				for (Boolean key : keySet) {
					Set<String> emailSet = emailMap.get(key);
					MailFactory.getInstance().sendEmail(emailSet, emailTitle, getEmailContent(teacher, msg, key));
				}

			}
		}
		// 发送短消息消息
		Boolean isSendSms = PropKit.getBoolean("sendSms", false);
		if (sendSms && isSendSms && msg.getKind().equals(EIgpKind.VIP.getValue()) && isMarketOpen()) {
			// 是否加入邮箱信息发送短消息
			Boolean sendSmsWithEmail = PropKit.getBoolean("sendSmsWithEmail", false);
			if (sendSmsWithEmail) {
				Set<User> users = getVipUsers4Sms(teacherId);
				// 进行分类发送
				Map<String, Set<Long>> smsContentMap = new HashMap<String, Set<Long>>();
				for (User user : users) {
					String smsContent = getSmsContent(teacher, msg, user.getEmail());
					Set<Long> phoneSet = null;
					if (smsContentMap.containsKey(smsContent)) {
						phoneSet = smsContentMap.get(smsContent);
					} else {
						phoneSet = new HashSet<Long>();
						smsContentMap.put(smsContent, phoneSet);
					}
					phoneSet.add(user.getPhoneNumber());
				}
				for (String smsContent : smsContentMap.keySet()) {
					SmsFactory.getInstance().sendSms(smsContentMap.get(smsContent), smsContent);
				}
			} else {
				Set<Long> vipPhones = getVipPhones(teacherId);
				String smsContent = getSmsContent(teacher, msg);
				SmsFactory.getInstance().sendSms(vipPhones, smsContent);
			}
		}
	}

	/***
	 * 是否开始
	 * 
	 * @return
	 */
	private boolean isMarketOpen() {
		Calendar instance = Calendar.getInstance();
		// 判断是否是工作日
		int curday = instance.get(Calendar.DAY_OF_WEEK);
		int beginday = 2;
		int endday = 6;
		if (curday < beginday && curday > endday) {
			return false;
		}
		// 判断是否是开市时间
		int beginHour = 9;
		int endHour = 15;
		int curHour = instance.get(Calendar.HOUR_OF_DAY);
		if (curHour >= beginHour && curHour < endHour) {
			return true;
		}
		return false;
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
	private String getEmailContent(Teacher teacher, IgpWebMsgBean msg, boolean showUrl) {
		StringBuilder sb = new StringBuilder();
		sb.append(teacher.getName()).append(getKindDescr(msg.getKind(), msg.getVip_group_info())).append("<br />");
		sb.append(Util.dateSecondFormat(msg.getRec_time())).append("<br />");
		sb.append(ContentsService.getInstance().getContentDetail(msg.getBrief(), msg.getKind(), msg.getContent(),
				msg.getContent_new(), msg.getImage(), msg.getImage_thumb())).append("<br />");
		if (showUrl) {
			sb.append("更多信息：").append(Constant.URL_5IGU8_LIST + "?tid=" + teacher.getId());
		}
		return sb.toString();
	}

	/**
	 * 获取vip描述
	 * 
	 * @param kind
	 * @param vipGroupInfo
	 * @return
	 */
	public String getKindDescr(String kind, String vipGroupInfo) {
		if (kind.equals(EIgpKind.VIP.getValue())) {
			if (Strings.isNotBlank(vipGroupInfo)) {
				return "【会员信息：" + vipGroupInfo + "】";
			} else {
				return "【会员信息】";
			}
		} else {
			return "【公开信息】";
		}
	}

	private class UpadateAdUsersThread extends Thread {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			init();
		}
	}
	
	public static void main(String[] args) {
		String fileName = PathKit.getRootClassPath() + File.separator + "user4Ad.xlsx";
		File file = new File(fileName);
		if (!file.exists() || !file.isFile()) {
			log.error(fileName + " is not exist or not a file");
			return;
		}
		List<String> sheetNames = ExcelUtil.getSheetNames(file);
		Set<String> correctSet = new HashSet<String>();
		for(String sheetName : sheetNames){
			System.out.println(" == " + sheetName + " == " );
			Set<String> errorSet = new HashSet<String>();
			List<User4AdInfo> excelObjList = ExcelUtil.getExcelObjList(file, sheetName, User4AdInfo.class);
			for(User4AdInfo tmpUser4AdInfo : excelObjList){
				String email = tmpUser4AdInfo.getEmail();
				if (correctSet.contains(email)) {
					continue;
				}
				boolean addressValid = SMTPMXLookup.isAddressValid(email);
				if (addressValid) {
					correctSet.add(email);
				} else {
					if(errorSet.contains(email)){
						System.out.println(sheetName + " == " + email );
					}
					errorSet.add(email);
				}
			}
			
		}
		
	}

}
