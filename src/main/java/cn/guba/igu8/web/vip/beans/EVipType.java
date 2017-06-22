/**
 * 
 */
package cn.guba.igu8.web.vip.beans;

/**
 * @author zongtao liu
 *
 */
public enum EVipType {
	
	igupiao(1,"爱股票"),
	zqzx(2,"证券之星");
	
	private int value;
	
	private String descr;
	
	private EVipType(int value,String descr){
		this.value = value;
		this.descr = descr;
	}
	
	public int getValue() {
		return value;
	}
	
	public String getDescr() {
		return descr;
	}
	
	public static EVipType getEVipType(int value){
		EVipType result = null;
		for(EVipType type : values()){
			if(type.getValue() == value){
				result = type;
				break;
			}
		}
		return result;
	}

}
