/**
 * 
 */
package cn.guba.igu8.processor.igupiaoWeb.threads;

import cn.guba.igu8.processor.igupiaoWeb.msg.beans.IgpWebMsgBean;
import cn.guba.igu8.processor.igupiaoWeb.service.IgpMsgService;

/**
 * @author zongtao liu
 *
 */
public class SendMessageThread extends Thread {

	private long teacherId;

	private IgpWebMsgBean igpWebMsgBean;

	private boolean sendSms = true;

	public SendMessageThread(long teacherId, IgpWebMsgBean igpWebMsgBean) {
		this.teacherId = teacherId;
		this.igpWebMsgBean = igpWebMsgBean;
	}

	public SendMessageThread(long teacherId, IgpWebMsgBean igpWebMsgBean, boolean sendSms) {
		this.teacherId = teacherId;
		this.igpWebMsgBean = igpWebMsgBean;
		this.sendSms = sendSms;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// 如果，发送短消息
		IgpMsgService.getInstance().sendMsg(teacherId, igpWebMsgBean, sendSms);

	}

}
