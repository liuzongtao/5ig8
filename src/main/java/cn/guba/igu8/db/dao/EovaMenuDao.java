/**
 * 
 */
package cn.guba.igu8.db.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.guba.igu8.db.mysqlModel.EovaMenu;

/**
 * @author zongtao liu
 *
 */
public class EovaMenuDao {

	private static Map<Integer, EovaMenu> eovaMenuMap = new HashMap<Integer, EovaMenu>();

	public static void initEovaMenuMap() {
		List<EovaMenu> list = EovaMenu.dao.find("select * from eova_menu");
		for (EovaMenu eovaMenu : list) {
			eovaMenuMap.put(eovaMenu.getId(), eovaMenu);
		}
	}

	public static List<EovaMenu> getAllEovaMenu() {
		if (eovaMenuMap.size() == 0) {
			initEovaMenuMap();
		}
		List<EovaMenu> list = new ArrayList<EovaMenu>();
		list.addAll(eovaMenuMap.values());
		return list;
	}

	public static List<EovaMenu> getEovaMenuRoot() {
		return getEovaMenusByParentId(0);
	}

	public static List<EovaMenu> getEovaMenusByParentId(int parentId) {
		return EovaMenu.dao.find("select * from eova_menu where parent_id=" + parentId + " order by order_num");
	}

	public static EovaMenu getEovaMenu(int id) {
		return EovaMenu.dao.findById(id);
	}

	public static boolean addEovaMenu(EovaMenu eovaMenu) {
		eovaMenuMap.put(eovaMenu.getId(), eovaMenu);
		return eovaMenu.save();
	}

}
