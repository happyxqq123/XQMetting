package com.xqmetting.server.lock.zk;

import com.xqmetting.server.lock.AbstractLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * zookeeper实现的分布式锁
 */
@Slf4j
@Component
@Scope("prototype")
public class ZkDistributedLock extends AbstractLock implements InitializingBean,CuratorCacheListener {

    @Resource
    private CuratorFramework curatorFramework;
    private String path = "/lock";
    private String config;

    private CountDownLatch countDownLatch = new CountDownLatch(1);
    private String beforePath; //当前节点前一个节点
    private String currentPath; //当前节点

    public void init(){
        Stat stat = null;
        try {
            synchronized(ZkDistributedLock.class){
                stat = curatorFramework.checkExists().forPath(path);
                if(stat == null){
                    curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath(path);
                    log.info("创建/lock节点成功!");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("创建并发锁/lock节点失败,{}",stat);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void lock() {
        //尝试获取锁
        boolean locked = tryLock();
        while(!locked){
            //等待锁 阻塞
            waitLock();
            //重试
            //获取等待的子节点列表
            try {
                List<String> childrens = curatorFramework.getChildren().forPath(path);
                if(checkLocked(childrens)){
                    locked = true;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected boolean tryLock() {
        try{
            //创建临时有序的节点 -e -s
            currentPath = curatorFramework.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path+"/");
            //获取到所有子节点
            List<String> childrens =  curatorFramework.getChildren().forPath("/lock");
            //获取等待的子节点列表 ，判断自己是否是第一个
            if(checkLocked(childrens)){
                return true;
            }
            //若不是第一个，则找到自己的前一个节点
            int index = Collections.binarySearch(childrens,currentPath.substring(currentPath.lastIndexOf("/")+1));
            if(index < 0){
                throw new RuntimeException(currentPath+" 节点没找到");
            }
            //如果自己没有获得锁，则要监听前一个节点
            beforePath = path+"/"+childrens.get(index-1);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkLocked(List<String> childrens){
        //节点按照编号，升序排列
        Collections.sort(childrens);
        if(currentPath.equals(path+"/"+childrens.get(0))){
            return true;
        }
        return false;
    }

    @Override
    protected void waitLock() {
        try{
            Stat stat = curatorFramework.checkExists().forPath(beforePath);
            if(stat != null){
                //订阅比自己小顺序节点的删除事件 index-1
                CuratorCache curatorCache = CuratorCache.build(curatorFramework,beforePath, CuratorCache.Options.SINGLE_NODE_CACHE);
                curatorCache.start();
                curatorCache.listenable().addListener(this);
                countDownLatch.await();
                curatorCache.listenable().removeListener(this);
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unlock() {
        //删除临时节点
        try {
            curatorFramework.delete().forPath(currentPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    @Override
    public void event(Type type, ChildData oldData, ChildData data) {
        if(type == Type.NODE_DELETED){
            countDownLatch.countDown();
        }
    }
}
