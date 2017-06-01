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

	private Cookie cookie;

	private int uid;

	public IgpUpdateLiverMsgThread(Cookie cookie, int uid) {
		this.cookie = cookie;
		this.uid = uid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		IgpMsgFactory.getInstance().updateLiverMsg(cookie, uid);
	}

}
