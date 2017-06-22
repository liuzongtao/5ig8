/**
 * 
 */
package cn.guba.igu8.web.vip.controller;

import java.util.List;

import org.nutz.lang.Strings;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

import cn.guba.igu8.web.content.beans.KindViewBean;
import cn.guba.igu8.web.content.service.ContentsService;
import cn.guba.igu8.web.teacher.beans.TeacherViewInfoBean;
import cn.guba.igu8.web.teacher.service.TeacherService;
import cn.guba.igu8.web.user.validator.AdminValidator;
import cn.guba.igu8.web.vip.beans.UserVipViewBean;
import cn.guba.igu8.web.vip.beans.VipTypeViewBean;
import cn.guba.igu8.web.vip.service.VipService;

/**
 * @author zongtao liu
 *
 */
@Before(AdminValidator.class)
public class VipController extends Controller {

	public void list() {
		String uidStr = getPara();
		int pageNumber = getParaToInt("p", 1);
		if (Strings.isBlank(uidStr) && !Strings.isNumber(uidStr)) {
			setAttr("errorMsg", "uid错误");
			render("/err/520.html");
			return;
		}
		long uid = Long.valueOf(uidStr);
		Page<UserVipViewBean> vipInfosPage = VipService.getInstance().getPageList(uid, pageNumber);
		setAttr("vipInfosPage", vipInfosPage);
		setAttr("uid", uid);
		render("list.html");
	}

	public void toAdd() {
		String uidStr = getPara();
		if (Strings.isBlank(uidStr) && !Strings.isNumber(uidStr)) {
			setAttr("errorMsg", "uid错误");
			render("/err/520.html");
			return;
		}
		long uid = Long.valueOf(uidStr);
		List<KindViewBean> kindList = ContentsService.getInstance().getKindList();
		setAttr("kinds", kindList);
		List<VipTypeViewBean> typeList = VipService.getInstance().getVipTypeList();
		setAttr("types", typeList);
		List<TeacherViewInfoBean> canAddTeachers = TeacherService.getInstance().getCanAddTeacher(uid);
		setAttr("teachers", canAddTeachers);
		UserVipViewBean userVipViewBean = new UserVipViewBean();
		userVipViewBean.setUid(uid);
		setAttr("userVipViewBean", userVipViewBean);
		render("detail.html");
	}

	public void save() {
		UserVipViewBean userVipViewBean = getBean(UserVipViewBean.class);
		String[] emailTypeArr = getParaValues("emailTypeArr");
		long id = userVipViewBean.getId();
		if (id == 0) {
			VipService.getInstance().addVipInfo(userVipViewBean, emailTypeArr);
		} else {
			VipService.getInstance().updateVipInfo(userVipViewBean, emailTypeArr);
		}
		redirect("/vip/list/" + userVipViewBean.getUid());
	}

	public void delete() {
		String idStr = getPara("id");
		if (Strings.isBlank(idStr) && !Strings.isNumber(idStr)) {
			setAttr("errorMsg", "id错误");
			render("/err/520.html");
			return;
		}
		long id = Long.valueOf(idStr);
		String uidStr = getPara("uid");
		if (Strings.isBlank(idStr) && !Strings.isNumber(uidStr)) {
			setAttr("errorMsg", "uid错误");
			render("/err/520.html");
			return;
		}
		long uid = Long.valueOf(uidStr);
		VipService.getInstance().delById(id);
		redirect("/vip/list/" + uid);
	}

	public void toEdit() {
		String idStr = getPara();
		if (Strings.isBlank(idStr) && !Strings.isNumber(idStr)) {
			setAttr("errorMsg", "id错误");
			render("/err/520.html");
			return;
		}
		long id = Long.valueOf(idStr);
		UserVipViewBean userVipViewBean = VipService.getInstance().getUserVipViewBeanById(id);
		userVipViewBean.setPeriodNum(0);
		setAttr("userVipViewBean", userVipViewBean);
		List<KindViewBean> kindList = ContentsService.getInstance().getKindList();
		setAttr("kinds", kindList);
		List<VipTypeViewBean> typeList = VipService.getInstance().getVipTypeList();
		setAttr("types", typeList);
		List<TeacherViewInfoBean> canAddTeachers = TeacherService.getInstance()
				.getCanUpdateTeacher(userVipViewBean.getUid(), userVipViewBean.getTeacherId());
		setAttr("teachers", canAddTeachers);
		String emailType = userVipViewBean.getEmailType();
		String[] emailTypeArr = emailType.split(",");
		setAttr("emailTypeArr", emailTypeArr);
		render("detail.html");
	}

}
