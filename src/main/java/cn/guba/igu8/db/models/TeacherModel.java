/**
 * 
 */
package cn.guba.igu8.db.models;

/**
 * @author zongtao liu
 *
 */
public class TeacherModel extends BaseModel {

	/***
	 * 平台指导老师id
	 */
	private int pfId;

	/***
	 * 名字
	 */
	private String name;

	/***
	 * 从平台购买结束时间
	 */
	private long buyEndTime;

	/***
	 * vip类型id
	 */
	private long vipTypeId;

}
