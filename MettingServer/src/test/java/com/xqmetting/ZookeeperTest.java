package com.xqmetting;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.*;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.CountDownLatch;

/**
 * @ClassName ZookeeperTest
 * @Description TODO
 * @Author admin
 * @Date 2022/10/24 14:27
 * @Version 1.0
 **/

public class ZookeeperTest {

    @Test
    public void test1() throws IOException, InterruptedException, KeeperException {
        String zkconnect = "114.55.64.45:2181,112.124.45.211:2181,112.124.45.211:2180";
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        ZooKeeper zooKeeper = new ZooKeeper(zkconnect, 4000, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                if(Event.KeeperState.SyncConnected == watchedEvent.getState()
                   && watchedEvent.getType() == Event.EventType.None){
                    countDownLatch.countDown();
                    System.out.println("连接成功！");
                }
            }
        });

        countDownLatch.await();

        zooKeeper.create("/user","fox".getBytes(Charset.forName("utf-8")), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    @Test
    public void test2(){
        long MAX_SEQUENCE = ~(-1L << 10);
        System.out.println(MAX_SEQUENCE);
    }
}
