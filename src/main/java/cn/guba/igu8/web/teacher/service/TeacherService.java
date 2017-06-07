/**
 * 
 */
package cn.guba.igu8.web.teacher.service;

import cn.guba.igu8.db.dao.TeacherDao;
import cn.guba.igu8.db.mysqlModel.Teacher;
import cn.guba.igu8.processor.igupiaoWeb.msg.IgpMsgFactory;

/**
 * @author zongtao liu
 *
 */
public class TeacherService {

	private static volatile TeacherService teacherService;

	private TeacherService() {
	}

	public static TeacherService getInstance() {
		if (teacherService == null) {
			synchronized (TeacherService.class) {
				if (teacherService == null) {
					teacherService = new TeacherService();
				}
			}
		}
		return teacherService;
	}

	/***
	 * 获取老师名字
	 * 
	 * @param teacherId
	 * @return
	 */
	public String getTeacherName(long teacherId) {
		String name = "";
		Teacher teacher = TeacherDao.getTeacher(teacherId);
		if (teacher != null) {
			name = teacher.getName();
		}
		return name;
	}

	/**
	 * 初始化teacher
	 * 
	 * @param teacherId
	 */
	public void initTeacher4VipUser(long teacherId) {
		Teacher teacher = TeacherDao.getTeacher(teacherId);
		Integer pfId = teacher.getPfId();
		int pfVipUid = IgpMsgFactory.getInstance().getUidFromAll(pfId);
		if (teacher.getPfVipUid() != pfVipUid) {
			teacher.setPfVipUid(pfVipUid);
			TeacherDao.saveTeacher(teacher);
		}
	}

}
