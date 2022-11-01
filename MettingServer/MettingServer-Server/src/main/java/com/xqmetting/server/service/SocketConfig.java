package com.xqmetting.server.service;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "server")
@Component
@ToString
@Data
public class SocketConfig {
    protected  String ip;
    protected  int port;
    protected  int socketType;        //socket类型 1普通socket 2 websocket
    protected  boolean keepAlive;      //是否启用keepAlive true or false
    protected  boolean tcpNoDelay;     //是否启用tcpNoDelay
    protected  int workerCount;       //工作线程池大小
    protected  boolean openExecutor;   //是否开启业务处理线程池
    protected  int corePoolSize;      //消息事件业务处理线程池最小保持线程数
    protected  int maximumPoolSize;  //消息事件业务处理线程池最大线程数
    protected  int queueCapacity;  //消息事件业务处理线程池队列最大值
    protected  boolean checkHeartbeat;    //是否开启心跳检查
    protected  int readerIdleTimeSeconds;  //心跳检查时读空闲事件 s
    protected  int writerIdleTimeSeconds;  //心跳检查时的写空间时间
    protected  int allIdleTimeSeconds;    //心跳检查的读写空闲时间

}
