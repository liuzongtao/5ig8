package cn.guba.igu8.convertibleBond.threads;

import cn.guba.igu8.convertibleBond.beans.ConvertibleBondBean;
import cn.guba.igu8.convertibleBond.beans.ConvertibleBondDetailBean;
import cn.guba.igu8.convertibleBond.enums.CbTypeEnum;
import cn.guba.igu8.convertibleBond.service.ConvertibleBondService;
import cn.guba.igu8.core.mail.MailFactory;
import cn.guba.igu8.db.dao.StockBondLogDao;
import cn.guba.igu8.db.dao.StockBondRecordDao;
import cn.guba.igu8.db.mysqlModel.StockBondRecord;
import cn.guba.igu8.stock.service.StockService;
import cn.guba.igu8.utils.DateUtils;
import com.google.common.collect.ImmutableSet;
import com.jfinal.ext.kit.DateKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.nutz.json.Json;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class ConvertibleBondThread implements Runnable {


    private static Set<String> RATING_CD_SET = ImmutableSet.of("AAA", "AA+", "AA");
    private static final String EMAIL_ADDRESS = "369650047@qq.com";
    private static final String EMAIL_TITLE = "【可转债】统计信息";

    public static void main(String[] args) {
        ConvertibleBondThread t = new ConvertibleBondThread();
        t.run();
    }

    @Override
    public void run() {
        log.info("ConvertibleBondThread begin run !!!");
        //判断是不是休息日
        boolean isRestDay = DateUtils.isRestDay();
        if (isRestDay) {
            log.info("today is restDay");
            return;
        }
        boolean stockRest = StockService.getInstance().isRest();
        if (stockRest) {
            log.info("today is stock rest !!!");
            return;
        }
        log.info("today is stock rest !!!");
        //随机一段时间
        Random random = new Random();
        int time = random.nextInt(10);
        log.info("ConvertibleBondThread.random == " + time);
        try {
            TimeUnit.MINUTES.sleep(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        coreRun();
        log.info("ConvertibleBondThread is end !!!");
    }

    public void coreRun() {
        //获取所有的可转债
        List<ConvertibleBondBean> convertibleBondBeans = ConvertibleBondService.getInstance().getConvertibleBondBeans();
        if (CollectionUtils.isEmpty(convertibleBondBeans)) {
            log.info("convertibleBondBeans is Empty");
            return;
        }
        //防御型列表
        List<ConvertibleBondDetailBean> defensiveBeans = new ArrayList<>();
        //平衡型列表
        List<ConvertibleBondDetailBean> balancedBeans = new ArrayList<>();
        //激进型列表
        List<ConvertibleBondDetailBean> redicalBeans = new ArrayList<>();

        //同时，查询出防御型、激进型，平衡型三个列表
        convertibleBondBeans.stream().forEach(e -> {
            ConvertibleBondDetailBean detailBean = e.getCell();
            if (detailBean.getRedeemDt() != null) {
                detailBean.setBondNm(detailBean.getBondNm() + "!");
            }

            //筛选出AA评级以上的
            if (RATING_CD_SET.contains(detailBean.getRatingCd())) {
                //税后收益率
                float ytmRtTax = Float.valueOf(detailBean.getYtmRtTax().replace("%", ""));
                //转股溢价率
                float premiumRt = Float.valueOf(detailBean.getPremiumRt().replace("%", ""));
                if (ytmRtTax >= 3) {
                    //防御型
                    detailBean.setCbType(CbTypeEnum.DEFENSIVE.getCode());
                    defensiveBeans.add(detailBean);
                } else if (ytmRtTax > 0 && premiumRt <= 5) {
                    //平衡型
                    detailBean.setCbType(CbTypeEnum.BALANCED.getCode());
                    balancedBeans.add(detailBean);
                }
                Date now = new Date();
                Date convertDt = detailBean.getConvertDt();
                if (premiumRt < 2 && now.after(convertDt)) {
                    //激进型
                    int cbType = detailBean.getCbType() + CbTypeEnum.REDICAL.getCode();
                    detailBean.setCbType(cbType);
                    redicalBeans.add(detailBean);

                }
            }
        });


        //记录所有的可转债信息
        StockBondLogDao.batchInsertBond(convertibleBondBeans);

        //更新现在可转债信息
        Map<String, ConvertibleBondDetailBean> detailMap = new HashMap<>();
        convertibleBondBeans.stream().forEach(e -> {
            detailMap.put(e.getId(), e.getCell());
        });
        List<StockBondRecord> stockBondRecords = updateBondRecord(detailMap);
        //记录可购买可转债
        StockBondRecordDao.batchInsertRecord(defensiveBeans, 3);
        StockBondRecordDao.batchInsertRecord(balancedBeans, 5);
        StockBondRecordDao.batchInsertRecord(redicalBeans, 2);
        log.info("defensiveBeans == " + Json.toJson(defensiveBeans));
        log.info("balancedBeans == " + Json.toJson(balancedBeans));
        log.info("redicalBeans == " + Json.toJson(redicalBeans));

        // 获取邮件内容
        String emailData = getEmailData(defensiveBeans, balancedBeans, redicalBeans, stockBondRecords, detailMap);
        log.info(emailData);
        //发送邮件
        String title = EMAIL_TITLE + DateKit.toStr(new Date());
        MailFactory.getInstance().sendEmail(EMAIL_ADDRESS, title, emailData);
    }

    /***
     * 更新股票记录
     * @param detailMap
     * @return
     */
    private List<StockBondRecord> updateBondRecord(Map<String, ConvertibleBondDetailBean> detailMap) {
        List<StockBondRecord> allStockBondRecord = StockBondRecordDao.getAllStockBondRecord();
        for (StockBondRecord stockBondRecord : allStockBondRecord) {
            String bondId = stockBondRecord.getBondId();
            ConvertibleBondDetailBean bean = detailMap.get(bondId);
            if (bean == null) {
                stockBondRecord.setSellPrice(stockBondRecord.getCurrPrice());
                stockBondRecord.setSellDt(new Date());
            } else {
                //如果有回购，名字会发生变化
                stockBondRecord.setBondNm(bean.getBondNm());
                float price = bean.getPrice();
                stockBondRecord.setCurrPrice(price);
                float maxPrice = stockBondRecord.getMaxPrice();
                if (price > maxPrice) {
                    stockBondRecord.setMaxPrice(price);
                }
                float minPrice = stockBondRecord.getMinPrice();
                if (price < minPrice) {
                    stockBondRecord.setMinPrice(price);
                }
                //卖出条件
                if (price > 130 && price < maxPrice * 0.9) {
                    stockBondRecord.setSellPrice(price);
                    stockBondRecord.setSellDt(new Date());
                }
            }
        }
        StockBondRecordDao.updateAllStockBondRecord(allStockBondRecord);
        return allStockBondRecord;
    }

    /**
     * 获取邮件数据
     *
     * @param defensiveBeans
     * @param balancedBeans
     * @param redicalBeans
     * @param stockBondRecords
     * @param detailMap
     * @return
     */
    private String getEmailData(List<ConvertibleBondDetailBean> defensiveBeans,
                                List<ConvertibleBondDetailBean> balancedBeans,
                                List<ConvertibleBondDetailBean> redicalBeans,
                                List<StockBondRecord> stockBondRecords,
                                Map<String, ConvertibleBondDetailBean> detailMap) {
        StringBuilder content = new StringBuilder("<html><head></head><body>");
        content.append("</br>");
        getEmailFilteredData(content, defensiveBeans, balancedBeans, redicalBeans);
        content.append("</br>");
        getEmailBuyedData(content, stockBondRecords, detailMap);
        content.append("</body></html>");
        return content.toString();
    }

    private void getEmailFilteredData(StringBuilder content, List<ConvertibleBondDetailBean> defensiveBeans,
                                      List<ConvertibleBondDetailBean> balancedBeans,
                                      List<ConvertibleBondDetailBean> redicalBeans) {
        content.append("筛选条件：防御型(到期税后年化收益率大于3%)；").append("</br>");
        content.append("&nbsp;&nbsp;&nbsp;&nbsp;平衡型(到期税后年化收益率大于0，小于3%，转股溢价率小于5%)；").append("</br>");
        content.append("&nbsp;&nbsp;&nbsp;&nbsp;进攻型(转股溢价率小于2%，并且已到转股期)；").append("</br>");
        content.append("&nbsp;&nbsp;&nbsp;&nbsp;去掉AA以下评级；按照到期年化收益率排序。").append("</br>");
        content.append("</br>");
        content.append("<table border=\"5\" style=\"border:solid 1px #E8F2F9;font-size=12px;\">");
        //标题行
        content.append("<tr style=\"background-color: #428BCA; color:#ffffff\">");
        content.append("<th>可转债名字</th>");
        content.append("<th>可转债代码</th>");
        content.append("<th>可转债价格</th>");
        content.append("<th>正股名称</th>");
        content.append("<th>正股价格</th>");
        content.append("<th>转股时间</th>");
        content.append("<th>转股价格</th>");
        content.append("<th>到期收益率</th>");
        content.append("<th>溢价率</th>");
        content.append("<th>剩余年限</th>");
        content.append("<th>评级</th>");
        content.append("<th>类型</th>");
        content.append("</tr>");
        generTable(content, defensiveBeans, "yellow");
        generTable(content, balancedBeans, "blue");
        generTable(content, redicalBeans, "green");
        content.append("</table>");
    }

    /**
     * 生成表格
     *
     * @param content
     * @param list
     * @param color
     */
    private void generTable(StringBuilder content, List<ConvertibleBondDetailBean> list, String color) {
        list.stream().sorted().collect(Collectors.toList()).forEach(e -> {
            content.append("<tr style=\"background-color: " + color + " \">");
            content.append("<td>").append(e.getBondNm()).append("</td>");
            content.append("<td>").append(e.getBondId()).append("</td>");
            content.append("<td>").append(e.getPrice()).append("</td>");
            content.append("<td>").append(e.getStockNm()).append("</td>");
            content.append("<td>").append(e.getSprice()).append("</td>");
            content.append("<td>").append(DateKit.toStr(e.getConvertDt())).append("</td>");
            content.append("<td>").append(e.getConvertPrice()).append("</td>");
            content.append("<td>").append(e.getYtmRtTax()).append("</td>");
            content.append("<td>").append(e.getPremiumRt()).append("</td>");
            content.append("<td>").append(e.getYearLeft()).append("</td>");
            content.append("<td>").append(e.getRatingCd()).append("</td>");
            CbTypeEnum resolve = CbTypeEnum.resolve(e.getCbType());
            content.append("<td>").append(resolve.getDesc()).append("</td>");
            content.append("</tr>");
        });
    }

    private void getEmailBuyedData(StringBuilder content, List<StockBondRecord> stockBondRecords, Map<String, ConvertibleBondDetailBean> detailMap) {
        content.append("当日购买信息：").append("</br>");
        content.append("<table border=\"5\" style=\"border:solid 1px #E8F2F9;font-size=12px;\">");
        //标题行
        content.append("<tr style=\"background-color: #428BCA; color:#ffffff\">");
        content.append("<th>可转债名字</th>");
        content.append("<th>可转债代码</th>");
        content.append("<th>购买价格</th>");
        content.append("<th>购买时间</th>");
        content.append("<th>最高价格</th>");
        content.append("<th>最低价格</th>");
        content.append("<th>当前价格</th>");
        content.append("<th>卖出价格</th>");

        content.append("<th>到期收益率</th>");
        content.append("<th>溢价率</th>");
        content.append("<th>剩余年限</th>");
        content.append("<th>评级</th>");
        content.append("<th>类型</th>");
        content.append("<th>类型排名</th>");
        content.append("</tr>");
        //生成数据
        generTable(content, stockBondRecords, detailMap);
        content.append("</table>");
    }

    private void generTable(StringBuilder content, List<StockBondRecord> stockBondRecords, Map<String, ConvertibleBondDetailBean> detailMap) {
        stockBondRecords.stream().forEach(e -> {
            content.append("<tr>");
            content.append("<td>").append(e.getBondNm()).append("</td>");
            content.append("<td>").append(e.getBondId()).append("</td>");
            content.append("<td>").append(e.getBuyPrice()).append("</td>");
            content.append("<td>").append(DateKit.toStr(e.getCreatedDt())).append("</td>");

            content.append("<td>").append(e.getMaxPrice()).append("</td>");
            content.append("<td>").append(e.getMinPrice()).append("</td>");
            content.append("<td>").append(e.getCurrPrice()).append("</td>");
            content.append("<td>").append(e.getSellPrice()).append("</td>");
            ConvertibleBondDetailBean convertibleBondDetailBean = detailMap.get(e.getBondId());
            if (convertibleBondDetailBean != null) {
                content.append("<td>").append(convertibleBondDetailBean.getYtmRtTax()).append("</td>");
                content.append("<td>").append(convertibleBondDetailBean.getPremiumRt()).append("</td>");
                content.append("<td>").append(convertibleBondDetailBean.getYearLeft()).append("</td>");
                content.append("<td>").append(convertibleBondDetailBean.getRatingCd()).append("</td>");
            } else {
                content.append("<td>").append("</td>");
                content.append("<td>").append("</td>");
                content.append("<td>").append("</td>");
                content.append("<td>").append("</td>");
            }

            CbTypeEnum resolve = CbTypeEnum.resolve(e.getCbType());
            content.append("<td>").append(resolve.getDesc()).append("</td>");
            content.append("<td>").append(e.getCbTypeIndex()).append("</td>");
            content.append("</tr>");
        });
    }


}
