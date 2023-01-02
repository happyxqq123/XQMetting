package com.test;


import com.xqmetting.codec.MeetMessageCodec;
import com.xqmetting.codec.ProtocolFrameDecoder;
import com.xqmetting.protocol.BaseMessageOuterClass;
import com.xqmetting.protocol.BaseMessageOuterClass.BaseMessage;
import com.xqmetting.protocol.PingRequestOuterClass;
import com.xqmetting.server.MettingServerApplication;
import com.xqmetting.server.service.SocketConfig;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
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
        EmbeddedChannel embeddedChannel = new EmbeddedChannel();
        ChannelPipeline channelPipeline = embeddedChannel.pipeline();
        channelPipeline.addLast(new LoggingHandler());
        channelPipeline.addLast(new ProtocolFrameDecoder());
        channelPipeline.addLast("codec",new MeetMessageCodec());
        BaseMessage.Builder baseMessageBuilder = BaseMessage.newBuilder();
        baseMessageBuilder.setMessageType(BaseMessageOuterClass.MessageType.PingMessageType);
        baseMessageBuilder.setSequenceId(2);
        baseMessageBuilder.setPingRequest(PingRequestOuterClass.PingRequest.newBuilder());
        embeddedChannel.writeAndFlush(baseMessageBuilder.build());

    }

    @Test
    public void testEmbeddedChannelIn(){
/*        PingMessage pingMessage = ProtoBufUtils.newPingMessage();
        EmbeddedChannel embeddedChannel = new EmbeddedChannel();
        ChannelPipeline channelPipeline = embeddedChannel.pipeline();
        channelPipeline.addLast(new LoggingHandler());
        channelPipeline.addLast(new ProtocolFrameDecoder());
        channelPipeline.addLast("codec",new MeetMessageCodec());
        embeddedChannel.writeOutbound(pingMessage);*/

        BaseMessage.Builder baseMessageBuilder = BaseMessage.newBuilder();
        baseMessageBuilder.setMessageType(BaseMessageOuterClass.MessageType.PingMessageType);
        baseMessageBuilder.setSequenceId(1);
        baseMessageBuilder.setPingRequest(PingRequestOuterClass.PingRequest.newBuilder());
        EmbeddedChannel embeddedChannel1 = new EmbeddedChannel();
        ChannelPipeline channelPipeline1 = embeddedChannel1.pipeline();
        channelPipeline1.addLast(new LoggingHandler());
        channelPipeline1.addLast(new ProtocolFrameDecoder());
        channelPipeline1.addLast("codec",new MeetMessageCodec());
        embeddedChannel1.writeOutbound(baseMessageBuilder.build());


    }

    @Test
    public void testSocketConfig(){
        System.out.println(socketConfig);
    }

    @Test
    public void testProtobuf() throws NoSuchMethodException {
/*        Method method = PingMessage.class.getMethod("parseFrom", byte[].class);
        System.out.println(method);*/
    }

    @Test
    public void testHashMap(){
        Map<Integer,String> map = new HashMap<>();
        map.put(new Integer(1),"sdfsd");
        System.out.println(map.get(new Integer(1)));
    }





}
