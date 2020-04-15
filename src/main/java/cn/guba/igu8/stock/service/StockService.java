package cn.guba.igu8.stock.service;

import cn.guba.igu8.stock.beans.StockRestBean;
import com.jfinal.kit.HttpKit;
import lombok.extern.slf4j.Slf4j;
import org.nutz.json.Json;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class StockService {

    private static String REST_URL = "https://www.jisilu.cn/data/calendar/get_calendar_data/?qtype=OTHER&start=%d&end=%d&_=%d";

    private static volatile StockService instance;

    private StockService() {
    }

    public static StockService getInstance() {
        if (instance == null) {
            synchronized (StockService.class) {
                if (instance == null) {
                    instance = new StockService();
                }
            }
        }
        return instance;
    }

    public boolean isRest() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        long begin = cal.getTimeInMillis() / 1000;
        cal.add(Calendar.DAY_OF_MONTH, 1);
        long end = cal.getTimeInMillis() / 1000;
        String url = String.format(REST_URL, begin, end, System.currentTimeMillis());
        String content = HttpKit.post(url, null);

        log.info("StockService.isRest.content === " + content);
        List<StockRestBean> stockRestBeans = Json.fromJsonAsList(StockRestBean.class, content);
        List<StockRestBean> beans = stockRestBeans.stream()
                .filter(e -> e.getTitle()
                        .contains("A股休市")
                        && e.getStart().getTime() < end * 1000
                        && e.getStart().getTime() > begin * 1000)
                .collect(Collectors.toList());
        return beans.size() > 0 ? true : false;
    }
}
