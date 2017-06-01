/**
 * 
 */
package cn.guba.igu8.web.content.controller;

import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Strings;

import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;

import cn.guba.igu8.core.constants.Constant;
import cn.guba.igu8.core.utils.DESUtil;
import cn.guba.igu8.web.content.beans.IgpWebDetailBean;
import cn.guba.igu8.web.content.beans.ParaInfoBean;
import cn.guba.igu8.web.content.service.CommenedContentService;

/**
 * @author zongtao liu
 *
 */
@Clear
public class CommenedContentController extends Controller {

	public void index() {
		try {
			String para = getPara("p");
			ParaInfoBean paraInfo = null;
			if (Strings.isNotBlank(para)) {
				String infoStr = DESUtil.decrypt(para, Constant.KEY_PARA_DES);
				paraInfo = Json.fromJson(ParaInfoBean.class, infoStr);
				long teacherId = paraInfo.getTeacherId();
				long id = paraInfo.getId();

				IgpWebDetailBean detailMsg = CommenedContentService.getInstance().getIgpWebDetailBean(teacherId, id);
				setAttr("teacherName", detailMsg.getName());
				setAttr("msg", detailMsg);
				if(detailMsg == null || detailMsg.getTimeDesc() == null ){
					setAttr("msgInfo", "消息已过期，请登录查看!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		render("detail.html");
	}

	public static void main(String[] args) {

		ParaInfoBean paraInfoBean = new ParaInfoBean();
		paraInfoBean.setId(732083);
		paraInfoBean.setTeacherId(12);
		String paraInfoStr = Json.toJson(paraInfoBean, JsonFormat.compact());
		try {
			String encodeStr = DESUtil.encrypt(paraInfoStr, Constant.KEY_PARA_DES);
			// eyJ0eXBlIjoxLCJpZCI6MTAyNDV9
			// lmgYoLmFZ9v9jZC7WGpf3Yh2aCHm2k4u
			System.out.println(encodeStr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
