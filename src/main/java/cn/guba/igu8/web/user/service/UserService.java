/**
 * 
 */
package cn.guba.igu8.web.user.service;

import cn.guba.igu8.db.dao.UserDao;
import cn.guba.igu8.db.mysqlModel.User;

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
	
	public User getUser(String nickName,String pwd){
		return UserDao.getUser(nickName, pwd);
	}
	
	public User getUser(long uid){
		return UserDao.getUser(uid);
	}

}
