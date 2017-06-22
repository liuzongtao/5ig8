/**
 * 
 */
package cn.guba.igu8.web.user.service;

import java.util.ArrayList;
import java.util.List;

import org.nutz.lang.Strings;

import com.jfinal.plugin.activerecord.Page;

import cn.guba.igu8.core.constants.Constant;
import cn.guba.igu8.core.utils.Util;
import cn.guba.igu8.db.dao.UserDao;
import cn.guba.igu8.db.mysqlModel.User;
import cn.guba.igu8.db.mysqlModel.Uservipinfo;
import cn.guba.igu8.web.teacher.service.TeacherService;
import cn.guba.igu8.web.user.beans.UserViewInfoBean;
import cn.guba.igu8.web.vip.service.VipService;

/**
 * @author zongtao liu
 *
 */
public class UserService {

	private static volatile UserService userService;

	private UserService() {
	}

	public static UserService getInstance() {
		if (userService == null) {
			synchronized (UserService.class) {
				if (userService == null) {
					userService = new UserService();
				}
			}
		}
		return userService;
	}

	public User getUser(String nickName, String pwd) {
		return UserDao.getUser(nickName, pwd);
	}

	public User getUser(String nickName) {
		return UserDao.getUser(nickName);
	}

	public User getUserByPhone(long phone) {
		return UserDao.getUserByPhone(phone);
	}

	public User getUserByEmail(String email) {
		return UserDao.getUserByEmail(email);
	}

	public User getUser(long uid) {
		return UserDao.getUser(uid);
	}

	public List<User> getAllUser() {
		return UserDao.getAllUser();
	}

	public UserViewInfoBean getUserViewInfo(long uid) {
		User user = getUser(uid);
		if (user == null) {
			return null;
		}
		return getUserViewInfoBean(user);
	}

	/**
	 * 删除用户
	 * 
	 * @param uid
	 */
	public void delUser(long uid) {
		User user = getUser(uid);
		if (user == null) {
			return;
		}
		UserDao.delete(user);
		// 删除相关的vip信息
		VipService.getInstance().delByUid(uid);
	}

	/**
	 * 获取分页列表
	 * 
	 * @param pageNumber
	 * @return
	 */
	public Page<UserViewInfoBean> getPageList(int pageNumber) {
		int pageSize = 10;
		Page<User> userPage = UserDao.paginate(pageNumber, pageSize);
		List<UserViewInfoBean> list = new ArrayList<UserViewInfoBean>();
		for (User user : userPage.getList()) {
			UserViewInfoBean info = getUserViewInfoBean(user);
			list.add(info);
		}
		return new Page<UserViewInfoBean>(list, userPage.getPageNumber(), userPage.getPageSize(),
				userPage.getTotalPage(), userPage.getTotalRow());
	}

	/**
	 * 获取用户显示信息
	 * 
	 * @param user
	 * @return
	 */
	private UserViewInfoBean getUserViewInfoBean(User user) {
		UserViewInfoBean info = new UserViewInfoBean();
		info.setId(user.getId());
		String createTime = Util.dateformat(user.getCreatTime());
		info.setCreateTime(createTime);
		info.setNickname(user.getNickname());
		info.setPhoneNumber(String.valueOf(user.getPhoneNumber()));
		info.setEmail(user.getEmail());
		// 设置邀请人信息
		if (user.getInviterUid() != null) {
			long inviterUid = user.getInviterUid();
			User inviterUser = getUser(inviterUid);
			if (inviterUser != null) {
				info.setInviterNickname(inviterUser.getNickname());
			}
		}
		info.setVipInfo(getVipInfoStr(user.getId()));
		info.setRoleType(user.getRoleType());
		return info;
	}

	/**
	 * 获取vip信息
	 * 
	 * @param uid
	 * @return
	 */
	private String getVipInfoStr(long uid) {
		List<Uservipinfo> vipList = VipService.getInstance().getVipList(uid);
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for (Uservipinfo info : vipList) {
			if (index >= 2) {
				sb.append("...");
				break;
			}
			Long teacherId = info.getConcernedTeacherId();
			String teacherName = TeacherService.getInstance().getTeacherName(teacherId);
			sb.append(teacherName).append(" ");
			index++;
		}
		return sb.toString();
	}

	public void addUser(UserViewInfoBean userInfo) {
		User user = new User();
		user.setNickname(userInfo.getNickname());
		user.setCreatTime(System.currentTimeMillis());
		user.setPasswd(Constant.USER_PWD_INIT);
		user.setEmail(userInfo.getEmail());
		if (Strings.isNotBlank(userInfo.getPhoneNumber())) {
			user.setPhoneNumber(Long.valueOf(userInfo.getPhoneNumber()));
		}
		String inviter = userInfo.getInviter();
		if (Strings.isNotBlank(inviter)) {
			User inviterUser = null;
			if (Strings.isEmail(inviter)) {
				inviterUser = getUserByEmail(inviter);
			} else if (Strings.isMobile(inviter)) {
				inviterUser = getUserByPhone(Long.valueOf(inviter));
			} else {
				inviterUser = getUser(inviter);
			}
			if (inviterUser != null) {
				long inviterUid = inviterUser.getId();
				user.setInviterUid(inviterUid);
			}
		}
		user.setRoleType(userInfo.getRoleType());
		UserDao.saveUser(user);
	}

	public void updateUser(UserViewInfoBean userInfo) {
		User user = getUser(userInfo.getId());
		user.setNickname(userInfo.getNickname());
		user.setEmail(userInfo.getEmail());
		if (Strings.isNotBlank(userInfo.getPhoneNumber())) {
			user.setPhoneNumber(Long.valueOf(userInfo.getPhoneNumber()));
		}
		user.setRoleType(userInfo.getRoleType());
		UserDao.updateUser(user);
	}

}
