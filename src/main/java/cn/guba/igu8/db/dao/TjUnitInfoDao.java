package cn.guba.igu8.db.dao;

import cn.guba.igu8.db.mysqlModel.TjUnitInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TjUnitInfoDao {

    public static boolean insert(TjUnitInfo tjUnitInfo) {
        return tjUnitInfo.save();
    }

    public static TjUnitInfo selectByUnitId(Long unitId) {
        String sql = "select * from tj_unit_info where unitId = " + unitId;
        return TjUnitInfo.dao.findFirst(sql);
    }
}
