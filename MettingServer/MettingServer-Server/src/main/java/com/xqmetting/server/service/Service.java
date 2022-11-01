package com.xqmetting.server.service;


import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;


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


    public Service(){
        //默认工作线程
        if(socketConfig.getWorkerCount() == 0){
            socketConfig.setWorkerCount(Runtime.getRuntime().availableProcessors()+1);
        }
        //添加Jvm关闭时的钩子
        Runtime.getRuntime().addShutdownHook(new ShutdownHook(this));
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
