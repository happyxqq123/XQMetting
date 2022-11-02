package com.test;


import com.xqmetting.codec.MeetMessageCodec;
import com.xqmetting.codec.ProtocolFrameDecoder;
import com.xqmetting.protocol.PingMessageOuterClass.PingMessage;
import com.xqmetting.server.MettingServerApplication;
import com.xqmetting.server.service.SocketConfig;
import com.xqmetting.util.ProtoBufUtils;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MettingServerApplication.class)
public class TestNetty {

    @Resource
    private SocketConfig socketConfig;


    @Test
    public void testEmbeddedChannelOut(){
//        EmbeddedChannel embeddedChannel = new EmbeddedChannel();
//        ChannelPipeline channelPipeline = embeddedChannel.pipeline();
//        channelPipeline.addLast(new LoggingHandler());
//        channelPipeline.addLast(new ProtocolFrameDecoder());
//        channelPipeline.addLast("codec",new MeetMessageCodec());
//
//        Message message =  ProtoBufUtils.newPingMessage();
//        embeddedChannel.writeOutbound(message);
//        //embeddedChannel.writeAndFlush(message);
    }

    @Test
    public void testEmbeddedChannelIn(){
        PingMessage pingMessage = ProtoBufUtils.newPingMessage();
        EmbeddedChannel embeddedChannel = new EmbeddedChannel();
        ChannelPipeline channelPipeline = embeddedChannel.pipeline();
        channelPipeline.addLast(new LoggingHandler());
        channelPipeline.addLast(new ProtocolFrameDecoder());
        channelPipeline.addLast("codec",new MeetMessageCodec());

        embeddedChannel.writeOutbound(pingMessage);
    }

    @Test
    public void testSocketConfig(){
        System.out.println(socketConfig);
    }

    @Test
    public void testProtobuf() throws NoSuchMethodException {
        Method method = PingMessage.class.getMethod("parseFrom", byte[].class);
        System.out.println(method);
    }

    @Test
    public void testHashMap(){
        Map<Integer,String> map = new HashMap<>();
        map.put(new Integer(1),"sdfsd");
        System.out.println(map.get(new Integer(1)));
    }





}
