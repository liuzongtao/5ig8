/**
 * 
 */
package cn.guba.igu8.db.dao;

import java.util.List;

import cn.guba.igu8.db.mysqlModel.Viptype;
import cn.guba.igu8.web.vip.beans.EVipType;

/**
 * @author zongtao liu
 *
 */
public class VipTypeDao {
	
	public static void initInsert(){
		List<Viptype> list = Viptype.dao.find("select * from vipType");
		if(list == null || list.size() == 0){
			for(EVipType vipType: EVipType.values()){
				Viptype viptype = new Viptype().set("id", vipType.getValue()).setDescr(vipType.getDescr());
				viptype.save();
			}
		}
	}

}
