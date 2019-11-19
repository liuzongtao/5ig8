package cn.guba.igu8.db.dao;

import cn.guba.igu8.db.mysqlModel.TjDealInfo;
import com.jfinal.ext.kit.DateKit;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
public class TjDealInfoDao {

    public static boolean insert(TjDealInfo tjDealInfo) {
        return tjDealInfo.save();
    }

    public static List<TjDealInfo> getInfoListByDate(Date date) {
        String beginDate = DateKit.toStr(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        String endDate = DateKit.toStr(cal.getTime());
        String sql = "select * from tj_deal_info where createdAt >= '" + beginDate + "' and createdAt < '" + endDate + "'";
        return TjDealInfo.dao.find(sql);
    }

}
