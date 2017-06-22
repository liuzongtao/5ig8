/**
 * 
 */
package cn.guba.igu8.web.user.beans;

/**
 * @author zongtao liu
 *
 */
public enum ERoleType {

	ADMIN(1),

	CONSUMER(2),

	;

	private int value;

	private ERoleType(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}

}
