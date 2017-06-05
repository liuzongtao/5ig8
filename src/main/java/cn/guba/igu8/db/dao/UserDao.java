/**
 * 
 */
package cn.guba.igu8.db.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.lang.Lang;

import cn.guba.igu8.db.mysqlModel.User;

/**
 * @author zongtao liu
 *
 */
public class UserDao {

	static Map<Long, User> userMap = new HashMap<Long, User>();

	/***
	 * 获取user
	 * 
	 * @param uid
	 * @return
	 */
	public static User getUser(long uid) {
		updateUserMap();
		User user = null;
		if (userMap.containsKey(uid)) {
			user = userMap.get(uid);
		}
		return user;
	}

	private static void updateUserMap() {
		if (userMap.size() == 0) {
			List<User> userList = User.dao.find("select * from user");
			if (userList != null) {
				for (User user : userList) {
					userMap.put(user.getId(), user);
				}
			}
		}
	}

	public static User getUser(String nickName) {
		updateUserMap();
		User user = null;
		for (User tmpUser : userMap.values()) {
			if (tmpUser.getNickname().equals(nickName)) {
				user = tmpUser;
			}
		}
		return user;
	}
	
	/***
	 * 查询用户
	 * @param nickName
	 * @param pwd
	 * @return
	 */
	public static User getUser(String nickName,String pwd) {
		updateUserMap();
		User user = null;
		String pwdDb = Lang.md5(pwd);
		for (User tmpUser : userMap.values()) {
			if (tmpUser.getNickname().equals(nickName) && tmpUser.getPasswd().equals(pwdDb)) {
				user = tmpUser;
			}
		}
		return user;
	}

}
