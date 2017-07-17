/**
 * 
 */
package cn.guba.igu8.web.teacher.beans;

/**
 * @author zongtao liu
 *
 */
public enum EIgpTeacher {

	/***
	 * 刀锋
	 */
	daofeng(2, "刀锋", Long.MAX_VALUE),
	/***
	 * 天策看市
	 */
	tiancekanshi(48, "天策看市"),
	/***
	 * 涅槃重生
	 */
	niepanchongsheng(57, "涅槃重生", Long.MAX_VALUE),
	/***
	 * 狙击手
	 */
	jujishou(8, "狙击手"),
	/***
	 * 张莉
	 */
	zhangli(120, "张莉"),
	/***
	 * 掘金者
	 */
	junjinzhe(75, "掘金者"),
	/***
	 * 子房论市
	 */
	zifanglunshi(109, "子房论市"),
	/***
	 * 老艾
	 */
	laoai(14, "老艾"),
	/***
	 * 红星看市
	 */
	hongxingkanshi(335, "红星看市"),
	/***
	 * 股市咪蒙
	 */
	gushimimeng(47, "股市咪蒙"),
	/***
	 * 复利为王
	 */
	fuliweiwang(342, "复利为王"),
	/***
	 * 牛股启蒙
	 */
	niuguqimeng(91, "牛股启蒙", Long.MAX_VALUE),
	/***
	 * 每日一股
	 */
	meiriyigu(219, "每日一股", Long.MAX_VALUE),
	/***
	 * 龙迈
	 */
	longmai(181, "龙迈"),
	/***
	 * 行者
	 */
	xingzhe(67, "行者"),
	/***
	 * 趋势之友
	 */
	qushizhiyou(7, "趋势之友"),
	/***
	 * 缠行者
	 */
	chanxingzhe(84, "缠行者"),
	/***
	 * 股哥
	 */
	guge(177, "股哥"),
	/***
	 * 盘手看盘
	 */
	panshoukanpan(33, "盘手看盘"),
	/***
	 * 执著信仰
	 */
	zhizhuoxinyang(538, "执著信仰"),
	/***
	 * 风痕居
	 */
	fenghenju(235, "风痕居"),
	/***
	 * 老龙头
	 */
	laolongtou(486, "老龙头"),
	/***
	 * 花路伯爵
	 */
	hualubojue(244, "花路伯爵"),
	/**
	 * 抱拙居士
	 */
	baozhuojushi(585, "抱拙居士", Long.MAX_VALUE),
	;
	private int value;

	private String name;

	private long buyEndTime = 0;

	private EIgpTeacher(int value, String name) {
		this.value = value;
		this.name = name;
	}

	private EIgpTeacher(int value, String name, long buyEndTime) {
		this.value = value;
		this.name = name;
		this.buyEndTime = buyEndTime;
	}

	public int getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	public long getBuyEndTime() {
		return buyEndTime;
	}

}
