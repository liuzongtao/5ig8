/**
 * 
 */
package cn.guba.igu8.processor.igupiaoWeb.msg.beans;

/**
 * @author zongtao liu
 *
 */
public enum EIgpContentSource {

	VIPUSER("vipuser"),
	
	DETAIL("detail"),

	;

	private String value;

	private EIgpContentSource(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

}
