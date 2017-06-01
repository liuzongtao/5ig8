/**
 * 
 */
package cn.guba.igu8.processor.igupiaoWeb;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.nutz.http.Cookie;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import cn.guba.igu8.processor.igupiaoWeb.account.IgpAccount;
import cn.guba.igu8.processor.igupiaoWeb.msg.IgpMsgFactory;
import cn.guba.igu8.processor.igupiaoWeb.threads.IgpUpdateLiverMsgThread;


/**
 * @author zongtao liu
 *
 */
public class IgupiaoProcessor {
	
	private static Log log = Logs.get();
	
	private static volatile IgupiaoProcessor igupiaoProcessor;
	
	private IgupiaoProcessor(){
		init();
	}
	
	public static IgupiaoProcessor getInstance(){
		if(igupiaoProcessor == null){
			synchronized (IgupiaoProcessor.class) {
				if(igupiaoProcessor == null){
					igupiaoProcessor = new IgupiaoProcessor();
				}
			}
		}
		return igupiaoProcessor;
	}
	
	private void init(){
		log.info("IgupiaoProcessor init ;" + new Date());
		
		IgpAccount user = new IgpAccount("18701641809", "aayueya123");

		Cookie cookie = new Cookie();
		cookie.set("PHPSESSID",
				"nlfgvh6a1pa93ol1uaq31pdld3; Hm_lvt_97a2c934daaed931f060269d1a9310de=1494835149; Hm_lpvt_97a2c934daaed931f060269d1a9310de=1494838876");

		int uid = user.getUid(cookie);
		//初始化老数据
		IgpMsgFactory.getInstance().initOldMsg(cookie, uid);
		
		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(new IgpUpdateLiverMsgThread(cookie,uid), 0, 30, TimeUnit.SECONDS);
	}

}
