/**
 * 
 */
package cn.guba.igu8.processor.igupiaoWeb.threads;

import org.nutz.http.Cookie;

import cn.guba.igu8.processor.igupiaoWeb.msg.IgpMsgFactory;

/**
 * @author zongtao liu
 *
 */
public class IgpUpdateLiverMsgThread implements Runnable {

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
		try {
			Cookie cookie = IgpMsgFactory.getInstance().getCookie();
			IgpMsgFactory.getInstance().updateLiverMsg(cookie, uid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
