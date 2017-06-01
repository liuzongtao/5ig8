/**
 * 
 */
package cn.guba.igu8.processor.igupiaoWeb.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zongtao liu
 *
 */
public class ThreadsManager {

	private static ExecutorService pool = Executors.newFixedThreadPool(10);

	private static volatile ThreadsManager threadsManager;

	private ThreadsManager() {
	}

	public static ThreadsManager getInstance() {
		if (threadsManager == null) {
			synchronized (ThreadsManager.class) {
				if (threadsManager == null) {
					threadsManager = new ThreadsManager();
				}
			}
		}
		return threadsManager;
	}

	public void addThread(Thread thread) {
		pool.execute(thread);
	}

}
