package cn.guba.igu8.minsu.tj.service;

import cn.guba.igu8.core.constants.Constant;
import cn.guba.igu8.minsu.tj.bean.*;
import com.jfinal.kit.HttpKit;
import lombok.extern.slf4j.Slf4j;
import org.nutz.http.Header;
import org.nutz.http.Request;
import org.nutz.http.Response;
import org.nutz.http.Sender;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class TjInfoService {

    private static volatile TjInfoService instance;

    private TjInfoService() {
    }

    private static final String URL_TUJIA = "https://client.tujia.com/bingo/app/search/searchhousebyhouseidlist";
    private static final String USER_AGENT = "Mozilla/5.0 (Linux; Android 8.0.0; FRD-AL00 Build/HUAWEIFRD-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/70.0.3538.110 Mobile Safari/537.3";

    public static TjInfoService getInstance() {
        if (instance == null) {
            synchronized (TjInfoService.class) {
                if (instance == null) {
                    instance = new TjInfoService();
                }
            }
        }
        return instance;
    }

    private SearchHouseDto initSearchHouseDto(List<Integer> houseIdList) {
        HashMap<String, String> abTest = new HashMap<>();
        abTest.put("appUnitCard", "A");
        abTest.put("detailfive", null);
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime tomorrow = today.plusDays(1);
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern(Constant.DATE_FORMAT_MEDIUM);
        String todayStr = today.format(formatters);
        String tomorrowStr = tomorrow.format(formatters);

        SearchHouseParams searchHouseParams = SearchHouseParams.builder().abTest(abTest)
                .checkInDate(todayStr)
                .checkOutDate(tomorrowStr)
                .favorite(false)
                .houseIdList(houseIdList)
                .needUnactive(true)
                .noNeedPrice(false).build();

        HashMap<String, ABTestValueBean> abTestMap = new HashMap<>();
        abTestMap.put("login1909", ABTestValueBean.builder().s(false).v("A").build());
        abTestMap.put("DetailBookingTabABTest", ABTestValueBean.builder().s(false).v("A").build());
        String appVersion = "rtag-20191016-112420-yehongh_1";
        ClientBean client = ClientBean.builder().abTest(new Object())
                .abTests(abTestMap)
                .appId("com.tujia.hotel")
                .appVersion("213_213")
                .appVersionUpdate(appVersion)
                .buildTag(appVersion)
                .channelCode("huawei")
                .devModel("FRD-AL00")
                .devToken("")
                .devType(2)
                .locale("zh-CN")
                .oaId("")
                .osVersion("8.0.0")
                .screenInfo("44:6e:e5:ee:2f:e1,862915039201328")
                .sessionId("dae166a7-c8e2-3ecf-b0f7-35b64148991e_1572526999125")
                .tId("19092000364388722990")
                .uID("dae166a7-c8e2-3ecf-b0f7-35b64148991f").build();

        return SearchHouseDto.builder().parameter(searchHouseParams)
                .client(client).build();
    }

    private TjHouseInfoBean getTjHouseInfoBeanByKit(SearchHouseDto searchHouseDto) {
        String data = Json.toJson(searchHouseDto, JsonFormat.compact());
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("User-Agent", USER_AGENT);

        String content = HttpKit.post(URL_TUJIA, data, headers);

        log.info("content === " + content);

        return Json.fromJson(TjHouseInfoBean.class, content);
    }

    public List<TjUnitInfoBean> getTjUnitInfoBeans(List<Integer> houseIdList){
        SearchHouseDto searchHouseDto = initSearchHouseDto(houseIdList);
        log.info(Json.toJson(searchHouseDto));
        TjHouseInfoBean tjHouseInfoBean = getTjHouseInfoBeanByKit(searchHouseDto);
        return tjHouseInfoBean.getContent().getItems();
    }



    public static void main(String[] args) {
        TjInfoService instance = TjInfoService.getInstance();
        Integer[] houseIdArr = {11306079, 7754510, 1188052, 4142707, 4151184, 13932983, 16408960, 381709, 381759, 6507118, 13244848, 7767824, 10649689, 381631, 10579444, 3945475, 10579010, 5958430, 11210319, 5484138};
//        Integer[] houseIdArr = {11306079};
        List<Integer> houseIdList = Arrays.asList(houseIdArr);
        List<TjUnitInfoBean> items = instance.getTjUnitInfoBeans(houseIdList);
        for (TjUnitInfoBean tmpTjUnitInfoBean : items) {
            String productPrice = tmpTjUnitInfoBean.getProductPrice();
            String unitName = tmpTjUnitInfoBean.getUnitName();
            Boolean allowBooking = tmpTjUnitInfoBean.getAllowBooking();
            String unitInfor = tmpTjUnitInfoBean.getUnitInfor();
            log.info(unitName + " " + allowBooking + " " + productPrice);
        }
    }


}
