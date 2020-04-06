package cn.guba.igu8.convertibleBond.service;

import cn.guba.igu8.convertibleBond.beans.ConvertibleBondBean;
import cn.guba.igu8.convertibleBond.beans.ConvertibleBondsBean;
import com.jfinal.kit.HttpKit;
import lombok.extern.slf4j.Slf4j;
import org.nutz.json.Json;

import java.util.List;

@Slf4j
public class ConvertibleBondService {

    private static final String URL_JISILU = "https://www.jisilu.cn/data/cbnew/cb_list/?___jsl=LST___t=";

    private static volatile ConvertibleBondService instance;

    private ConvertibleBondService() {
    }

    public static ConvertibleBondService getInstance() {
        if (instance == null) {
            synchronized (ConvertibleBondService.class) {
                if (instance == null) {
                    instance = new ConvertibleBondService();
                }
            }
        }
        return instance;
    }

    public List<ConvertibleBondBean> getConvertibleBondBeans(){
        ConvertibleBondsBean convertibleBondsBean = getConvertibleBondsBeanByKit();
        return convertibleBondsBean.getRows();
    }

    private ConvertibleBondsBean getConvertibleBondsBeanByKit() {
        String url = URL_JISILU + System.currentTimeMillis();
        String param = "is_search=N&btype=C&listed=Y&rp=50";
        String content = HttpKit.post(url, param);

        log.info("content === " + content);
        return Json.fromJson(ConvertibleBondsBean.class, content);
    }


    public static void main(String[] args) {
        ConvertibleBondService cbs = new ConvertibleBondService();
        ConvertibleBondsBean convertibleBondsBean = cbs.getConvertibleBondsBeanByKit();
        String bondId = convertibleBondsBean.getRows().get(0).getCell().getBondId();
        System.out.println(bondId);
        System.out.println(convertibleBondsBean.getTotal());
    }


}
