/**
 * 
 */
package cn.guba.igu8.db.models;

/**
 * @author zongtao liu
 *
 */
public class UserDiscountInfoModel extends BaseModel {

	/***
	 * uid
	 */
	private long uid;

	/***
	 * 优惠金额
	 */
	private int discountMoney;

	/***
	 * 优惠结束时间
	 */
	private long discountEndTime;

	/**
	 * 优惠来源
	 */
	private long discountFromUid;

	/***
	 * 当前时间
	 */
	private long curTime;

}
