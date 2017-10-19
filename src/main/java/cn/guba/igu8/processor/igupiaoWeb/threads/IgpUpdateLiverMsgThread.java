/**
 * 
 */
package cn.guba.igu8.processor.igupiaoWeb.threads;

import java.util.Calendar;

import org.nutz.http.Cookie;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import cn.guba.igu8.core.init.SysInit;
import cn.guba.igu8.processor.igupiaoWeb.msg.IgpMsgFactory;

/**
 * @author zongtao liu
 *
 */
public class IgpUpdateLiverMsgThread implements Runnable {

	private static Log log = Logs.get();

	public static volatile boolean isWorking = false;
	public static volatile long lastUpdateTime = 0;

	private int uid;

	public IgpUpdateLiverMsgThread(int uid) {
		this.uid = uid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		log.info("isWorking == " + isWorking);
		if (!isWorking) {
			isWorking = true;
			long intervalTime = getIntervalTime();
			boolean canEnter = true;
			if (intervalTime != 0) {
				long now = System.currentTimeMillis();
				if (now - lastUpdateTime > intervalTime) {
					lastUpdateTime = now;
				} else {
					canEnter = false;
					log.info("canEnter is " + canEnter);
				}
			}
			if (canEnter) {
				try {
					Cookie cookie = IgpMsgFactory.getInstance().getCookie();
					// 系统爱股票平台特有参数
					SysInit.getInstance().initIgpUrlParam();
					IgpMsgFactory.getInstance().updateLiverMsg(cookie, uid);
				} catch (Exception e) {
					log.error(e.getMessage());
					e.printStackTrace();
				}
				log.info("updateLiverMsg is over ! isWorking == " + isWorking);
			}
			isWorking = false;
		}
	}

	/**
	 * 获取间隔时间
	 * 
	 * @return
	 */
	private long getIntervalTime() {
		Calendar cal = Calendar.getInstance();

		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int interval = 0;// 秒为单位
		if (hour >= 1 && hour <= 7) {// 早上
			interval = 60 * 60;
		} else if (hour > 7 && hour < 17) {
			// 如果是周末则10分钟一次
			int curday = cal.get(Calendar.DAY_OF_WEEK);
			if (curday < 2 || curday > 6) {
				interval = 10 * 60;
			} else {
				interval = 0;
			}
		} else if (hour >= 17 && hour <= 23) {
			interval = 10 * 60;
		} else {
			interval = 30 * 60;
		}
		return interval * 1000;
	}
}
