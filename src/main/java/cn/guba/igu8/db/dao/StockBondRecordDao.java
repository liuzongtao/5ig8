package cn.guba.igu8.db.dao;

import cn.guba.igu8.convertibleBond.beans.ConvertibleBondDetailBean;
import cn.guba.igu8.db.mysqlModel.StockBondRecord;
import com.jfinal.plugin.activerecord.Db;
import lombok.extern.slf4j.Slf4j;
import org.nutz.lang.Strings;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class StockBondRecordDao {

    public static void batchInsertRecord(List<ConvertibleBondDetailBean> list, int size) {
        List<StockBondRecord> contentList = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            ConvertibleBondDetailBean convertibleBondDetailBean = list.get(i);
            StockBondRecord stockBondRecord = getStockBondRecord(convertibleBondDetailBean.getBondId());
            if (stockBondRecord == null) {
                StockBondRecord record = generStockBondRecord(convertibleBondDetailBean, i);
                contentList.add(record);
            }
        }
        Db.batchSave(contentList, contentList.size());
    }

    private static StockBondRecord getStockBondRecord(String bondId) {
        List<StockBondRecord> stockBondRecords = StockBondRecord.dao.find("select * from stock_bond_record where bondId = " + bondId + " and sellPrice = 0");
        if (stockBondRecords.size() > 0) {
            return stockBondRecords.get(0);
        }
        return null;
    }

    public static List<StockBondRecord> getAllStockBondRecord() {
        return StockBondRecord.dao.find("select * from stock_bond_record where sellPrice = 0");
    }

    public static boolean updateAllStockBondRecord(List<StockBondRecord> records) {
        Db.batchUpdate(records,records.size());
        return true;
    }

    private static StockBondRecord generStockBondRecord(ConvertibleBondDetailBean bean, int index) {
        StockBondRecord record = new StockBondRecord();
        record.setBondId(bean.getBondId());
        record.setBondNm(bean.getBondNm());
        record.setStockId(bean.getStockId());
        record.setCurrPrice(bean.getPrice());
        record.setMaxPrice(bean.getPrice());
        record.setMinPrice(bean.getPrice());
        record.setBuyPrice(bean.getPrice());
        record.setCbType(bean.getCbType());
        record.setCbTypeIndex(index);
        return record;
    }

    private static float getRt(String rt) {
        if (Strings.isBlank(rt)) {
            return 0;
        }
        return Float.valueOf(rt.replace("%", ""));
    }
}
