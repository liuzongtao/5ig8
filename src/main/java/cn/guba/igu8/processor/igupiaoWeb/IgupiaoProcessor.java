/**
 * 
 */
package cn.guba.igu8.processor.igupiaoWeb;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.nutz.http.Cookie;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.jfinal.kit.PropKit;

import cn.guba.igu8.core.utils.Util;
import cn.guba.igu8.processor.igupiaoWeb.account.IgpAccount;
import cn.guba.igu8.processor.igupiaoWeb.msg.IgpMsgFactory;
import cn.guba.igu8.processor.igupiaoWeb.threads.IgpUpdateLiverMsgThread;
import cn.guba.igu8.processor.igupiaoWeb.threads.VipCheckThread;

/**
 * @author zongtao liu
 *
 */
public class IgupiaoProcessor {

	private static Log log = Logs.get();

	private static volatile IgupiaoProcessor igupiaoProcessor;

	private IgupiaoProcessor() {
		initNonUser();
	}

	public static IgupiaoProcessor getInstance() {
		if (igupiaoProcessor == null) {
			synchronized (IgupiaoProcessor.class) {
				if (igupiaoProcessor == null) {
					igupiaoProcessor = new IgupiaoProcessor();
				}
			}
		}
		return igupiaoProcessor;
	}

	private void init() {
		log.info("IgupiaoProcessor init ;" + new Date());

		IgpAccount user = new IgpAccount(PropKit.get("igpAcc"), PropKit.get("igpPwd"));

		Cookie cookie = new Cookie();
		cookie.set("PHPSESSID",
				"jrlpvk0og3ticb9f9ategq4nu3;Hm_lvt_97a2c934daaed931f060269d1a9310de=1496714409,1496800440,1496802877; Hm_lpvt_97a2c934daaed931f060269d1a9310de=1496805866");

		user.login4Cokie(cookie);
		int uid = user.getUid(cookie);
		// 初始化老数据
		IgpMsgFactory.getInstance().initOldMsg(cookie, uid);

		ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
		long period = 24 * 60 * 60 * 1000l;
		executor.scheduleAtFixedRate(new VipCheckThread(), Util.getSurplusTime(8), period, TimeUnit.MILLISECONDS);
	}

	private void initNonUser() {
		log.info("IgupiaoProcessor initNonUser ;" + new Date());
		Cookie cookie = IgpMsgFactory.getInstance().getCookie();
		int uid = 0;
		// 初始化老数据
		IgpMsgFactory.getInstance().initOldMsg(cookie, uid);

		ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
		executor.scheduleAtFixedRate(new IgpUpdateLiverMsgThread(uid), 0, 30, TimeUnit.SECONDS);
		long period = 24 * 60 * 60 * 1000l;
		executor.scheduleAtFixedRate(new VipCheckThread(), Util.getSurplusTime(8), period, TimeUnit.MILLISECONDS);
	}
	
	

}
