/**
 * 
 */
package cn.guba.igu8.processor.igupiaoWeb.msg.beans;

/**
 * @author zongtao liu
 *
 */
public enum EIgpKind {
	
	VIP("vip","会员信息"),
	FREE("free","公开信息"),
	CHARGE("charge","总结信息"),
	;
	
	private String value;
	
	private String descr;
	
	private EIgpKind(String value,String descr){
		this.value = value;
		this.descr = descr;
	}
	
	public String getValue(){
		return this.value;
	}
	
	public String getDescr(){
		return this.descr;
	}

}
