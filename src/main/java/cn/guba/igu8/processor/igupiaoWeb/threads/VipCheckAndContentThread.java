/**
 * 
 */
package cn.guba.igu8.processor.igupiaoWeb.threads;

import java.util.List;

import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import cn.guba.igu8.db.dao.UserVipInfoDao;
import cn.guba.igu8.db.mysqlModel.Uservipinfo;
import cn.guba.igu8.web.content.service.ContentsService;
import cn.guba.igu8.web.mail.service.MailService;

/**
 * @author zongtao liu
 *
 */
public class VipCheckAndContentThread implements Runnable {

	private static Log log = Logs.get();

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
		log.info("VipCheckThread will check ! ");
		for (Uservipinfo info : allUservipinfo) {
			long vipEndTime = info.getVipEndTime();
			if (vipEndTime > now && vipEndTime - now < time) {
				MailService.getInstance().sendNotice4EndEmail(info.getUid(), info.getConcernedTeacherId(),
						info.getVipEndTime());
				log.info("it will end ! " + Json.toJson(info, JsonFormat.compact()));
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		int day = 120;
		// 删除120天之前的数据
		int num = ContentsService.getInstance().delContentsByDay(day);
		log.info("has del content num is " + num);
	}

}
