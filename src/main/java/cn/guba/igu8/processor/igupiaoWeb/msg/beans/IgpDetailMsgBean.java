/**
 * 
 */
package cn.guba.igu8.processor.igupiaoWeb.msg.beans;

/**
 * @author zongtao liu
 *
 */
public class IgpDetailMsgBean {

	private String rslt;

	private IgpDetailBean[] show_detail;

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
	 * @return the show_detail
	 */
	public IgpDetailBean[] getShow_detail() {
		return show_detail;
	}

	/**
	 * @param show_detail
	 *            the show_detail to set
	 */
	public void setShow_detail(IgpDetailBean[] show_detail) {
		this.show_detail = show_detail;
	}

}
