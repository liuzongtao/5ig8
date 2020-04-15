package cn.guba.igu8.db.dao;

import cn.guba.igu8.convertibleBond.beans.ConvertibleBondBean;
import cn.guba.igu8.convertibleBond.beans.ConvertibleBondDetailBean;
import cn.guba.igu8.db.mysqlModel.StockBondLog;
import com.jfinal.plugin.activerecord.Db;
import lombok.extern.slf4j.Slf4j;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Strings;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class StockBondLogDao {

    public static void batchInsertBond(List<ConvertibleBondBean> list) {
        List<StockBondLog> contentList = new ArrayList<>();
        for (ConvertibleBondBean bean : list) {
            StockBondLog content = getStockBondLog(bean);
            log.debug(Json.toJson(content, JsonFormat.compact()));
            contentList.add(content);
        }
        Db.batchSave(contentList, contentList.size());
    }

    private static StockBondLog getStockBondLog(ConvertibleBondBean cbBean) {
        ConvertibleBondDetailBean bean = cbBean.getCell();
        StockBondLog log = new StockBondLog();
        log.setBondId(bean.getBondId());
        log.setBondNm(bean.getBondNm());
        log.setCbType(bean.getCbType());
        log.setConvertDt(bean.getConvertDt());
        log.setConvertPrice(bean.getConvertPrice());
        float putConvertPrice = 0;
        if (!bean.getPutConvertPrice().equals("-")) {
            putConvertPrice = Float.valueOf(bean.getPutConvertPrice());
        }
        log.setPutConvertPrice(putConvertPrice);
        log.setCurrIssAmt(bean.getCurrIssAmt());
        log.setForceRedeemPrice(bean.getForceRedeemPrice());
        log.setIncreaseRt(getRt(bean.getIncreaseRt()));
        log.setMaturityDt(bean.getMaturityDt());
        log.setOrigIssAmt(bean.getOrigIssAmt());
        log.setPremiumRt(getRt(bean.getPremiumRt()));
        log.setPrice(bean.getPrice());
        log.setRatingCd(bean.getRatingCd());
        log.setRedeemDt(bean.getRedeemDt());
        log.setSincreaseRt(getRt(bean.getSincreaseRt()));
        log.setSprice(bean.getSprice());
        log.setStockId(bean.getStockId());
        log.setVolume(bean.getVolume());
        log.setYearLeft(bean.getYearLeft());
        log.setYtmRtTax(getRt(bean.getYtmRtTax()));
        return log;
    }

    private static float getRt(String rt) {
        if (Strings.isBlank(rt)) {
            return 0;
        }
        float rtFloat = 0;
        try {
            rtFloat = Float.valueOf(rt.replace("%", ""));
        } catch (Exception e) {
            log.error("getRt == " + rt, e);
        }

        return rtFloat;
    }
}
