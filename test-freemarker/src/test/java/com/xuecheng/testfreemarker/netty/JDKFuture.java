package com.xuecheng.testfreemarker.netty;


import io.netty.util.concurrent.Promise;

import java.util.Date;
import java.util.concurrent.*;

public class JDKFuture {

    static ExecutorService executors = new ThreadPoolExecutor(1,
            1,
            60,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(16));

    public static void main(String[] args) throws Exception {
        int cnt = 4;
        Future[] jdkFuture=new Future[cnt];
        Object jdkFutureResult;
        for(int i = 0;i < cnt; i++) {
            jdkFuture[i] = executors.submit(new JDKCallable(i));
        }
        System.out.println(String.format("%s 在 %s 即将获取任务执行结果", Thread.currentThread(), new Date()));
        jdkFutureResult = jdkFuture[3].get();
        System.out.println(String.format("%s 在 %s 任务结果获取完毕 %s", Thread.currentThread(), new Date(), jdkFutureResult));
        executors.shutdown();
    }

    static class JDKCallable implements Callable {

        int index;

        JDKCallable(int ind){
            this.index = ind;
        }

        public Object call() throws Exception {
            try {
                System.out.println(String.format("线程 [%s] 提交任务[%s]", Thread.currentThread(), this.index));
                // 耗时2秒,模拟耗时操作
                Thread.sleep(2000);
                System.out.println(String.format("线程 [%s] 执行任务[%s]执行完毕", Thread.currentThread(), this.index));

            }catch(InterruptedException e){
                e.printStackTrace();
            }
            return String.format("任务%s执行结果",this.index);
        }
    }

}
