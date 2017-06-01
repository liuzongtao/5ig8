/**
 * 
 */
package cn.guba.igu8.processor.igupiaoWeb.msg.beans;

/**
 * @author zongtao liu
 *
 */
public class IgpWebLiverMsgBean {
	private String rslt;

	private boolean is_vip;

	private IgpWebMsgBean[] top_msg_list;

	private IgpWebMsgBean[] msg_list;

	/**
	 * @return the rslt
	 */
	public String getRslt() {
		return rslt;
	}

	/**
	 * @param rslt
	 *            the rslt to set
	 */
	public void setRslt(String rslt) {
		this.rslt = rslt;
	}

	/**
	 * @return the is_vip
	 */
	public boolean isIs_vip() {
		return is_vip;
	}

	/**
	 * @param is_vip
	 *            the is_vip to set
	 */
	public void setIs_vip(boolean is_vip) {
		this.is_vip = is_vip;
	}

	/**
	 * @return the top_msg_list
	 */
	public IgpWebMsgBean[] getTop_msg_list() {
		return top_msg_list;
	}

	/**
	 * @param top_msg_list
	 *            the top_msg_list to set
	 */
	public void setTop_msg_list(IgpWebMsgBean[] top_msg_list) {
		this.top_msg_list = top_msg_list;
	}

	/**
	 * @return the msg_list
	 */
	public IgpWebMsgBean[] getMsg_list() {
		return msg_list;
	}

	/**
	 * @param msg_list
	 *            the msg_list to set
	 */
	public void setMsg_list(IgpWebMsgBean[] msg_list) {
		this.msg_list = msg_list;
	}

}
