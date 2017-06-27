package cn.guba.igu8.core.config;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;

import cn.guba.igu8.core.init.SysInit;
import cn.guba.igu8.core.interceptor.AuthInterceptor;
import cn.guba.igu8.db.mysqlModel._MappingKit;
import cn.guba.igu8.processor.igupiaoWeb.IgupiaoProcessor;
import cn.guba.igu8.processor.igupiaoWeb.service.IgpMsgService;
import cn.guba.igu8.web.content.controller.CommenedContentController;
import cn.guba.igu8.web.content.controller.ContentsController;
import cn.guba.igu8.web.index.controller.IndexController;
import cn.guba.igu8.web.user.controller.UserController;
import cn.guba.igu8.web.vip.controller.VipController;

/**
 * @author zongtao liu
 *
 */
public class MyConfig extends JFinalConfig {

	public static void main(String[] args) {
		JFinal.start("src/main/webapp", 80, "/", 5);
		// System.out.println(System.currentTimeMillis() + 1000l*
		// 60*60*24*365*100);//4649279698951
		// System.out.println("http://47.92.157.16/cc/158262".length());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jfinal.config.JFinalConfig#configConstant(com.jfinal.config.
	 * Constants)
	 */
	@Override
	public void configConstant(Constants me) {
		// 加载少量必要配置，随后可用PropKit.get(...)获取值
		PropKit.use("config.txt");
		me.setDevMode(PropKit.getBoolean("devMode", false));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jfinal.config.JFinalConfig#configRoute(com.jfinal.config.Routes)
	 */
	@Override
	public void configRoute(Routes me) {
		me.setBaseViewPath("/view");
		me.add("/", IndexController.class, "index");
		me.add("/ccc", CommenedContentController.class, "content");
		me.add("/cc", ContentsController.class, "content");
		me.add("/user", UserController.class);
		me.add("/vip", VipController.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jfinal.config.JFinalConfig#configEngine(com.jfinal.template.Engine)
	 */
	@Override
	public void configEngine(Engine me) {
		me.addSharedFunction("/view/common/_layout.html");
		me.addSharedFunction("/view/common/_paginate.html");
		me.addSharedFunction("/view/common/_include.html");
	}

	public static DruidPlugin createDruidPlugin() {
		return new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jfinal.config.JFinalConfig#configPlugin(com.jfinal.config.Plugins)
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jfinal.config.JFinalConfig#configInterceptor(com.jfinal.config.
	 * Interceptors)
	 */
	@Override
	public void configInterceptor(Interceptors me) {
		me.add(new AuthInterceptor());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jfinal.config.JFinalConfig#configHandler(com.jfinal.config.Handlers)
	 */
	@Override
	public void configHandler(Handlers me) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jfinal.config.JFinalConfig#afterJFinalStart()
	 */
	@Override
	public void afterJFinalStart() {
		super.afterJFinalStart();
		// 系统初始化
		SysInit.getInstance().init();
		// 初始化消息服务
		IgpMsgService.getInstance();
		// 爱股票信息系统初始化
		IgupiaoProcessor.getInstance();

	}

}
