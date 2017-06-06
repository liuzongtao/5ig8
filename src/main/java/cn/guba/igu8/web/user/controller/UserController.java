/**
 * 
 */
package cn.guba.igu8.web.user.controller;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;

import cn.guba.igu8.core.constants.ControllerConstant;
import cn.guba.igu8.core.utils.IpUtil;
import cn.guba.igu8.db.mysqlModel.User;
import cn.guba.igu8.web.user.manager.UserSessionManager;
import cn.guba.igu8.web.user.service.UserService;
import cn.guba.igu8.web.user.validator.LoginValidator;

/**
 * @author zongtao liu
 *
 */
public class UserController extends Controller {

	@Clear
	public void toLogin() {
		render("login.html");
	}

	@Clear
	@Before(LoginValidator.class)
	public void doLogin() {
		String nickName = getPara("name");
		String pwd = getPara("pwd");
		User user = UserService.getInstance().getUser(nickName, pwd);
		if (user == null) {
			redirect("/user/login");
		}
		// 添加ip和uid的对应关系
		HttpServletRequest request = getRequest();
		String requestIp = IpUtil.getUserIp(request);
		long uid = user.getId();
		UserSessionManager.getInstance().add(uid, requestIp);
		// 将uid的放入session中
		setSessionAttr(ControllerConstant.SESSION_NAME_USER, user);
		// 特殊处理重定位地址
		String referer = getSessionAttr(ControllerConstant.SESSION_NAME_BACKURL);
		if (referer == null || referer.equals("user/doLogin")) {
			referer = "/";
		}
		removeSessionAttr(ControllerConstant.SESSION_NAME_BACKURL);
		redirect(referer);
	}

	public void doExit() {
		User user = getSessionAttr(ControllerConstant.SESSION_NAME_USER);
		if (user != null) {
			HttpServletRequest request = getRequest();
			String requestIp = IpUtil.getUserIp(request);
			UserSessionManager.getInstance().remove(user.getId(), requestIp);
			// 从session中删除
			removeSessionAttr(ControllerConstant.SESSION_NAME_USER);
		}
		// 从session中删除
		removeSessionAttr(ControllerConstant.SESSION_NAME_BACKURL);
		render("login.html");
	}

	public void toUpdatePwd() {

	}

}
