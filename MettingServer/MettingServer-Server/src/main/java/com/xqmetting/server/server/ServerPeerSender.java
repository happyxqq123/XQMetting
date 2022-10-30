package com.xqmetting.server.server;

import com.google.protobuf.Any;
import com.xqmetting.codec.MeetMessageCodec;
import com.xqmetting.codec.ProtocolFrameDecoder;
import com.xqmetting.entity.ServerNode;
import com.xqmetting.protocol.MessageOuterClass.Message;
import com.xqmetting.protocol.message.PingMessageOuterClass.PingMessage;
import com.xqmetting.server.utils.ProtoBufUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


/**
 * 连接其它的服务(cluster 之间的连接)
 */
@Data
@Slf4j
public class ServerPeerSender {

    private Channel channel;
    private Bootstrap bootstrap;
    private EventLoopGroup eventLoopGroup;
    private static final int WRITE_IDLE_GAP = 150;

    public  ServerPeerSender(){
        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup();
    }

    public void doConnectServer(ServerNode serverNode){
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new ProtocolFrameDecoder());
                        pipeline.addLast("codec",new MeetMessageCodec());
                        pipeline.addLast(new IdleStateHandler(0,WRITE_IDLE_GAP,0));
                        pipeline.addLast(new ChannelDuplexHandler(){
                            @Override
                            public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                IdleStateEvent event =(IdleStateEvent) evt;
                                if(event.state() == IdleState.WRITER_IDLE){
                                    log.info("{},没有写数据了，发送一个心跳包[服务间]",WRITE_IDLE_GAP);
                                    ctx.writeAndFlush(ProtoBufUtils.newPingMessage());
                                }
                            }

                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            }
                        });
                    }
                });

    }
}
