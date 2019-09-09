/**
 * 
 */
package cn.guba.igu8.web.mail.service;

import cn.guba.igu8.core.constants.Constant;
import cn.guba.igu8.core.mail.MailFactory;
import cn.guba.igu8.core.utils.Util;
import cn.guba.igu8.db.dao.TeacherDao;
import cn.guba.igu8.db.dao.UserDao;
import cn.guba.igu8.db.mysqlModel.Teacher;
import cn.guba.igu8.db.mysqlModel.User;
import cn.guba.igu8.web.user.service.UserService;

/**
 * @author zongtao liu
 *
 */
public class MailService {

	private static volatile MailService mailService;

	private MailService() {
	}

	public static MailService getInstance() {
		if (mailService == null) {
			synchronized (MailService.class) {
				if (mailService == null) {
					mailService = new MailService();
				}
			}
		}
		return mailService;
	}

	public void sendNoticeEmail(long uid, long teacherId, long endTime) {
		User user = UserDao.getUser(uid);
		Teacher teacher = TeacherDao.getTeacher(teacherId);
		String dateStr = Util.dateformat(endTime);
		String mailContent = String.format(Constant.MAIL_TEMP_UPDATE, teacher.getName(), dateStr);
		MailFactory.getInstance().sendEmail(user.getEmail(), Constant.MAIL_TEMP_UPDATE_SUBJECT, mailContent);
	}

	public void sendNotice4EndEmail(long uid, long teacherId, long endTime) {
		User user = UserDao.getUser(uid);
		Teacher teacher = TeacherDao.getTeacher(teacherId);
		String dateStr = Util.dateformat(endTime);
		String mailContent = String.format(Constant.MAIL_TEMP_END, teacher.getName(), dateStr);
		MailFactory.getInstance().sendEmail(user.getEmail(), Constant.MAIL_TEMP_END_SUBJECT, mailContent);
	}
}
