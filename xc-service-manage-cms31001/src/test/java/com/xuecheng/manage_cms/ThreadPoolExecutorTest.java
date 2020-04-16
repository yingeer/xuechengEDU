package com.xuecheng.manage_cms;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ThreadPoolExecutorTest {
    /**
     * 创建一个线程池(完整入参):
     * 核心线程数为5 (corePoolSize),
     * 最大线程数为10 (maximumPoolSize),
     * 存活时间为60分钟(keepAliveTime),
     * 工作队列为LinkedBlockingQueue (workQueue),
     * 线程工厂为默认的DefaultThreadFactory (threadFactory),
     * 饱和策略(拒绝策略)为AbortPolicy: 抛出异常(handler).
     */

    private static ExecutorService THREAD_POOL = new ThreadPoolExecutor(5, 10, 60, TimeUnit.MINUTES,
            new LinkedBlockingQueue<Runnable>(), Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());
    /**
     * 只有一个线程的线程池 没有超时时间, 工作队列使用无界的LinkedBlockingQueue
     */
    private static ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    // private static ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor(Executors.defaultThreadFactory());
    /**
     * 有固定线程的线程池(即corePoolSize = maximumPoolSize) 没有超时时间,
     * 工作队列使用无界的LinkedBlockingQueue
     */
    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
    // private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5, Executors.defaultThreadFactory());
    /**
     * 大小不限的线程池 核心线程数为0, 最大线程数为Integer.MAX_VALUE, 存活时间为60秒 该线程池可以无限扩展,
     * 并且当需求降低时会自动收缩, 工作队列使用同步移交SynchronousQueue.
     */
    private static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    // private static ExecutorService cachedThreadPool = Executors.newCachedThreadPool(Executors.defaultThreadFactory());
    /**
     * 给定的延迟之后运行任务, 或者定期执行任务的线程池
     */
    private static ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
    // private static ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5, Executors.defaultThreadFactory());

    public static void main(String args[]) throws Exception {
        /**
         * 例子1: 没有返回结果的异步任务
         */
        THREAD_POOL.submit(new Runnable() {
            @Override
            public void run() {
                // do something
                System.out.println("没有返回结果的异步任务");
            }
        });
        /**
         * 例子2: 有返回结果的异步任务
         */
        Future<List<String>> future = THREAD_POOL.submit(new Callable<List<String>>() {
            @Override
            public List<String> call() {
                List<String> result = new ArrayList<>();
                result.add("JoonWhee");
                return result;
            }
        });
        List<String> result = future.get(); // 获取返回结果
        System.out.println("有返回结果的异步任务: " + result);
        /**
         * 例子3:
         * 有延迟的, 周期性执行异步任务
         * 本例子为: 延迟1秒, 每2秒执行1次
         */
        scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("this is " + Thread.currentThread().getName());
            }
        }, 1, 2, TimeUnit.SECONDS);
        /**
         * 例子4: FutureTask的使用
         */
        Callable<String> task = new Callable<String>() {
            public String call() {
                return "JoonWhee";
            }
        };
        FutureTask<String> futureTo = new FutureTask<String>(task);
        THREAD_POOL.submit(futureTo);
        System.out.println(futureTo.get()); // 获取返回结果
//        System.out.println(futureTo.get(3, TimeUnit.SECONDS));  // 超时时间为3秒
    }
}


