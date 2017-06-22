/**
 * 
 */
package cn.guba.igu8.web.vip.beans;

/**
 * @author zongtao liu
 *
 */
public class VipTypeViewBean {
	
	public int typeId = EVipType.igupiao.getValue();
	
	public String typeDescr;

	/**
	 * @return the typeId
	 */
	public int getTypeId() {
		return typeId;
	}

	/**
	 * @param typeId the typeId to set
	 */
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	/**
	 * @return the typeDescr
	 */
	public String getTypeDescr() {
		return typeDescr;
	}

	/**
	 * @param typeDescr the typeDescr to set
	 */
	public void setTypeDescr(String typeDescr) {
		this.typeDescr = typeDescr;
	}
	
	

}
