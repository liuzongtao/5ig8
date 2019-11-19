/**
 *
 */
package cn.guba.igu8.minsu.tj.threads;

import cn.guba.igu8.core.mail.MailFactory;
import cn.guba.igu8.db.dao.TjDealInfoDao;
import cn.guba.igu8.db.dao.TjUnitInfoDao;
import cn.guba.igu8.db.mysqlModel.TjDealInfo;
import cn.guba.igu8.db.mysqlModel.TjUnitInfo;
import cn.guba.igu8.minsu.tj.bean.TjUnitInfoBean;
import cn.guba.igu8.minsu.tj.service.TjInfoService;
import com.google.common.collect.Lists;
import com.jfinal.ext.kit.DateKit;
import org.apache.commons.collections4.CollectionUtils;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author zongtao liu
 */
public class MinsuInfoThread implements Runnable {

    private static Log log = Logs.get();

    private static final Integer[] HOUSE_ID_ARR = {16883707, 7758472, 13850383, 15700903, 13089679, 13027204, 12000885, 12346055, 4255729, 1157600, 9847279, 9587397, 1759772, 14665771, 13693933, 11942890, 13749163, 11512075, 11512033, 11485419, 11301060, 1182010, 1119709, 1121983, 1997737, 10574810, 12378360, 13900517, 16188194, 16751141, 10479274, 16117074, 8945301, 381709, 6507118, 7767824, 13244848, 16500744, 10649689, 381759, 3946854, 5484138, 381631, 3916068, 11821839, 12734044, 5225985, 11822189, 11210319, 10579444, 3945475, 5958430, 10579010, 11306079, 16408960};

    private static final String EMAIL_ADDRESS = "369650047@qq.com";
    private static final String EMAIL_TITLE = "【民宿】统计信息";

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        Random random = new Random();
        int time = random.nextInt(10);
        try {
            TimeUnit.MINUTES.sleep(time);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Integer> houseIdList = Arrays.asList(HOUSE_ID_ARR);
        int size = 20;
        List<Integer> allList = houseIdList.subList(0, houseIdList.size() > size ? size : houseIdList.size());
        List<List<Integer>> listPar = Lists.partition(allList, size);
        for (List<Integer> list : listPar) {
            updateInfo(list);
        }
        // 统计近30天的数据，发送邮件
        int dayNum = 30;
        Date today = new Date();
        String title = EMAIL_TITLE + DateKit.toStr(today);
        String latestData = getLatestData(dayNum);
        MailFactory.getInstance().sendEmail(EMAIL_ADDRESS, title, latestData);
    }

    /**
     * 获取最近的数据
     *
     * @param dayNum
     * @return
     */
    private String getLatestData(int dayNum) {
        //获取每天数据
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -dayNum);
        Map<Long, List<TjDealInfo>> allTjDealInfos = new TreeMap<>();
        for (int i = dayNum; i > 0; i--) {
            List<TjDealInfo> list = TjDealInfoDao.getInfoListByDate(cal.getTime());
            cal.add(Calendar.DAY_OF_MONTH, 1);
            if (CollectionUtils.isEmpty(list)) {
                continue;
            }
            for (TjDealInfo tmpTjDealInfo : list) {
                Long unitId = tmpTjDealInfo.getUnitId();
                List<TjDealInfo> tjDealInfos = allTjDealInfos.get(unitId);
                if (tjDealInfos == null) {
                    tjDealInfos = new ArrayList<>(dayNum);
                }
                tjDealInfos.add(dayNum - i, tmpTjDealInfo);
            }
        }
        //组装为字符串
        return formatTable(allTjDealInfos, dayNum);
    }

    /***
     * 格式化数据
     * @param allTjDealInfos
     * @param dayNum
     * @return
     */
    private String formatTable(Map<Long, List<TjDealInfo>> allTjDealInfos, int dayNum) {
        StringBuilder content = new StringBuilder("<html><head></head><body>");
        content.append("<table border=\"5\" style=\"border:solid 1px #E8F2F9;font-size=14px;;font-size:18px;\">");
        //标题行
        content.append("<tr style=\"background-color: #428BCA; color:#ffffff\"><th>名称</th>");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -dayNum);
        for (int i = dayNum; i > 0; i--) {
            content.append("<th>" + DateKit.toStr(cal.getTime()) + "</th>");
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        content.append("</tr>");

        for (Map.Entry<Long, List<TjDealInfo>> entry : allTjDealInfos.entrySet()) {
            content.append("<tr>");
            Long unitId = entry.getKey();
            TjUnitInfo tjUnitInfo = TjUnitInfoDao.selectByUnitId(unitId);
            //第一列
            content.append("<td>" + tjUnitInfo.getUnitName() + "</td>");
            List<TjDealInfo> tjDealInfoList = entry.getValue();
            for (TjDealInfo tjDealInfo : tjDealInfoList) {
                content.append("<td>" + tjDealInfo.getFinalPrice() + "</td>");
            }
            content.append("</tr>");
        }

        content.append("</table>");
        content.append("</body></html>");
        return content.toString();
    }


    /**
     * 更新信息
     */
    private void updateInfo(List<Integer> list) {
        // 获取信息
        List<TjUnitInfoBean> tjUnitInfoBeans = TjInfoService.getInstance().getTjUnitInfoBeans(list);

        // 遍历更新 单元信息表和 入住信息表
        for (TjUnitInfoBean tmpTjUnitInfoBean : tjUnitInfoBeans) {
            Long unitId = Long.valueOf(tmpTjUnitInfoBean.getUnitId());
            TjUnitInfo tjUnitInfo = TjUnitInfoDao.selectByUnitId(unitId);
            if (tjUnitInfo == null) {
                // 插入新的单元信息
                TjUnitInfo tmpTjUnitInfo = new TjUnitInfo();
                tmpTjUnitInfo.setUnitId(Long.valueOf(unitId));
                tmpTjUnitInfo.setUnitName(tmpTjUnitInfoBean.getUnitName());
                tmpTjUnitInfo.setAddress(tmpTjUnitInfoBean.getAddress());
                tmpTjUnitInfo.setCityId(tmpTjUnitInfoBean.getCityId());
                tmpTjUnitInfo.setCityName(tmpTjUnitInfoBean.getCityName());
                tmpTjUnitInfo.setDitrictName(tmpTjUnitInfoBean.getDistrictName());
                tmpTjUnitInfo.setLongitude(tmpTjUnitInfoBean.getLongitude());
                tmpTjUnitInfo.setLatitude(tmpTjUnitInfoBean.getLatitude());
                TjUnitInfoDao.insert(tmpTjUnitInfo);
            }

            //添加入住信息
            TjDealInfo tmpTjDealInfo = new TjDealInfo();
            tmpTjDealInfo.setUnitId(unitId);
            tmpTjDealInfo.setProductPrice(tmpTjUnitInfoBean.getProductPrice());
            if (!tmpTjUnitInfoBean.getAllowBooking()) {
                tmpTjDealInfo.setFinalPrice(tmpTjUnitInfoBean.getFinalPrice());
            } else {
                tmpTjDealInfo.setFinalPrice("0");
            }
            TjDealInfoDao.insert(tmpTjDealInfo);
        }

    }


}
