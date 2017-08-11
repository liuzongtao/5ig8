/**
 * 
 */
package cn.guba.igu8.db.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.lang.Lang;
import org.nutz.lang.Strings;

import com.jfinal.plugin.activerecord.Page;

import cn.guba.igu8.core.init.SysInit;
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
	
	/**
	 * 获取管理员账户
	 * @return
	 */
	public static User getAdmin(){
		return getUser(SysInit.adminNickname);
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
			if (Strings.equals(tmpUser.getNickname(), nickName)) {
				user = tmpUser;
			}
		}
		return user;
	}

	public static User getUserByPhone(long phone) {
		updateUserMap();
		User user = null;
		for (User tmpUser : userMap.values()) {
			if(tmpUser.getPhoneNumber() == null){
				continue;
			}
			if (tmpUser.getPhoneNumber() == phone) {
				user = tmpUser;
			}
		}
		return user;
	}

	public static User getUserByEmail(String email) {
		updateUserMap();
		User user = null;
		for (User tmpUser : userMap.values()) {
			if (Strings.equals(String.valueOf(tmpUser.getEmail()), email)) {
				user = tmpUser;
			}
		}
		return user;
	}

	/***
	 * 查询用户
	 * 
	 * @param nickName
	 * @param pwd
	 * @return
	 */
	public static User getUser(String nickName, String pwd) {
		updateUserMap();
		User user = null;
		String pwdDb = Lang.md5(pwd);
		for (User tmpUser : userMap.values()) {
			if (Strings.equals(tmpUser.getNickname(), nickName) && Strings.equals(tmpUser.getPasswd(), pwdDb)) {
				user = tmpUser;
			}
		}
		return user;
	}

	/**
	 * 获取所有用户
	 * 
	 * @return
	 */
	public static List<User> getAllUser() {
		updateUserMap();
		List<User> list = new ArrayList<User>(userMap.values());
		Collections.sort(list, new Comparator<User>() {
			@Override
			public int compare(User o1, User o2) {
				return Long.compare(o1.getId(), o2.getId());
			}
		});
		return list;
	}

	public static Page<User> paginate(int pageNumber, int pageSize) {
		return User.dao.paginate(pageNumber, pageSize, "select *", "from user order by id ");
	}
	
	public static void saveUser(User user){
		user.setPasswd(Lang.md5(user.getPasswd()));
		user.save();
		userMap.put(user.getId(), user);
	}
	
	public static void updateUser(User user){
		userMap.put(user.getId(), user);
		user.update();
	}
	
	public static void delete(User user){
		long uid = user.getId();
		userMap.remove(uid);
		user.delete();
	}

}
