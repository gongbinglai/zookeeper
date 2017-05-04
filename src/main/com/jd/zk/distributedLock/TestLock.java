package com.jd.zk.distributedLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * Created by weichen on 17/4/13.
 *
 * 此类；只是个测试 分布式锁 一个例子调用；
 * 模拟10个线程，同时请求同一个server
 * server根据分布式锁争抢情况，按着在zk 中创建临时节点从小及大，逐个执行；
 */
public class TestLock {

    private static final Logger LOG = LoggerFactory.getLogger(TestLock.class);
    
    private static final int THREAD_NUM = 3;
    public static CountDownLatch threadSemaphore = new CountDownLatch(THREAD_NUM);
    
//    private static final String CONNECTION_STRING = "192.168.80.201:2181";
//    private static final String GROUP_PATH = "/disLocks";
//    private static final String SUB_PATH = "/disLocks/sub";
//    private static final int SESSION_TIMEOUT = 10000;

    public static void main(String[] args) {

        /*模拟10个线程，争抢分布式锁，同时一起请求同一个服务*/
        for (int i = 0; i < THREAD_NUM; i++) {
            final int threadId = i;

            /* 模拟每个线程中，要做的事情 */
            final DoTemplate doTemplateTest = new DoTemplate() {
                public void dodo() {
                    try {
                        Thread.sleep(1000);
                        LOG.info(Thread.currentThread().getName() + " 我要修改一个文件 ... " + threadId);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };

           new Thread(new Runnable(){
               public void run() {
                   try {
                       new LockService().doService(doTemplateTest);
                   } catch (Exception e) {
                       LOG.error("【第"+threadId+"个线程】 抛出的异常：");
                       e.printStackTrace();
                   }
               }

           }).start();
        }

        try {
            threadSemaphore.await();
            Thread.sleep(1000);
            LOG.info("所有线程运行结束");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


