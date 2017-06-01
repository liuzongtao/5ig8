/**
 * 
 */
package cn.guba.igu8.core.utils;

/**
 * @author zongtao liu
 *
 */
public class DwzURLInfo {
	
	public static final String URL_CREATE = "http://www.dwz.cn/create.php";
	public static final String URL_QUERY = "http://www.dwz.cn/query.php";
	
	
	private String tinyurl;
	private String longurl;
	private int status;
	private String err;
	/**
	 * @return the tinyurl
	 */
	public String getTinyurl() {
		return tinyurl;
	}
	/**
	 * @param tinyurl the tinyurl to set
	 */
	public void setTinyurl(String tinyurl) {
		this.tinyurl = tinyurl;
	}
	/**
	 * @return the longurl
	 */
	public String getLongurl() {
		return longurl;
	}
	/**
	 * @param longurl the longurl to set
	 */
	public void setLongurl(String longurl) {
		this.longurl = longurl;
	}
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return the err
	 */
	public String getErr() {
		return err;
	}
	/**
	 * @param err the err to set
	 */
	public void setErr(String err) {
		this.err = err;
	}
}
