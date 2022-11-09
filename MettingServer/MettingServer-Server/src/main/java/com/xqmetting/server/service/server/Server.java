package com.xqmetting.server.service.server;


import com.xqmetting.codec.MeetMessageCodec;
import com.xqmetting.registry.ServiceRegistry;
import com.xqmetting.server.service.Service;
import com.xqmetting.server.utils.SpringContextUtil;
import com.xqmetting.server.worker.ServerRouterWorker;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class Server extends Service implements InitializingBean {


    private static final String MANAGE_PATH = "/meet/nodes";

    public static final String PATH_PREFIX = MANAGE_PATH + "/seq-";

   // @Resource
   // private ZkService zkService;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private ServerBootstrap bootstrap;

    @Resource
    private ServerRouterWorker serverRouterWorker;

    @Resource
    private ServiceRegistry serviceRegistry;



    public void startServer(){
        init();
        try{
            bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup);
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE,socketConfig.isKeepAlive());
            bootstrap.childOption(ChannelOption.TCP_NODELAY, socketConfig.isTcpNoDelay());
            bootstrap.channel(useEpoll()? EpollServerSocketChannel.class : NioServerSocketChannel.class);
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new LengthFieldBasedFrameDecoder(8192,16,4,0,0));
                    pipeline.addLast(new MeetMessageCodec());
                }
            });
            bootstrap.localAddress(new InetSocketAddress(socketConfig.getPort()));
            ChannelFuture channelFuture = bootstrap.bind().sync();
            channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if(future.isSuccess()){
                        log.info("服务端启动成功！");

                        //注册到zookeeper
                    //    ServerNode serverNode = new ServerNode("127.0.0.1",socketConfig.getPort());
                      ///  serviceRegistry.register(serverNode);
                        //开启监听
                        serverRouterWorker.init();
                    }
                }
            });
            //监听通道关闭事件
            //一直等待，直到channel关闭
            ChannelFuture closeFuture = channelFuture.channel().closeFuture();
            closeFuture.sync();
        }catch (Exception ex){
            ex.printStackTrace();;
        }finally{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }

    @Override
    protected void init() {
        super.init();
        if(useEpoll()){
            bossGroup = new EpollEventLoopGroup(socketConfig.getWorkerCount(), new ThreadFactory() {
                private AtomicInteger index = new AtomicInteger(0);
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r,"LINUX_BOSS_"+index.incrementAndGet());
                }
            });
            workerGroup = new EpollEventLoopGroup(socketConfig.getWorkerCount(), new ThreadFactory() {
               private AtomicInteger index = new AtomicInteger(0);
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r,"LINUX_WORKER_"+index.incrementAndGet());
                }
            });
        }else{
            bossGroup = new NioEventLoopGroup(socketConfig.getWorkerCount(), new ThreadFactory() {
                private AtomicInteger index = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "BOSS_" + index.incrementAndGet());
                }
            });
            workerGroup = new NioEventLoopGroup(socketConfig.getWorkerCount(), new ThreadFactory() {
                private AtomicInteger index = new AtomicInteger(0);
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "WORK_" + index.incrementAndGet());
                }
            });
        }
    }

    public boolean useEpoll(){
        String osName = System.getProperty("os.name");
        boolean isLinuxPlatform = StringUtils.containsIgnoreCase(osName,"linux");
        return isLinuxPlatform && Epoll.isAvailable();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        CuratorFramework curatorFramework =  SpringContextUtil.getBean(CuratorFramework.class);
        System.out.println(curatorFramework);
      //  startServer();
    }

    @Override
    public void shutdown() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
