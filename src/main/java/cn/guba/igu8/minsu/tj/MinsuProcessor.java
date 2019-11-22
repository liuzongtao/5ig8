/**
 *
 */
package cn.guba.igu8.minsu.tj;

import cn.guba.igu8.minsu.tj.threads.MinsuInfoThread;
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
public class MinsuProcessor {

    private static Log log = Logs.get();

    private static volatile MinsuProcessor minsuProcessor;

    private MinsuProcessor() {
        init();
    }

    public static MinsuProcessor getInstance() {
        if (minsuProcessor == null) {
            synchronized (MinsuProcessor.class) {
                if (minsuProcessor == null) {
                    minsuProcessor = new MinsuProcessor();
                }
            }
        }
        return minsuProcessor;
    }


    private void init() {
        log.info("MinsuProcessor init ;" + new Date());

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 30);
        long delay = (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
        if (delay < 0) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
            delay = (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
        }
        executor.scheduleAtFixedRate(new MinsuInfoThread(), delay, 24 * 60 * 60, TimeUnit.SECONDS);
        log.info("MinsuProcessor init MinsuInfoThread is over ;" + new Date());
    }


}
