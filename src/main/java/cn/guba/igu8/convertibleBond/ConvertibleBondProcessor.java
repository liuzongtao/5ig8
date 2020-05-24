/**
 *
 */
package cn.guba.igu8.convertibleBond;

import cn.guba.igu8.convertibleBond.threads.ConvertibleBond4PreMiumRtThread;
import cn.guba.igu8.convertibleBond.threads.ConvertibleBondThread;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author zongtao liu
 */
public class ConvertibleBondProcessor {

    private static Log log = Logs.get();

    private static volatile ConvertibleBondProcessor convertibleBondProcessor;

    private ConvertibleBondProcessor() {
        init();
    }

    public static ConvertibleBondProcessor getInstance() {
        if (convertibleBondProcessor == null) {
            synchronized (ConvertibleBondProcessor.class) {
                if (convertibleBondProcessor == null) {
                    convertibleBondProcessor = new ConvertibleBondProcessor();
                }
            }
        }
        return convertibleBondProcessor;
    }


    private void init() {
        log.info("convertibleBondProcessor init ;" + new Date());

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 22);
        cal.set(Calendar.MINUTE, 15);
        long delay = (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
        if (delay < 0) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
            delay = (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
        }
        executor.scheduleAtFixedRate(new ConvertibleBondThread(), delay, 24 * 60 * 60, TimeUnit.SECONDS);
        cal.set(Calendar.HOUR_OF_DAY, 14);
        cal.set(Calendar.MINUTE, 55);
        long delay2 = (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
        if (delay2 < 0) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
            delay2 = (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
        }
        executor.scheduleAtFixedRate(new ConvertibleBond4PreMiumRtThread(), delay2, 24 * 60 * 60, TimeUnit.SECONDS);
        log.info("convertibleBondProcessor init ConvertibleBondThread is over ;" + new Date());
    }


}
