package com.test;

import com.xqmetting.server.MettingServerApplication;
import com.xqmetting.server.lock.zk.ZkDistributedLock;
import com.xqmetting.server.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MettingServerApplication.class)
public class TestZookeeper {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private CuratorFramework curatorFramework;

    @Value("${session.expireTime}")
    private int exipireTime;

    @Test
    public void testRedis(){
        log.info("测试redis!");
        redisTemplate.opsForValue().set("a","b");
        System.out.println(redisTemplate.opsForValue().get("a").toString());
    }

    @Test
    public void testZookeeper() throws Exception {
        Stat stat = curatorFramework.checkExists().forPath("/lock");
        if(stat == null){
            curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath("/lock");
        }
        curatorFramework.getData().usingWatcher(new CuratorWatcher() {
            @Override
            public void process(WatchedEvent event) throws Exception {
                System.out.println(event.getType());
                if(event.getType() == Watcher.Event.EventType.NodeDeleted){

                }
            }
        }).forPath("/lock");
        curatorFramework.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/lock/");
        List<String> list =  curatorFramework.getChildren().forPath("/lock");
        for (String child : list) {
            System.out.println(child);
        }
        System.in.read();
    }

    @Test
    public void testDistributedLock() throws Exception {
        int [] sums= new int[1];
        sums[0] = 0;

        for(int i = 0 ;i < 10; i++){
            Thread th = new Thread(()->{
                ZkDistributedLock zkDistributedLock =  SpringContextUtil.getBean(ZkDistributedLock.class);
                try{
                    zkDistributedLock.lock();
                    sums[0]++;
                    System.out.println(Thread.currentThread().getName()+",拿到锁,sum ="+sums[0]);
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    zkDistributedLock.unlock();
                }
            });
            th.start();
        }
        System.in.read();
    }

    @Test
    public void testCreateEphemeral() throws Exception {
        for(int i = 0 ; i < 10; i++){
            String currentPath =  curatorFramework.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/lock/");
            System.out.println(currentPath);
        }

    }

    @Test
    public void testExpireTime(){
        System.out.println(exipireTime);
    }

    @Test
    public void testCreateEphemeral2() throws Exception {
        curatorFramework.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/abc");
        System.in.read();
    }


}
