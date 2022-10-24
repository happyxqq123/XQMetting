package com.xqmetting.tool;

import lombok.Data;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.client.ZKClientConfig;

import java.io.IOException;

/**
 * @ClassName SnowflakeIdWorker
 * @Description TODO
 * @Author admin
 * @Date 2022/10/24 13:34
 * @Version 1.0
 **/
@Data
public class SnowflakeIdWorker {

    /**
     * 114.55.64.45
     * 112.124.45.211
     */

    //zk客户端
    private CuratorFramework  zkClient = null;

    public static SnowflakeIdWorker instance = new SnowflakeIdWorker();

    private static final String zkconnect = "114.55.64.45:2181,112.124.45.211:2181,112.124.45.211:2180";

    private static final int timeout = 15000;

    /**
     * 初始化单例
     */
    public synchronized void init(long workerId){
        if(workerId > MAX_WORKER_ID){
            //zk分配的workerId过大
            throw new IllegalArgumentException("worker Id worng:"+workerId);
        }
        instance.workerId = workerId;
    }


    public SnowflakeIdWorker(){
        try {
            //重试策略
            ExponentialBackoffRetry exponentialBackoffRetry = new ExponentialBackoffRetry(1000,3,3);
            zkClient = CuratorFrameworkFactory.newClient(zkconnect,3000,3000,exponentialBackoffRetry);
            //启动客户端
            zkClient.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 开始使用算法时间为:2017-01-01 00:00:00
     */
    private static final long START_TIME = 1483200000000L;

    /**
     * worker id的bit数,最多支持8192个节点
     */
    private static final int WORKER_ID_BITS = 13;

    /**
     * 序列号，支持单节点最高毫秒的最大ID数1024
     */
    private static final int SEQUENCE_BITS = 10;
    /**
     * 最大的worker id,
     */
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    /**
     * 最大的序列号,1023
     * -1的补码（二进制全是1）右移10位，然后取反
     */
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);
    /**
     * worker 节点编号的移位
     */
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    /**
     * 时间戳的移位
     */
    private final static long TIMESTAMP_LEFT_SHIFT =WORKER_ID_BITS + SEQUENCE_BITS;
    /**
     * 该项目的worker 节点
     */
    private long workerId;
    /**
     * 上次生成ID的时间戳
     */
    private long lastTimeStamp = -1L;
    /**
     * 当前毫秒生成的序列
     */
    private long sequence = 0L;

    public Long nextId(){
        return generateId();
    }

    /**
     *
     * @return
     */
    private synchronized long generateId(){
        long current = System.currentTimeMillis();
        if(current < lastTimeStamp){
            //如果当前时间小于上一次ID生成时间戳，说明系统时钟回退过，出现问题返回-1
            return -1;
        }
        if(current == lastTimeStamp){
            //如果当前生成id的时间还是上次的时间，那么兑sequence序列号进行+1
            sequence = (sequence+1) & MAX_SEQUENCE;

            if(sequence == MAX_SEQUENCE){
                //当前毫秒生成序列数已经大于最大值，那么阻塞到下一毫秒再获取新的时间戳
                current = this.nextMs(lastTimeStamp);
            }
        }else{
            //当前的时间戳已经是下一秒了
            sequence = 0L;
        }

        //更新上次生成id的时间戳
        lastTimeStamp = current;
        //进行移位操作生成int64的唯一ID
        //时间戳右移动23位
        long time = (current-START_TIME) << TIMESTAMP_LEFT_SHIFT;
        //workerId 右移10位
        long workerId = this.workerId <<WORKER_ID_SHIFT;
        return time|workerId|sequence;
    }

    /**
     * 阻塞到下一毫秒
     * @param timeStamp
     * @return
     */
    private long nextMs(long timeStamp){
        long current = System.currentTimeMillis();
        while(current <= timeStamp){
            current = System.currentTimeMillis();
        }
        return current;
    }




}
