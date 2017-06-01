/**
 * 
 */
package cn.guba.igu8.db.models;

/**
 * @author zongtao liu
 *
 */
public class RechargeLogModel extends BaseModel {

	/***
	 * uid
	 */
	private long uid;

	/***
	 * 昵称
	 */
	private String nickname;

	/***
	 * 创建时间
	 */
	private long creatTime;

	/***
	 * 电话号码
	 */
	private long phoneNumber;

	/***
	 * 当前时间
	 */
	private long curTime;

	/***
	 * 购买前vip结束时间
	 */
	private long oldVipEndTime;

	/***
	 * 购买后vip结束时间
	 */
	private long newVipEndTime;

	/***
	 * 优惠信息字符串 多个<优惠金额，优惠结束时间>
	 */
	private String disCountInfosStr;

	/***
	 * vip类型id
	 */
	private long vipTypeId;

	/***
	 * 购买的vip类型描述
	 */
	private String vipTypeDescr;

	/***
	 * 充值消耗钱数：单位为元
	 */
	private int costMoney;

}
