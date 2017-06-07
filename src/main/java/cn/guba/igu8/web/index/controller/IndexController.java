package cn.guba.igu8.web.index.controller;

import java.util.List;

import com.jfinal.core.Controller;

import cn.guba.igu8.core.constants.ControllerConstant;
import cn.guba.igu8.db.mysqlModel.EovaMenu;
import cn.guba.igu8.db.mysqlModel.User;
import cn.guba.igu8.web.index.beans.TreeInfoBean;
import cn.guba.igu8.web.index.service.EovaMenuService;

/**
 * @author zongtao liu
 *
 */
public class IndexController extends Controller {

	public void index() {
		List<EovaMenu> allEovaMenu = EovaMenuService.getInstance().getEovaMenuRoot();
		setAttr("rootList", allEovaMenu);
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
	
	public void showTree(){
		int parentId = getParaToInt(0);
		List<TreeInfoBean> treeInfoList = EovaMenuService.getInstance().getTreeInfoList(parentId);
		renderJson(treeInfoList);
	}

}
