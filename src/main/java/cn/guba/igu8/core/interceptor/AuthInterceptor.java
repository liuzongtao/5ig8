package cn.guba.igu8.core.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.nutz.lang.Strings;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

import cn.guba.igu8.core.constants.ControllerConstant;
import cn.guba.igu8.core.utils.IpUtil;
import cn.guba.igu8.db.mysqlModel.User;
import cn.guba.igu8.web.user.manager.UserSessionManager;
import cn.guba.igu8.web.user.service.UserService;

/**
 * @author zongtao liu
 *
 */
public class AuthInterceptor implements Interceptor {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jfinal.aop.Interceptor#intercept(com.jfinal.aop.Invocation)
	 */
	@Override
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		User user = controller.getSessionAttr(ControllerConstant.SESSION_NAME_USER);
		if (user == null) {
			// 从user的session管理器中根据ip来获取uid
			HttpServletRequest request = controller.getRequest();
			String requestIp = IpUtil.getUserIp(request);
			long memUid = UserSessionManager.getInstance().getUid(requestIp);
			if (memUid != 0) {
				user = UserService.getInstance().getUser(memUid);
				controller.setSessionAttr(ControllerConstant.SESSION_NAME_USER, user);
				inv.invoke();
			} else {
				String backUrl = controller.getSessionAttr(ControllerConstant.SESSION_NAME_BACKURL);
				if (Strings.isBlank(backUrl)) {
					backUrl = getUrlAndPara(request);
					controller.setSessionAttr(ControllerConstant.SESSION_NAME_BACKURL, backUrl);
				}
				controller.redirect("/user/toLogin");
			}
		} else {
			inv.invoke();
		}
	}

	private String getUrlAndPara(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		String uri = request.getRequestURI();
		sb.append(uri).append("?");
		Enumeration<String> names = request.getParameterNames();
		int index = 0;
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			String value = request.getParameter(name);
			if (index != 0) {
				sb.append("&");
			}
			sb.append(name).append("=").append(value);
			index++;
		}
		return sb.toString();
	}

}
