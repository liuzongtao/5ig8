/**
 * 
 */
package cn.guba.igu8.processor.igupiaoWeb.msg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.nutz.http.Cookie;
import org.nutz.http.Response;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.jfinal.kit.PropKit;

import cn.guba.igu8.core.constants.Constant;
import cn.guba.igu8.core.mail.MailFactory;
import cn.guba.igu8.core.utils.HttpUtil;
import cn.guba.igu8.core.utils.Util;
import cn.guba.igu8.db.dao.IgpcontentDao;
import cn.guba.igu8.db.dao.TeacherDao;
import cn.guba.igu8.db.dao.UserDao;
import cn.guba.igu8.db.mysqlModel.Teacher;
import cn.guba.igu8.db.mysqlModel.User;
import cn.guba.igu8.processor.igupiaoWeb.msg.beans.EIgpContentSource;
import cn.guba.igu8.processor.igupiaoWeb.msg.beans.EIgpKind;
import cn.guba.igu8.processor.igupiaoWeb.msg.beans.IgpDetailBean;
import cn.guba.igu8.processor.igupiaoWeb.msg.beans.IgpDetailMsgBean;
import cn.guba.igu8.processor.igupiaoWeb.msg.beans.IgpWebLiverMsgBean;
import cn.guba.igu8.processor.igupiaoWeb.msg.beans.IgpWebMsgBean;
import cn.guba.igu8.processor.igupiaoWeb.threads.SendMessageThread;
import cn.guba.igu8.processor.igupiaoWeb.threads.ThreadsManager;
import cn.guba.igu8.web.teacher.service.TeacherService;

/**
 * @author zongtao liu
 *
 */
public class IgpMsgFactory {

	private static Log log = Logs.get();

	private static long lastSendMailTime = 0;

	private Random random = new Random();

	private static volatile IgpMsgFactory msgFactory;

	private IgpMsgFactory() {
	}

	public static IgpMsgFactory getInstance() {
		if (msgFactory == null) {
			synchronized (IgpMsgFactory.class) {
				if (msgFactory == null) {
					msgFactory = new IgpMsgFactory();
				}
			}
		}
		return msgFactory;
	}

	public static Map<Long, IgpAceInfo> aceInfoMap = new ConcurrentHashMap<Long, IgpAceInfo>();

	/**
	 * 更新消息
	 * 
	 * @param cookie
	 * @param uid
	 */
	public void updateLiverMsg(Cookie cookie, int uid) {
		log.debug("uid = " + uid + " ; cookie = " + Json.toJson(cookie, JsonFormat.compact()));
		List<Teacher> igpTeacherList = TeacherDao.getIgpTeachers();
		log.debug("uid = " + uid + " ; igpTeacherList = " + Json.toJson(igpTeacherList));
		long now = System.currentTimeMillis();
		boolean uidNone = false;
		if (uid == 0) {
			uidNone = true;
		}
		for (Teacher teacher : igpTeacherList) {
			if (now > teacher.getBuyEndTime()) {
				continue;
			}
			if (uidNone) {
				uid = 5 + random.nextInt(99999);
			}
			int pfId = teacher.getPfId();
			long tearcherId = teacher.getId();
			IgpWebLiverMsgBean liverMsg = getLiverMsg(cookie, uid, pfId);
			// 如果获取失败，再次获取一次
			if (liverMsg == null) {
				liverMsg = getLiverMsg(cookie, uid, pfId);
			}
			log.debug("teacher = " + teacher.getName());
			if (liverMsg != null) {
				log.debug("teacher = " + teacher.getName() + " ; liverMsg is " + liverMsg.getRslt());
				IgpAceInfo aceInfo = null;
				if (aceInfoMap.containsKey(tearcherId)) {
					aceInfo = aceInfoMap.get(tearcherId);
				} else {
					aceInfo = new IgpAceInfo(tearcherId);
					aceInfoMap.put(tearcherId, aceInfo);
				}
				aceInfo.addMsg(liverMsg);
			}
		}
	}

	/**
	 * 获取线上信息
	 * 
	 * @param cookie
	 * @param uid
	 * @param pfId
	 * @return
	 */
	private IgpWebLiverMsgBean getLiverMsg(Cookie cookie, int uid, int pfId) {
		return getLiverMsg(cookie, uid, pfId, 0);
	}

