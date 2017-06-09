/**
 * 
 */
package cn.guba.igu8.processor.igupiaoWeb.msg;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.nutz.http.Cookie;
import org.nutz.http.Response;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import cn.guba.igu8.core.constants.Constant;
import cn.guba.igu8.core.utils.HttpUtil;
import cn.guba.igu8.db.dao.IgpcontentDao;
import cn.guba.igu8.db.dao.TeacherDao;
import cn.guba.igu8.db.mysqlModel.Teacher;
import cn.guba.igu8.processor.igupiaoWeb.msg.beans.EIgpKind;
import cn.guba.igu8.processor.igupiaoWeb.msg.beans.IgpWebLiverMsgBean;
import cn.guba.igu8.processor.igupiaoWeb.msg.beans.IgpWebMsgBean;
import cn.guba.igu8.processor.igupiaoWeb.threads.SendMessageThread;
import cn.guba.igu8.processor.igupiaoWeb.threads.ThreadsManager;
import cn.guba.igu8.web.teacher.beans.EIgpTeacher;

/**
 * @author zongtao liu
 *
 */
public class IgpMsgFactory {

	private static Log log = Logs.get();

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
		List<Teacher> igpTeacherList = TeacherDao.getIgpTeacherList();
		long now = System.currentTimeMillis();
		for (Teacher teacher : igpTeacherList) {
			if (now > teacher.getBuyEndTime()) {
				continue;
			}
			if (uid == 0) {
				uid = teacher.getPfVipUid();
			}
			int pfId = teacher.getPfId();
			long tearcherId = teacher.getId();
			IgpWebLiverMsgBean liverMsg = getLiverMsg(cookie, uid, pfId);
			// 如果获取失败，再次获取一次
			if (liverMsg == null) {
				liverMsg = getLiverMsg(cookie, uid, pfId);
			}
			if (liverMsg != null) {
				IgpAceInfo aceInfo = null;
				if (aceInfoMap.containsKey(tearcherId)) {
					aceInfo = aceInfoMap.get(tearcherId);
				} else {
					aceInfo = new IgpAceInfo(tearcherId);
					aceInfoMap.put(tearcherId, aceInfo);
				}
				aceInfo.addMsg(liverMsg.getMsg_list());
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
				liverMsg = Json.fromJson(IgpWebLiverMsgBean.class, response.getContent());
			}
		} catch (Exception e) {
			log.error("***********" + new Date() + "***********");
			e.printStackTrace();
		}
		return liverMsg;
	}

	/***
	 * 初始化老数据
	 * 
	 * @param cookie
	 * @param uid
	 */
	public void initOldMsg(Cookie cookie, int uid) {
		List<Teacher> igpTeacherList = TeacherDao.getIgpTeacherList();
		long now = System.currentTimeMillis();
		for (Teacher teacher : igpTeacherList) {
			if (teacher.getBuyEndTime() == null || now > teacher.getBuyEndTime()) {
				continue;
			}
			try {
				if (uid == 0) {
					uid = teacher.getPfVipUid();
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
		IgpWebMsgBean[] msg_list = getWebMsgArr(cookie, uid, teacherPfId, lastTime);
		if (msg_list != null && msg_list.length > 0) {
			List<IgpWebMsgBean> msgList = new ArrayList<IgpWebMsgBean>();
			int firstIndex = msg_list.length - 1;
			for (int i = firstIndex; i >= 0; i--) {
				IgpWebMsgBean igpWebMsgBean = msg_list[i];
				long tmpId = Long.valueOf(igpWebMsgBean.getId());
				if (tmpId > maxId) {
					// 如果，发送短消息
					ThreadsManager.getInstance().addThread(new SendMessageThread(teacherId, igpWebMsgBean, false));
					msgList.add(igpWebMsgBean);
					log.info("find can insert !");
				}
			}
			if (msgList.size() > 0) {
				IgpcontentDao.batchInserMsg(teacherId, msgList);
			}
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
			System.out.println("===i===" + i + " ; size = " + userList.size());
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
						if (Strings.isNotBlank(webMsg.getContent_new())) {
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
		EIgpTeacher[] values = EIgpTeacher.values();
		for (EIgpTeacher teacher : values) {
			// int uid =
			// IgpMsgFactory.getInstance().getUidFromAll(teacher.getValue());
			// System.out.println(teacher.getName() + " == " + uid);
			// List<Integer> allUsers =
			// IgpMsgFactory.getInstance().getAllUsers(teacher.getValue());
			// System.out.println(teacher.getName() + " == size == " +
			// allUsers.size());
		}

		List<Integer> allUsers = IgpMsgFactory.getInstance().getAllUsers(2);
		System.out.println(EIgpTeacher.daofeng.getValue() + " == size == " + allUsers.size());
	}

}
