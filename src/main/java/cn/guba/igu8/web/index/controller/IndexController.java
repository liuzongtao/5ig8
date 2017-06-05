package cn.guba.igu8.web.index.controller;

import com.jfinal.core.Controller;

import cn.guba.igu8.core.constants.ControllerConstant;
import cn.guba.igu8.db.mysqlModel.User;

/**
 * @author zongtao liu
 *
 */
public class IndexController extends Controller {

	public void index() {
		render("index.html");
	}
	
	public void footer() {
		render("footer.html");
	}

	public void toHeader() {
		User user = getSessionAttr(ControllerConstant.SESSION_NAME_USER);
		setAttr(ControllerConstant.SESSION_NAME_USER, user);
		render("header.html");
	}
	
	public void toMain() {
		render("main.html");
	}

}
