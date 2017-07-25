/**
 * 
 */
package cn.guba.igu8.processor.igupiaoWeb.threads;

import java.util.List;

import cn.guba.igu8.db.dao.UserVipInfoDao;
import cn.guba.igu8.db.mysqlModel.Uservipinfo;
import cn.guba.igu8.web.mail.service.MailService;

/**
 * @author zongtao liu
 *
 */
public class VipCheckThread implements Runnable {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		List<Uservipinfo> allUservipinfo = UserVipInfoDao.getAllUservipinfo();
		long now = System.currentTimeMillis();
		long time = 24 * 60 * 60 * 1000;
		for (Uservipinfo info : allUservipinfo) {
			long vipEndTime = info.getVipEndTime();
			if (vipEndTime > now && vipEndTime - now < time) {
				MailService.getInstance().sendNotice4EndEmail(info.getUid(), info.getConcernedTeacherId(),
						info.getVipEndTime());
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
