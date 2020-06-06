package cn.guba.igu8.convertibleBond.threads;

import cn.guba.igu8.convertibleBond.beans.ConvertibleBondBean;
import cn.guba.igu8.convertibleBond.beans.ConvertibleBondDetailBean;
import cn.guba.igu8.convertibleBond.enums.CbTypeEnum;
import cn.guba.igu8.convertibleBond.service.ConvertibleBondService;
import cn.guba.igu8.core.mail.MailFactory;
import cn.guba.igu8.stock.service.StockService;
import cn.guba.igu8.utils.DateUtils;
import com.jfinal.ext.kit.DateKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ConvertibleBond4PreMiumRtThread implements Runnable {


    private static final String EMAIL_ADDRESS = "369650047@qq.com";
    private static final String EMAIL_ADDRESS_SUB = "123660735@qq.com";
    private static final String EMAIL_TITLE = "【可转债】统计信息-溢价率";

    public static void main(String[] args) {
        ConvertibleBond4PreMiumRtThread t = new ConvertibleBond4PreMiumRtThread();
        t.run();
    }

    @Override
    public void run() {
        log.info("ConvertibleBondThread begin run !!!");
        try {
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

            coreRun();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("ConvertibleBondThread is end !!!");
    }

    public void coreRun() {
        //获取所有的可转债
        List<ConvertibleBondBean> convertibleBondBeans = ConvertibleBondService.getInstance().getConvertibleBondBeans();
        if (CollectionUtils.isEmpty(convertibleBondBeans)) {
            log.info("convertibleBondBeans is Empty");
            return;
        }

        //激进型列表
        List<ConvertibleBondDetailBean> redicalBeans = new ArrayList<>();

        //遍历获取处于转股期的且溢价率低的
        convertibleBondBeans.stream().forEach(e -> {
            ConvertibleBondDetailBean detailBean = e.getCell();
            if (detailBean.getRedeemDt() != null) {
                detailBean.setBondNm(detailBean.getBondNm() + "!");
            }


            //转股溢价率
            float premiumRt = Float.valueOf(detailBean.getPremiumRt().replace("%", ""));

            Date now = new Date();
            Date convertDt = detailBean.getConvertDt();
            if (premiumRt < 0 && now.after(convertDt)) {
                //激进型
                int cbType = detailBean.getCbType() + CbTypeEnum.REDICAL.getCode();
                detailBean.setCbType(cbType);
                redicalBeans.add(detailBean);
            }
        });

        redicalBeans.stream().sorted(new Comparator<ConvertibleBondDetailBean>() {
            @Override
            public int compare(ConvertibleBondDetailBean o1, ConvertibleBondDetailBean o2) {
                return o1.getPremiumRt().compareTo(o2.getPremiumRt());
            }
        });

        // 获取邮件内容
        String emailData = getEmailData(redicalBeans);
        log.info(emailData);
        //发送邮件
        String title = EMAIL_TITLE + DateKit.toStr(new Date());
        MailFactory.getInstance().sendEmail(EMAIL_ADDRESS, title, emailData);
        MailFactory.getInstance().sendEmail(EMAIL_ADDRESS_SUB, title, emailData);
    }

    /**
     * 获取邮件数据
     *
     * @param redicalBeans
     * @return
     */
    private String getEmailData(List<ConvertibleBondDetailBean> redicalBeans) {
        StringBuilder content = new StringBuilder("<html><head></head><body>");
        content.append("</br>");
        getEmailFilteredData(content, redicalBeans);
        content.append("</br>");
        content.append("</body></html>");
        return content.toString();
    }

    private void getEmailFilteredData(StringBuilder content,
                                      List<ConvertibleBondDetailBean> redicalBeans) {
        content.append("筛选条件：转股溢价率小于0%，并且已到转股期；").append("</br>");
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
        content.append("<th>剩余规模</th>");
        content.append("<th>评级</th>");
        content.append("<th>类型</th>");
        content.append("</tr>");
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
        list.stream().collect(Collectors.toList()).forEach(e -> {
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
            content.append("<td>").append(e.getCurrIssAmt()).append("</td>");
            content.append("<td>").append(e.getRatingCd()).append("</td>");
            CbTypeEnum resolve = CbTypeEnum.resolve(e.getCbType());
            content.append("<td>").append(resolve.getDesc()).append("</td>");
            content.append("</tr>");
        });
    }

}
