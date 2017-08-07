/**
 * 
 */
package cn.guba.igu8.processor.igupiaoWeb.threads;

import org.nutz.http.Cookie;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import cn.guba.igu8.processor.igupiaoWeb.msg.IgpMsgFactory;

/**
 * @author zongtao liu
 *
 */
public class IgpUpdateLiverMsgThread implements Runnable {

	private static Log log = Logs.get();

	public static volatile boolean isWorking = false;

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
			try {
				Cookie cookie = IgpMsgFactory.getInstance().getCookie();
				IgpMsgFactory.getInstance().updateLiverMsg(cookie, uid);
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
			log.debug("updateLiverMsg is over ! isWorking == " + isWorking);
			isWorking = false;
		}
	}

}
