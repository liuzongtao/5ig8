/**
 * 
 */
package cn.guba.igu8.db.models;

import java.util.List;

/**
 * @author zongtao liu
 *
 */
public class UserModel extends BaseModel{
	
	/***
	 * 昵称
	 */
	private String nickname;
	
	/***
	 * 密码
	 */
	private String passwd;
	
	/***
	 * 创建时间
	 */
	private long creatTime;
	
	/***
	 * 电话号码
	 */
	private long phoneNumber;
	
	/***
	 * 邀请人uid
	 */
	private long inviterUid;
	
	/***
	 * 优惠信息列表
	 */
	private List<UserDiscountInfoModel> discountInfoList; 
	
	/***
	 * vip信息列表
	 */
	private List<UserVipInfoModel> vipInfoList;

}
