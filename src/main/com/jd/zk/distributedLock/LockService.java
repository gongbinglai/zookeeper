package com.jd.zk.distributedLock;

import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * Created by weichen on 17/4/13.
 */
public class LockService {


    public static CountDownLatch countDownLatch = new CountDownLatch(10);
    AbstractZookeeper abstractZookeeper = new AbstractZookeeper();

    /**
     * 被客户端调用的，需要争抢"分布式所"的方法
     * @param doTemplate
     */
    public void doService (DoTemplate doTemplate) {
        ZooKeeper zk = null;
        try {

            /* 1. 创建一个zk 对象 */
            zk = abstractZookeeper.connect("127.0.0.1:2181", 10000);
            /* 2. 创建一个分布式锁对象 */
            DistributedLock distributedLock = new DistributedLock(zk);
            /* 3. 根据 分布式锁对象 和 传入的具体的处理逻辑代码，创建一个锁监控 lockWatcher */
            LockWatcher lockWatcher = new LockWatcher(distributedLock, doTemplate);
            /* 4. 给 分布式锁（distributedLock）设置监听（lockWatcher） */
            distributedLock.setWatcher(lockWatcher);
            /* 5. 在zk服务器，创建 分布式锁 的跟目录 */
            distributedLock.createPath("/disLocks", "节点由线程" + Thread.currentThread().getName() + "创建");
            /* 6. 尝试获取 分布式锁，如果成功执行具体逻辑 */
            boolean rs = distributedLock.getLock();

            if (rs == true) {
                lockWatcher.doSomeThing();
                distributedLock.unlock();
            }

        }catch (Exception e) {
            e.printStackTrace();

        }
    }
}
