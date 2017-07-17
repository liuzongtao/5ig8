/**
 * 
 */
package cn.guba.igu8.processor.igupiaoWeb.msg;

import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.jfinal.kit.PropKit;

import cn.guba.igu8.db.dao.IgpcontentDao;
import cn.guba.igu8.processor.igupiaoWeb.msg.beans.EIgpContentSource;
import cn.guba.igu8.processor.igupiaoWeb.msg.beans.EIgpKind;
import cn.guba.igu8.processor.igupiaoWeb.msg.beans.IgpWebLiverMsgBean;
import cn.guba.igu8.processor.igupiaoWeb.msg.beans.IgpWebMsgBean;
import cn.guba.igu8.processor.igupiaoWeb.service.IgpMsgService;
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

	public void addMsg(IgpWebLiverMsgBean liverMsg) {
		log.debug("addMsg == begin == teacherId = " + teacherId + " ; maxId = " + maxId);
		IgpWebMsgBean[] msg_list = liverMsg.getMsg_list();
		if (msg_list == null || msg_list.length == 0) {
			log.debug("msg_list is null or legth is 0");
			return;
		}
		if (maxId == 0) {
			maxId = IgpcontentDao.getMaxId(teacherId);
		}
		// 添加指定消息
		long curMaxId = 0;
		if (liverMsg.getTop_msg_list() != null && liverMsg.getTop_msg_list().length > 0) {
			curMaxId = addMsgForArr(liverMsg.getTop_msg_list(), false);
		}
		// 添加普通消息
		addMsgForArr(msg_list, true);
		// 更新最大id
		if (curMaxId > maxId) {
			maxId = curMaxId;
		}
		log.debug("addMsg == end == teacherId = " + teacherId + " ; maxId = " + maxId);
	}

	private long addMsgForArr(IgpWebMsgBean[] msg_list, boolean isUpMaxId) {
		long curMaxId = 0;
		int size = msg_list.length;
		for (int i = size - 1; i >= 0; i--) {
			IgpWebMsgBean igpWebMsgBean = msg_list[i];
			long id = Long.valueOf(igpWebMsgBean.getId());
			if (id > maxId) {
				// 判断系统启动时，是通过内容解析获得，还是模拟登陆获得 信息
				if (Strings.equals(PropKit.get("contentSource"), EIgpContentSource.VIPUSER.getValue())) {
					if (igpWebMsgBean.getKind().equals(EIgpKind.VIP.getValue())
							&& Strings.isBlank(igpWebMsgBean.getContent_new())) {
						TeacherService.getInstance().initTeacher4VipUser(teacherId);
						break;
					}
				}
				log.info("new msg : teacherId = " + teacherId + "; kind = " + igpWebMsgBean.getKind() + " ; time = "
						+ igpWebMsgBean.getRec_time_desc() + " ; id= " + id + "; maxId=" + maxId);
				// 插入数据库
				IgpcontentDao.insertMsg(teacherId, igpWebMsgBean);
				// 如果，发送短消息
				// ThreadsManager.getInstance().addThread(new
				// SendMessageThread(teacherId, igpWebMsgBean));
				IgpMsgService.getInstance().sendMsg(teacherId, igpWebMsgBean, true);
				if (isUpMaxId) {
					maxId = id;
				}
				curMaxId = id;
			}
		}
		return curMaxId;
	}

}
