/**
 * 
 */
package cn.guba.igu8.processor.igupiaoWeb.msg.beans;

/**
 * @author zongtao liu
 *
 */
public enum EIgpKind {
	
	VIP("vip"),
	FREE("free"),
	CHARGE("charge"),
	;
	
	private String value;
	
	private EIgpKind(String value){
		this.value = value;
	}
	
	public String getValue(){
		return this.value;
	}
	

}
