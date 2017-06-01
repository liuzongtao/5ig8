/**
 * 
 */
package cn.guba.igu8.db.models;

/**
 * @author zongtao liu
 *
 */
public class UserVipInfoModel extends BaseModel{
	
	/***
	 * vip类型id
	 */
	private long vipTypeId;

	/***
	 * vip结束时间
	 */
	private long vipEndTime;
	
	/***
	 * 关注的老师id
	 */
	private long concernedTeacherId;

}
