package com.gupaoedu.lock;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/6</p>
 */
public class App {
    public static void main(String[] args) throws IOException {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                try {
                    countDownLatch.await();
                    DistributedLock distributedLock = new DistributedLock();
                    distributedLock.lock();//获得锁
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            },"Thread-" + i).start();
            countDownLatch.countDown();
        }
        System.in.read();
    }
}
