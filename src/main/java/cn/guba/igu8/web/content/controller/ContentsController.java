/**
 * 
 */
package cn.guba.igu8.web.content.controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

import cn.guba.igu8.web.content.beans.IgpWebDetailBean;
import cn.guba.igu8.web.content.service.ContentsService;
import cn.guba.igu8.web.teacher.service.TeacherService;

/**
 * @author zongtao liu
 *
 */
public class ContentsController extends Controller {

	public void index() {
		Long teacherId = getParaToLong();
		if (teacherId == null) {
			setAttr("msg", new IgpWebDetailBean());
		} else {
			IgpWebDetailBean igpWebDetailModel = ContentsService.getInstance().getNewestDetail(teacherId);
			setAttr("teacherName", igpWebDetailModel.getName());
			setAttr("msg", igpWebDetailModel);
		}
		render("detail.html");
	}

	public void list() {
		Long teacherId = getParaToLong("tid", 13l);
		int pageNumber = getParaToInt("p", 1);
		Page<IgpWebDetailBean> detailListPage = ContentsService.getInstance().getDetailPageList(teacherId, pageNumber);
		String teacherName = TeacherService.getInstance().getTeacherName(teacherId);
		setAttr("detailListPage", detailListPage);
		setAttr("teacherName", teacherName);
		setAttr("tid", teacherId);
		render("list.html");
	}

}
