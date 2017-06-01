/**
 * 
 */
package cn.guba.igu8.processor.igupiaoWeb.msg.beans;

/**
 * @author zongtao liu
 *
 */
public class IgpWebStatusMsgBean {

	private String rslt;

	private int code;

	private String info;

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
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return the info
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * @param info
	 *            the info to set
	 */
	public void setInfo(String info) {
		this.info = info;
	}

}
