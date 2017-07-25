/**
 * 
 */
package cn.guba.igu8.db.dao;

import com.jfinal.plugin.activerecord.Page;

import cn.guba.igu8.db.mysqlModel.Rechargelog;

/**
 * @author zongtao liu
 *
 */
public class RechargeLogDao {

	public static boolean insert(Rechargelog rechargelog) {
		return rechargelog.save();
	}

	public static Page<Rechargelog> paginate(int pageNumber, int pageSize) {
		return Rechargelog.dao.paginate(pageNumber, pageSize, "select *", "from rechargelog order by id ");
	}

	public static Page<Rechargelog> paginateByUid(long uid, int pageNumber, int pageSize) {
		return Rechargelog.dao.paginate(pageNumber, pageSize, "select *",
				"from rechargelog where uid=" + uid + " order by id ");
	}

}