	private IgpWebLiverMsgBean getLiverMsg(Cookie cookie, int uid, int pfId, long time) {
		String url = Constant.URL_IGP_MSG_LIVER;
		String mdValue = PropKit.get("md");
		if(Strings.isNotBlank(mdValue)){
			url += "&md=" + mdValue;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", pfId);
		params.put("u_id", uid);
		params.put("before", time);
		params.put("source", "pc");
		IgpWebLiverMsgBean liverMsg = null;
		try {
			Response response = HttpUtil.httpsPost(url, params, cookie);
			if (response != null && Strings.isNotBlank(response.getContent()) && response.getContent().contains("rslt")
					&& response.getContent().contains("msg_list")) {
				String content = response.getContent();
				if(content.contains("<Sitemap>")){
					String flag = "</Sitemap>";
					int end = content.indexOf(flag);
					content = content.substring(end + flag.length(), content.length());
				}
				liverMsg = Json.fromJson(IgpWebLiverMsgBean.class, content);
				String contentSource = "";
				try {
					contentSource = PropKit.get("contentSource");
				} catch (Exception e) {
				}
				if (Strings.equals(contentSource, EIgpContentSource.DETAIL.getValue())) {
					getContentFromDetail(liverMsg, cookie, pfId);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		if (liverMsg == null) {
			log.error("uid == " + uid + " ; liverMsg is null ! pfId == " + pfId);
			long now = System.currentTimeMillis();
			// 每10分钟发送一次
			if (now - lastSendMailTime > 10 * 60 * 1000l) {
				lastSendMailTime = now;
				User admin = UserDao.getAdmin();
				String teacherName = TeacherService.getInstance().getIgpTeacherNameByPfId(pfId);
				MailFactory.getInstance().sendEmail(admin.getEmail(), Constant.EMAIL_NAME + ":获取消息失败",
						"获取老师：" + teacherName + " 消息失败！时间：" + Util.dateformat(now));
			}
		} else {
			log.debug("uid == " + uid + " ; liverMsg == " + Json.toJson(liverMsg, JsonFormat.compact()));
		}

		return liverMsg;
	}

	/**
	 * 从详细信息页面获取内容
	 * 
	 * @param liverMsg
	 * @param cookie
	 * @param pfId
	 */
	private void getContentFromDetail(IgpWebLiverMsgBean liverMsg, Cookie cookie, int pfId) {
		if (liverMsg == null) {
			return;
		}
		IgpWebMsgBean[] msg_list = liverMsg.getMsg_list();
		if (msg_list == null || msg_list.length == 0) {
			return;
		}
		// 补充普通信息
		Set<Long> idSet = new HashSet<Long>();
		for (IgpWebMsgBean msg : msg_list) {
			idSet.add(Long.valueOf(msg.getId()));
			supplementMsg(cookie, pfId, msg);
		}

		// 补充置顶帖
		if (liverMsg.getTop_msg_list() != null && liverMsg.getTop_msg_list().length > 0) {
			long minId = Long.valueOf(msg_list[msg_list.length - 1].getId());
			for (IgpWebMsgBean msg : liverMsg.getTop_msg_list()) {
				long id = Long.valueOf(msg.getId());
				if (id < minId || idSet.contains(id)) {
					continue;
				} else {
					supplementMsg(cookie, pfId, msg);
				}
			}
		}
	}

	/**
	 * 
	 * 补全消息
	 * 
	 * @param cookie
	 * @param pfId
	 * @param msg
	 */
	private void supplementMsg(Cookie cookie, int pfId, IgpWebMsgBean msg) {
		if (Strings.isBlank(msg.getContent())) {
			try {
				String tmpUrl = String.format(Constant.URL_IGP_MSG_LIVER_DETAIL, pfId, msg.getId());
				String mdValue = PropKit.get("md");
				if(Strings.isNotBlank(mdValue)){
					tmpUrl += "&md=" + mdValue;
				}
				Response response = HttpUtil.httpsPost(tmpUrl, null, cookie);
				if (response != null && response.isOK() && Strings.isNotBlank(response.getContent())) {
					String content = response.getContent();
					if(content.contains("<Sitemap>")){
						String flag = "</Sitemap>";
						int end = content.indexOf(flag);
						content = content.substring(end + flag.length(), content.length());
					}
					IgpDetailMsgBean fromJson = Json.fromJson(IgpDetailMsgBean.class, content);
					IgpDetailBean[] show_detail = fromJson.getShow_detail();
					if (show_detail != null && show_detail.length > 0) {
						String detailContent = show_detail[0].getContent();
						msg.setContent(detailContent);
						msg.setContent_new(detailContent);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/***
	 * 初始化老数据
	 * 
	 * @param cookie
	 * @param uid
	 */
	public void initOldMsg(Cookie cookie, int uid) {
		List<Teacher> igpTeacherList = TeacherDao.getIgpTeachers();
		log.debug(Json.toJson(igpTeacherList, JsonFormat.compact()));
		long now = System.currentTimeMillis();
		boolean uidNone = false;
		if (uid == 0) {
			uidNone = true;
		}
		for (Teacher teacher : igpTeacherList) {
			if (teacher.getBuyEndTime() == null || now > teacher.getBuyEndTime()) {
				continue;
			}
			try {
				log.debug(teacher.getName() + " ; getBuyEndTime == " + teacher.getBuyEndTime());
				if (uidNone) {
					uid = 5 + random.nextInt(99999);
				}
				updateMsgByTeacher(cookie, uid, teacher.getPfId(), teacher.getId());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/***
	 * 更新数据
	 * 
	 * @param cookie
	 * @param uid
	 * @param teacherPfId
	 * @throws InterruptedException
	 */
	private void updateMsgByTeacher(Cookie cookie, int uid, int teacherPfId, long teacherId)
			throws InterruptedException {
		long lastTime = findOldest(cookie, uid, teacherPfId, 0, teacherId);
		log.debug("teacherPfId == " + teacherPfId + " ; lastTime == " + lastTime);
		List<Long> timeList = new ArrayList<Long>();
		if (lastTime >= 0) {
			timeList.add(0l);
		}
		while (lastTime > 0) {
			timeList.add(lastTime);
			lastTime = findOldest(cookie, uid, teacherPfId, lastTime, teacherId);
			Thread.sleep(100);
		}
		int size = timeList.size();
		for (int i = size - 1; i >= 0; i--) {
			updateMsgByTeacher(cookie, uid, teacherPfId, timeList.get(i), teacherId);
			Thread.sleep(100);
		}
	}

	/***
	 * 更新数据
	 * 
	 * @param cookie
	 * @param uid
	 * @param teacherPfId
	 * @param lastTime
	 * @return
	 */
	private long findOldest(Cookie cookie, int uid, int teacherPfId, long lastTime, long teacherId) {
		long maxId = IgpcontentDao.getMaxId(teacherId);
		IgpWebMsgBean[] msg_list = getWebMsgArr(cookie, uid, teacherPfId, lastTime);
		if (msg_list != null) {
			log.debug("msg_list size == " + msg_list.length);
			if (msg_list.length == 0) {// 已经找到最后一页
				return 0;
			}
			int firstIndex = msg_list.length - 1;
			// 如果第一个都不符合要求的话，则返回，继续往下寻找
			IgpWebMsgBean firstMsg = msg_list[firstIndex];
			long firstId = Long.valueOf(firstMsg.getId());
			if (firstId > maxId) {
				return firstMsg.getRec_time();
			} else {
				IgpWebMsgBean lastMsg = msg_list[0];
				long lastId = Long.valueOf(lastMsg.getId());
				if (lastId > maxId) {
					return 0;
				}
			}
		}
		return -1;
	}

	/**
	 * 更新数据
	 * 
	 * @param cookie
	 * @param uid
	 * @param teacherPfId
	 * @param lastTime
	 * @param teacherId
	 * @return
	 */
	private long updateMsgByTeacher(Cookie cookie, int uid, int teacherPfId, long lastTime, long teacherId) {
		long maxId = IgpcontentDao.getMaxId(teacherId);
		log.debug("teacherId == " + teacherId);
		IgpWebLiverMsgBean liverMsg = getLiverMsg(cookie, uid, teacherPfId, lastTime);
		List<IgpWebMsgBean> msgList = new ArrayList<IgpWebMsgBean>();
		if (lastTime == 0) {// 处理置顶帖
			IgpWebMsgBean[] top_msg_list = liverMsg.getTop_msg_list();
			if (top_msg_list != null && top_msg_list.length > 0) {
				for (IgpWebMsgBean msg : top_msg_list) {
					long tmpId = Long.valueOf(msg.getId());
					if (tmpId > maxId) {
						// 如果，发送短消息
						ThreadsManager.getInstance().addThread(new SendMessageThread(teacherId, msg, false));
						msgList.add(msg);
					}
				}
			}
		}
		IgpWebMsgBean[] msg_list = liverMsg.getMsg_list();
		if (msg_list != null && msg_list.length > 0) {
			int firstIndex = msg_list.length - 1;
			for (int i = firstIndex; i >= 0; i--) {
				IgpWebMsgBean igpWebMsgBean = msg_list[i];
				long tmpId = Long.valueOf(igpWebMsgBean.getId());
				if (tmpId > maxId) {
					// 如果，发送短消息
					ThreadsManager.getInstance().addThread(new SendMessageThread(teacherId, igpWebMsgBean, false));
					msgList.add(igpWebMsgBean);
					log.info("insert oldMsg : teacherId = " + teacherId + " ; " + igpWebMsgBean.getKind() + " ; "
							+ igpWebMsgBean.getBrief());
				}
			}

		}
		if (msgList.size() > 0) {
			IgpcontentDao.batchInserMsg(teacherId, msgList);
		}
		return 0;
	}

	/**
	 * 或许消息数组
	 * 
	 * @param cookie
	 * @param uid
	 * @param teacherPfId
	 * @param lastTime
	 * @return
	 */
	public IgpWebMsgBean[] getWebMsgArr(Cookie cookie, int uid, int teacherPfId, long lastTime) {
		IgpWebMsgBean[] msg_list = null;
		IgpWebLiverMsgBean liverMsg = getLiverMsg(cookie, uid, teacherPfId, lastTime);
		// 如果获取失败，再次获取一次
		if (liverMsg == null) {
			liverMsg = getLiverMsg(cookie, uid, teacherPfId, lastTime);
		}
		if (liverMsg != null) {
			msg_list = liverMsg.getMsg_list();
		}
		return msg_list;
	}

	/***
	 * 获取cookie
	 * 
	 * @return
	 */
	public Cookie getCookie() {
		Cookie cookie = new Cookie();
		cookie.set("PHPSESSID", "ptor2a08iatnk44sj3bc4j5s32");
		long now = System.currentTimeMillis() / 1000;
		cookie.set("Hm_lvt_97a2c934daaed931f060269d1a9310de", String.valueOf(now));
		cookie.set("Hm_lpvt_97a2c934daaed931f060269d1a9310de", String.valueOf(now + 20 * 60));
		return cookie;
	}

	/**
	 * 获取某个老师的所有会员
	 * 
	 * @param teacherPfId
	 * @return
	 */
	public List<Integer> getAllUsers(int teacherPfId) {
		Cookie cookie = getCookie();
		long vipInfoBeginIndex = getVipInfoBeginIndex(cookie, 0, teacherPfId);
		List<Integer> userList = new ArrayList<Integer>();
		for (int i = 1; i < 300000; i++) {
			IgpWebMsgBean[] webMsgArr = getWebMsgArr(cookie, i, teacherPfId, vipInfoBeginIndex);
			if (webMsgArr != null) {
				for (IgpWebMsgBean webMsg : webMsgArr) {
					if (webMsg.getKind().equals(EIgpKind.VIP.getValue())) {
						if (Strings.isNotBlank(webMsg.getContent_new())) {
							userList.add(i);
						}
						break;
					}
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (i % 50 == 0) {
				log.debug("===i===" + i + " ; userList = " + Json.toJson(userList, JsonFormat.compact()));
			}
		}
		log.debugf("teacherPfId == %d,user size == %d", teacherPfId, userList.size());
		return userList;
	}

	/***
	 * 从所有用户中获取vip的用户
	 * 
	 * @param teacherPfId
	 * @return
	 */
	public int getUidFromAll(int teacherPfId) {
		Cookie cookie = getCookie();
		int uid = 0;
		long vipInfoBeginIndex = getVipInfoBeginIndex(cookie, uid, teacherPfId);
		if (vipInfoBeginIndex < 0) {
			log.infof("don't has user == %d,vipInfoBeginIndex == %d", teacherPfId, vipInfoBeginIndex);
			return uid;
		}
		for (int i = 1; i < 300000; i++) {
			IgpWebMsgBean[] webMsgArr = getWebMsgArr(cookie, i, teacherPfId, vipInfoBeginIndex);
			if (webMsgArr != null) {
				for (IgpWebMsgBean webMsg : webMsgArr) {
					if (webMsg.getKind().equals(EIgpKind.VIP.getValue())) {
						if (Strings.isNotBlank(webMsg.getContent())) {
							uid = i;
						}
						break;
					}
				}
				if (uid > 0) {
					break;
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			log.debugf("teacherPfId == %d,uid == %d", teacherPfId, i);
		}
		log.infof("getUidFromAll teacherPfId == %d,uid == %d", teacherPfId, uid);
		return uid;
	}

	public boolean isVipUser(int teacherPfId, int uid) {
		boolean isVipUser = false;
		if (uid == 0) {
			return false;
		}
		Cookie cookie = getCookie();
		long vipInfoBeginIndex = getVipInfoBeginIndex(cookie, uid, teacherPfId);
		if (vipInfoBeginIndex < 0) {
			log.infof("don't has user == %d,vipInfoBeginIndex == %d", teacherPfId, vipInfoBeginIndex);
			return false;
		}
		IgpWebMsgBean[] webMsgArr = getWebMsgArr(cookie, uid, teacherPfId, vipInfoBeginIndex);
		if (webMsgArr != null) {
			for (IgpWebMsgBean webMsg : webMsgArr) {
				if (webMsg.getKind().equals(EIgpKind.VIP.getValue())) {
					if (Strings.isNotBlank(webMsg.getContent_new())) {
						isVipUser = true;
					}
					break;
				}
			}
		}
		log.infof("isVipUser teacherPfId == %d,uid == %d,isVipUser == %b", teacherPfId, uid, isVipUser);
		return isVipUser;
	}

	/***
	 * 获取有vip信息开始id
	 * 
	 * @param cookie
	 * @param uid
	 * @param teacherPfId
	 * @return
	 */
	private long getVipInfoBeginIndex(Cookie cookie, int uid, int teacherPfId) {
		// 获取有vip的列表的开始id
		long index = 0l;
		boolean isFind = false;
		while (!isFind) {
			IgpWebMsgBean[] webMsgArr = IgpMsgFactory.getInstance().getWebMsgArr(cookie, 0, teacherPfId, index);
			if (webMsgArr != null && webMsgArr.length > 0) {
				for (IgpWebMsgBean msg : webMsgArr) {
					if (msg.getKind().equals(EIgpKind.VIP.getValue())) {
						isFind = true;
						break;
					}
				}
				if (!isFind) {
					index = Long.valueOf(webMsgArr[webMsgArr.length - 1].getId());
				}
			} else {// 已经到最后一个了
				break;
			}
		}
		if (!isFind) {
			index = -1;
		}
		return index;
	}

	public static void main(String[] args) {
		Cookie cookie = IgpMsgFactory.getInstance().getCookie();
		// IgpWebMsgBean[] webMsgArr =
		// IgpMsgFactory.getInstance().getWebMsgArr(cookie, 5, 538, 0);
		// System.out.println(Json.toJson(webMsgArr));

		// int uidFromAll =
		// IgpMsgFactory.getInstance().getUidFromAll(EIgpTeacher.daofeng.getValue());
		// System.out.println(uidFromAll);

		// List<Integer> allUsers = IgpMsgFactory.getInstance().getAllUsers(2);
		// System.out.println(EIgpTeacher.daofeng.getValue() + " == size == " +
		// allUsers.size());

		// String url =
		// "https://www.5igupiao.com/api/live.php?act=load_detail&id=91&oid=742411&soc&source=pc";
		// Response response = HttpUtil.httpsPost(url, null, cookie);
		// IgpDetailMsgBean fromJson = Json.fromJson(IgpDetailMsgBean.class,
		// response.getContent());
		// IgpDetailBean[] show_detail = fromJson.getShow_detail();
		// String content = show_detail[0].getContent();
		// System.out.println(content);

		// try {
		// String tmpUrl = String.format(Constant.URL_IGP_MSG_LIVER_DETAIL, 538,
		// 759340);
		// Response response = HttpUtil.httpsPost(tmpUrl, null, cookie);
		// if (response != null && response.isOK() &&
		// Strings.isNotBlank(response.getContent())) {
		// IgpDetailMsgBean fromJson = Json.fromJson(IgpDetailMsgBean.class,
		// response.getContent());
		// System.out.println(Json.toJson(fromJson));
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

//		int uidFromAll = IgpMsgFactory.getInstance().getUidFromAll(585);
//		System.out.println(uidFromAll);
		
		String content = "<Sitemap>123</Sitemap>122311";
		if(content.contains("<Sitemap>")){
			String flag = "</Sitemap>";
			int end = content.indexOf(flag);
			content = content.substring(end + flag.length(), content.length());
		}
		System.out.println(content);
	}

}
