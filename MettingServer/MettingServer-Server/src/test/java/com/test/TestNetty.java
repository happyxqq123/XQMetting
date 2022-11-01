package com.test;


import com.xqmetting.codec.MeetMessageCodec;
import com.xqmetting.codec.ProtocolFrameDecoder;
import com.xqmetting.protocol.MessageOuterClass.Message;
import com.xqmetting.server.MettingServerApplication;
import com.xqmetting.server.service.SocketConfig;
import com.xqmetting.server.utils.ProtoBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MettingServerApplication.class)
public class TestNetty {

    @Resource
    private SocketConfig socketConfig;


    @Test
    public void testEmbeddedChannelOut(){
        EmbeddedChannel embeddedChannel = new EmbeddedChannel();
        ChannelPipeline channelPipeline = embeddedChannel.pipeline();
        channelPipeline.addLast(new LoggingHandler());
        channelPipeline.addLast(new ProtocolFrameDecoder());
        channelPipeline.addLast("codec",new MeetMessageCodec());

        Message message =  ProtoBufUtils.newPingMessage();
        embeddedChannel.writeOutbound(message);
        //embeddedChannel.writeAndFlush(message);
    }

    @Test
    public void testEmbeddedChannelIn(){
        Message message = ProtoBufUtils.newPingMessage();
        ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
        out.writeBytes(new byte[]{1,2,3,4});
        //2. 1字节的版本
        out.writeByte(1);
        //3.1字节的序列化方式 jdk 0, json 1,protobuf 2
        out.writeByte(2);
        //4.4 字节的指令类型
        out.writeInt(message.getMessageType().getNumber());
        //5.4 字节 序列
        out.writeInt(message.getSequenceId());
        //5.2 字节 无意义对齐补充
        out.writeShort(0xffff);
        byte [] msgBytes = message.toByteArray();
        //7 写入消息体长度
        out.writeInt(msgBytes.length);
        out.writeBytes(msgBytes);
        EmbeddedChannel embeddedChannel = new EmbeddedChannel();
        ChannelPipeline channelPipeline = embeddedChannel.pipeline();
        channelPipeline.addLast(new LoggingHandler());
        channelPipeline.addLast(new ProtocolFrameDecoder());
        channelPipeline.addLast("codec",new MeetMessageCodec());

        embeddedChannel.writeInbound(out);
    }

    @Test
    public void testSocketConfig(){
        System.out.println(socketConfig);
    }





}
