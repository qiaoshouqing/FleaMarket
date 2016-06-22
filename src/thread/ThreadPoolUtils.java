package thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ÎÒÃÇÍøÂçÇëÇóÏß³Ì³Ø:ÏÞÖÆ²¢ÐÐµÄÍøÂçÇëÇóÏß³Ì¡£
 * */

public class ThreadPoolUtils {

	private ThreadPoolUtils() {
	}

	// ¶¨ÒåºËÐÄÏß³ÌÊý£¬²¢ÐÐÏß³ÌÊý
	private static int CORE_POOL_SIZE = 3;

	// Ïß³Ì³Ø×î´óÏß³ÌÊý£º³ýÁËÕýÔÚÔËÐÐµÄÏß³Ì¶îÍâ±£´æ¶àÉÙ¸öÏß³Ì
	private static int MAX_POOL_SIZE = 200;

	// ¶îÍâÏß³Ì¿ÕÏÐ×´Ì¬Éú´æÊ±¼ä
	private static int KEEP_ALIVE_TIME = 5000;

	// ×èÈû¶ÓÁÐ¡£µ±ºËÐÄÏß³Ì¶ÓÁÐÂúÁË·ÅÈëµÄ
	// ³õÊ¼»¯Ò»¸ö´óÐ¡Îª10µÄ·ºÐÍÎªRunnableµÄ¶ÓÁÐ
	private static BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(
			10);
	// Ïß³Ì¹¤³§,°Ñ´«µÝ½øÀ´µÄrunnable¶ÔÏóÉú³ÉÒ»¸öThread
	private static ThreadFactory threadFactory = new ThreadFactory() {

		// Ô­×ÓÐÍµÄinteger±äÁ¿Éú³ÉµÄintegerÖµ²»»áÖØ¸´
		private final AtomicInteger ineger = new AtomicInteger();

		@Override
		public Thread newThread(Runnable arg0) {
			return new Thread(arg0, "MyThreadPool thread:"
					+ ineger.getAndIncrement());
		}
	};

	// µ±Ïß³Ì³Ø·¢ÉúÒì³£µÄÊ±ºò»Øµ÷½øÈë
	private static RejectedExecutionHandler handler = new RejectedExecutionHandler() {
		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			// ½øÐÐÖØÆô²Ù×÷
		}

	};
	// Ïß³Ì³ØThreadPoolExecutor java×Ô´øµÄÏß³Ì³Ø
	private static ThreadPoolExecutor threadpool;
	// ¾²Ì¬´úÂë¿é£¬ÔÚÀà±»¼ÓÔØµÄÊ±ºò½øÈë
	static {
		threadpool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
				KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue, threadFactory,
				handler);
	}

	public static void execute(Runnable runnable) {
		threadpool.execute(runnable);
	}
}
