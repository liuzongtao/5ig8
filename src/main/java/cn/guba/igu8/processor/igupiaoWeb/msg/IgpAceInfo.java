/**
 * 
 */
package cn.guba.igu8.processor.igupiaoWeb.msg;

import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import cn.guba.igu8.db.dao.IgpcontentDao;
import cn.guba.igu8.processor.igupiaoWeb.msg.beans.EIgpKind;
import cn.guba.igu8.processor.igupiaoWeb.msg.beans.IgpWebMsgBean;
import cn.guba.igu8.processor.igupiaoWeb.threads.SendMessageThread;
import cn.guba.igu8.processor.igupiaoWeb.threads.ThreadsManager;
import cn.guba.igu8.web.teacher.service.TeacherService;

/**
 * @author zongtao liu
 *
 */
public class IgpAceInfo {

	private static Log log = Logs.get();

	private long teacherId;

	private long maxId;

	public IgpAceInfo(long teacherId) {
		this.teacherId = teacherId;
	}

	public synchronized void addMsg(IgpWebMsgBean[] msg_list) {
		if (maxId == 0) {
			maxId = IgpcontentDao.getMaxId(teacherId);
		}
		int size = msg_list.length;
		for (int i = size - 1; i >= 0; i--) {
			IgpWebMsgBean igpWebMsgBean = msg_list[i];
			long id = Long.valueOf(igpWebMsgBean.getId());
			if (id > maxId) {
				if (igpWebMsgBean.getKind().equals(EIgpKind.VIP.getValue())
						&& Strings.isBlank(igpWebMsgBean.getContent_new())) {
					TeacherService.getInstance().initTeacher4VipUser(teacherId);
					break;
				}
				log.info("new msg : teacherId = " + teacherId + "; kind = " + igpWebMsgBean.getKind() + " ; time = "
						+ igpWebMsgBean.getRec_time_desc() + " ; id= " + id + "; maxId=" + maxId);
				// 插入数据库
				IgpcontentDao.insertMsg(teacherId, igpWebMsgBean);
				// 如果，发送短消息
				ThreadsManager.getInstance().addThread(new SendMessageThread(teacherId, igpWebMsgBean));
				maxId = id;
			}
		}
	}

}
