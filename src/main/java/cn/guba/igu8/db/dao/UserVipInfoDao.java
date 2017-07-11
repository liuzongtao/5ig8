/**
 * 
 */
package cn.guba.igu8.db.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

import cn.guba.igu8.db.mysqlModel.Uservipinfo;

/**
 * @author zongtao liu
 *
 */
public class UserVipInfoDao {

	private static Map<Long, CopyOnWriteArrayList<Uservipinfo>> mapInfo = new ConcurrentHashMap<Long, CopyOnWriteArrayList<Uservipinfo>>();

	private static void init() {
		if (mapInfo.size() > 0) {
			return;
		}
		List<Uservipinfo> list = Uservipinfo.dao.find("select * from uservipinfo ");
		if (list == null) {
			return;
		}
		for (Uservipinfo info : list) {
			long teacherId = info.getConcernedTeacherId();
			CopyOnWriteArrayList<Uservipinfo> tmpList = null;
			if (mapInfo.containsKey(teacherId)) {
				tmpList = mapInfo.get(teacherId);
			} else {
				tmpList = new CopyOnWriteArrayList<Uservipinfo>();
				mapInfo.put(teacherId, tmpList);
			}
			tmpList.add(info);
		}
	}

	/**
	 * 获取老师的所有vip用户信息
	 * 
	 * @param teacherId
	 * @return
	 */
	public static List<Uservipinfo> getUservipinfos(long teacherId) {
		init();
		List<Uservipinfo> list = mapInfo.get(teacherId);
		if (list != null) {
			long now = System.currentTimeMillis();
			ArrayList<Uservipinfo> removeList = new ArrayList<Uservipinfo>();
			for (Uservipinfo tmpUservipinfo : list) {
				if (tmpUservipinfo.getVipEndTime() < now) {
					tmpUservipinfo.delete();
					removeList.add(tmpUservipinfo);
				}
			}
			list.removeAll(removeList);
		}
		return list;
	}

	/***
	 * 批量插入
	 * 
	 * @param list
	 */
	public static void batchInsert(List<Uservipinfo> list) {
		Db.batchSave(list, list.size());
	}

	/**
	 * 获取某个用户的vip信息列表
	 * 
	 * @param uid
	 * @return
	 */
	public static List<Uservipinfo> getVipList(long uid) {
		init();
		List<Uservipinfo> vipList = new ArrayList<Uservipinfo>();
		Collection<CopyOnWriteArrayList<Uservipinfo>> values = mapInfo.values();
		long now = System.currentTimeMillis();
		for (List<Uservipinfo> value : values) {
			List<Uservipinfo> tmpList = new ArrayList<Uservipinfo>();
			for (Uservipinfo vipInfo : value) {
				if (vipInfo.getVipEndTime() < now) {
					vipInfo.delete();
					tmpList.add(vipInfo);
				} else if (vipInfo.getUid() == uid) {
					vipList.add(vipInfo);
				}
			}
			tmpList.removeAll(tmpList);
		}
		return vipList;
	}

	public static void delByUid(long uid) {
		// 先从内容中清除
		Collection<CopyOnWriteArrayList<Uservipinfo>> values = mapInfo.values();
		long now = System.currentTimeMillis();

		for (List<Uservipinfo> value : values) {
			List<Uservipinfo> tmpList = new ArrayList<Uservipinfo>();
			for (Uservipinfo vipInfo : value) {
				if (vipInfo.getVipEndTime() < now) {
					vipInfo.delete();
					tmpList.add(vipInfo);
				} else if (vipInfo.getUid() == uid) {
					tmpList.add(vipInfo);
				}
			}
			value.removeAll(tmpList);
		}
		// 从数据中清除
		Db.update("delete from uservipinfo where uid=" + uid);
	}

	public static void delById(long id) {
		Uservipinfo uservipinfo = null;
		for (Entry<Long, CopyOnWriteArrayList<Uservipinfo>> entry : mapInfo.entrySet()) {
			List<Uservipinfo> values = entry.getValue();
			for (Uservipinfo tmpentry : values) {
				if (tmpentry.getId() == id) {
					uservipinfo = tmpentry;
					break;
				}
			}
			if (uservipinfo != null) {
				// 先从内容中清除
				values.remove(uservipinfo);
				// 从数据中清除
				uservipinfo.delete();
				break;
			}
		}
	}

	public static Page<Uservipinfo> paginateByUid(long uid, int pageNumber, int pageSize) {
		return Uservipinfo.dao.paginate(pageNumber, pageSize, "select *",
				"from uservipinfo where uid=" + uid + " order by id ");
	}

	public static Uservipinfo getById(long id) {
		init();
		Uservipinfo uservipinfo = null;
		for (Entry<Long, CopyOnWriteArrayList<Uservipinfo>> entry : mapInfo.entrySet()) {
			for (Uservipinfo tmpentry : entry.getValue()) {
				if (tmpentry.getId() == id) {
					uservipinfo = tmpentry;
					break;
				}
			}
			if (uservipinfo != null) {
				break;
			}
		}
		return uservipinfo;
	}

	public static boolean add(Uservipinfo uservipinfo) {
		init();
		long teacherId = uservipinfo.getConcernedTeacherId();
		CopyOnWriteArrayList<Uservipinfo> tmpList = null;
		if (mapInfo.containsKey(teacherId)) {
			tmpList = mapInfo.get(teacherId);
		} else {
			tmpList = new CopyOnWriteArrayList<Uservipinfo>();
			mapInfo.put(teacherId, tmpList);
		}
		tmpList.add(uservipinfo);
		return uservipinfo.save();
	}

	public static boolean update(Uservipinfo uservipinfo) {
		init();
		long id = uservipinfo.getId();
		Uservipinfo oldUservipinfo = null;
		long oldTeacherId = 0;
		for (Entry<Long, CopyOnWriteArrayList<Uservipinfo>> entry : mapInfo.entrySet()) {
			for (Uservipinfo tmpentry : entry.getValue()) {
				if (tmpentry.getId() == id) {
					oldUservipinfo = tmpentry;
					oldTeacherId = entry.getKey();
					break;
				}
			}
			if (oldUservipinfo != null) {
				break;
			}
		}
		if (oldUservipinfo == null) {
			return false;
		}

		long newTeacherId = uservipinfo.getConcernedTeacherId();
		if (oldTeacherId != newTeacherId) {// 需要修改缓存中位置
			List<Uservipinfo> oldList = mapInfo.get(oldTeacherId);
			oldList.remove(oldUservipinfo);
			CopyOnWriteArrayList<Uservipinfo> newList = mapInfo.get(newTeacherId);
			if (newList == null) {
				newList = new CopyOnWriteArrayList<Uservipinfo>();
				mapInfo.put(newTeacherId, newList);
			}
			newList.add(uservipinfo);
		}

		return uservipinfo.update();
	}

}
