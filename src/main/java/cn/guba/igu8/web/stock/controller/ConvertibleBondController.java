package cn.guba.igu8.web.stock.controller;

import cn.guba.igu8.convertibleBond.threads.ConvertibleBondThread;
import com.jfinal.core.Controller;
import org.nutz.lang.Strings;

public class ConvertibleBondController extends Controller {

    private static String SIGN = "23@sice!8e";

    public boolean run() {
        String sign = getPara("sign");
        if (Strings.isNotBlank(sign) && SIGN.equals(sign)) {
            ConvertibleBondThread thread = new ConvertibleBondThread();
            thread.coreRun();
        }
        return true;
    }
}
