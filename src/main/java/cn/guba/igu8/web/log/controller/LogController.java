/**
 * 
 */
package cn.guba.igu8.web.log.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

import cn.guba.igu8.web.log.beans.RechargeLogViewBean;
import cn.guba.igu8.web.recharge.service.RechargeService;
import cn.guba.igu8.web.user.validator.AdminValidator;

/**
 * @author zongtao liu
 *
 */
@Before(AdminValidator.class)
public class LogController extends Controller{
	
	public void list4Recharge() {
		int pageNumber = getParaToInt("p", 1);
		long uid = getParaToLong("uid", 0l);
		Page<RechargeLogViewBean> rechargelogsPage = RechargeService.getInstance().getPageList(uid, pageNumber);
		setAttr("rechargelogsPage", rechargelogsPage);
		setAttr("uid", uid);
		render("list.html");
	}

}
