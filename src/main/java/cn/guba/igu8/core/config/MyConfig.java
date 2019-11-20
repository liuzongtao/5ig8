package cn.guba.igu8.core.config;

import cn.guba.igu8.core.init.SysInit;
import cn.guba.igu8.core.interceptor.AuthInterceptor;
import cn.guba.igu8.db.mysqlModel._MappingKit;
import cn.guba.igu8.minsu.tj.MinsuProcessor;
import cn.guba.igu8.processor.igupiaoWeb.IgupiaoProcessor;
import cn.guba.igu8.processor.igupiaoWeb.service.IgpMsgService;
import cn.guba.igu8.web.content.controller.CommenedContentController;
import cn.guba.igu8.web.content.controller.ContentsController;
import cn.guba.igu8.web.index.controller.IndexController;
import cn.guba.igu8.web.log.controller.LogController;
import cn.guba.igu8.web.user.controller.UserController;
import cn.guba.igu8.web.vip.controller.VipController;
import com.jfinal.config.*;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zongtao liu
 */
@Slf4j
public class MyConfig extends JFinalConfig {


    public static void main(String[] args) {
//        JFinal.start("src/main/webapp", 8080, "/",5);
//		线上
        JFinal.start("src/main/webapp", 80, "/", 5);
        // System.out.println(System.currentTimeMillis() + 1000l*
        // 60*60*24*365*100);//4649279698951
        // System.out.println("http://47.92.157.16/cc/158262".length());
    }

    @Override
    public void configConstant(Constants me) {
        // 加载少量必要配置，随后可用PropKit.get(...)获取值
        PropKit.use("config.txt");
        me.setDevMode(PropKit.getBoolean("devMode", false));

    }

    @Override
    public void configRoute(Routes me) {
        me.setBaseViewPath("/view");
        me.add("/", IndexController.class, "index");
        me.add("/ccc", CommenedContentController.class, "content");
        me.add("/cc", ContentsController.class, "content");
        me.add("/user", UserController.class);
        me.add("/vip", VipController.class);
        me.add("/log", LogController.class);
    }

    @Override
    public void configEngine(Engine me) {
        me.addSharedFunction("/view/common/_layout.html");
        me.addSharedFunction("/view/common/_paginate.html");
        me.addSharedFunction("/view/common/_include.html");
    }

    public static DruidPlugin createDruidPlugin() {
        return new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
    }

    @Override
    public void configPlugin(Plugins me) {
        // 配置C3p0数据库连接池插件
        DruidPlugin druidPlugin = createDruidPlugin();
        me.add(druidPlugin);

        // 配置ActiveRecord插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        // 所有映射在 MappingKit 中自动化搞定
        _MappingKit.mapping(arp);
        me.add(arp);
    }

    @Override
    public void configInterceptor(Interceptors me) {
        me.add(new AuthInterceptor());

    }

    @Override
    public void configHandler(Handlers handlers) {

    }


    @Override
    public void onStart() {
        // 系统初始化
        SysInit.getInstance().init();
        // 初始化消息服务
        IgpMsgService.getInstance();
        // 爱股票信息系统初始化
        IgupiaoProcessor.getInstance();
        // 初始化民宿信息
        MinsuProcessor.getInstance();
        log.info(" ======= onStart init end =======");
    }

}
