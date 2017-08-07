/**
 * 
 */
package cn.guba.igu8.tools.igp.account.beans;

import java.util.List;

import org.nutz.lang.Strings;

/**
 * @author zongtao liu
 *
 */
public class IgpAccountRes {
	private String rslt;
	private List<IgpAccountBean> data;

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
	 * @return the data
	 */
	public List<IgpAccountBean> getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<IgpAccountBean> data) {
		this.data = data;
	}

	public boolean isSucc() {
		if (Strings.equals(rslt, "succ")) {
			return true;
		}
		return false;
	}

}
