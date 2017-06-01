/**
 * 
 */
package cn.guba.igu8.db.dao;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jfinal.plugin.activerecord.Db;

import cn.guba.igu8.db.mysqlModel.Uservipinfo;

/**
 * @author zongtao liu
 *
 */
public class UserVipInfoDao {

	private static Map<Long, List<Uservipinfo>> mapInfo = new ConcurrentHashMap<Long, List<Uservipinfo>>();

	public static List<Uservipinfo> getUservipinfos(long teacherId) {
		List<Uservipinfo> list = null;
		if (!mapInfo.containsKey(teacherId)) {
			list = Uservipinfo.dao.find("select * from uservipinfo where concernedTeacherId=" + teacherId);
			mapInfo.put(teacherId, list);
		} else {
			list = mapInfo.get(teacherId);
		}
		long now = System.currentTimeMillis();
		for (Uservipinfo tmpUservipinfo : list) {
			if (tmpUservipinfo.getVipEndTime() < now) {
				tmpUservipinfo.delete();
				list.remove(tmpUservipinfo);
			}
		}
		return list;
	}
	
	/***
	 * 批量插入
	 * @param list
	 */
	public static void batchInsert(List<Uservipinfo> list){
		Db.batchSave(list, list.size());
	}

}
