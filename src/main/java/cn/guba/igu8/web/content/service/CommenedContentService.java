/**
 * 
 */
package cn.guba.igu8.web.content.service;

import cn.guba.igu8.db.dao.IgpcontentDao;
import cn.guba.igu8.db.dao.TeacherDao;
import cn.guba.igu8.db.mysqlModel.Igpcontent;
import cn.guba.igu8.db.mysqlModel.Teacher;
import cn.guba.igu8.web.content.beans.IgpWebDetailBean;

/**
 * @author zongtao liu
 *
 */
public class CommenedContentService {
	
	private static volatile CommenedContentService commenedContentService;
	
	private CommenedContentService(){}
	
	public static CommenedContentService getInstance(){
		if(commenedContentService == null){
			synchronized(CommenedContentService.class){
				if(commenedContentService == null){
					commenedContentService = new CommenedContentService();
				}
			}
		}
		return commenedContentService;
	}

	public IgpWebDetailBean getIgpWebDetailBean(long teacherId, long id) {
		IgpWebDetailBean msg = new IgpWebDetailBean();
		Igpcontent content = IgpcontentDao.getIgpcontent(teacherId, id);
		if (content != null ) {
			long now = System.currentTimeMillis() / 1000;
			Long recTime = content.getRecTime();
			// 48小时前的信息需要清除
			if (now <= (recTime + 60 * 60 * 48)) {
				Teacher teacher = TeacherDao.getTeacher(teacherId);
				msg.setName(teacher.getName());
				msg.setTimeDesc(content.getRecTimeDesc());
				//设置消息
				msg.setDetail(ContentsService.getInstance().getContentDetail(content));
			}
		}
		return msg;
	}
	
	

}
