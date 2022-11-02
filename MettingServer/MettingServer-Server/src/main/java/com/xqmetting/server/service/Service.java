package com.xqmetting.server.service;


import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.*;


public abstract class Service {

    @Resource
    protected SocketConfig socketConfig;

    /**
     * 消息事件业务处理线程池
     */
    protected ExecutorService messageExecutor;

    /**
     * 通道事件业务处理线程池
     */
    protected ExecutorService channelExecutor;

    /**
     * 异常事件业务处理线程池
     */
    protected ExecutorService exceptionExecutor;

    protected ChannelInboundHandlerAdapter heartbeatHandler;


    public Service(){

    }

    protected void init(){
        //默认工作线程
        if(socketConfig.getWorkerCount() == 0){
            socketConfig.setWorkerCount(Runtime.getRuntime().availableProcessors()+1);
        }
        //添加Jvm关闭时的钩子
        Runtime.getRuntime().addShutdownHook(new ShutdownHook(this));

        if(socketConfig.openExecutor){
            messageExecutor = new ThreadPoolExecutor(
                    this.socketConfig.corePoolSize,
                    this.socketConfig.maximumPoolSize,
                    60L,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(socketConfig.queueCapacity),
                    new BasicThreadFactory.Builder().namingPattern("MessageEventProcessor-%d").daemon(true).build(),
                    new ThreadPoolExecutor.AbortPolicy());
            exceptionExecutor = Executors.newCachedThreadPool(
                    new BasicThreadFactory.Builder().namingPattern("ExceptionEventProcessor-%d").daemon(true).build());
            channelExecutor = Executors.newCachedThreadPool(
                    new BasicThreadFactory.Builder().namingPattern("ChannelEventProcessor-%d").daemon(true).build());
        }
    }

    public void setExecutor(ExecutorService executor){
        if(executor == null){
            throw new NullPointerException("executor is null.");
        }
        ExecutorService preExecutor = this.messageExecutor;
        this.messageExecutor = executor;
        List<Runnable> tasks = preExecutor.shutdownNow();
        for(Runnable task : tasks){
            this.messageExecutor.execute(task);
        }
    }

    public int getExecutorActiveCount(){
        if(messageExecutor instanceof ThreadPoolExecutor){
            return ((ThreadPoolExecutor) messageExecutor).getActiveCount();
        }
        return -1;
    }

    public long getExecutorCompletedTaskCount(){
        if(messageExecutor instanceof  ThreadPoolExecutor){
            return ((ThreadPoolExecutor) messageExecutor).getCompletedTaskCount();
        }
        return -1;
    }

    public int getExecutorLargestPoolSize(){
        if(messageExecutor instanceof ThreadPoolExecutor){
            return ((ThreadPoolExecutor) messageExecutor).getLargestPoolSize();
        }
        return -1;
    }

    public int getExecutorPoolSize(){
        if(messageExecutor instanceof ThreadPoolExecutor){
            return ((ThreadPoolExecutor) messageExecutor).getPoolSize();
        }
        return -1;
    }

    public long getExecutorTaskCount() {
        if (messageExecutor instanceof ThreadPoolExecutor) {
            return ((ThreadPoolExecutor) messageExecutor).getTaskCount();
        }
        return -1;
    }

    public int getExecutorQueueSize() {
        if (messageExecutor instanceof ThreadPoolExecutor) {
            return ((ThreadPoolExecutor) messageExecutor).getQueue().size();
        }
        return -1;
    }





    public abstract void shutdown();

    class ShutdownHook extends Thread{
        private Service service;

        public ShutdownHook(Service service){
            this.service = service;
        }

        @Override
        public void run() {
            service.shutdown();
        }
    }


}
