package com.xqmetting.util;

import com.google.common.collect.Maps;
import com.google.protobuf.GeneratedMessageV3;
import com.xqmetting.protocol.*;
import com.xqmetting.protocol.MessageOuterClass.Message;
import com.xqmetting.protocol.MessageOuterClass.MessageType;
import com.xqmetting.protocol.PingMessageOuterClass.PingMessage;
import com.xqmetting.protocol.ServerPeerConnectedMessageOuterClass.ServerPeerConnectedMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

;

public class ProtoBufUtils {

    private  static Map<Integer, Class<? extends GeneratedMessageV3>> messageMap = Maps.newHashMap();

    static{
        messageMap.put(MessageType.ChatRequestMessageType.getNumber(), ChatRequestMessageOuterClass.ChatRequestMessage.class);
        messageMap.put(MessageType.ChatResponseMessageType.getNumber(), ChatResponseMessageOuterClass.ChatResponseMessage.class);
        messageMap.put(MessageType.GroupChatRequestMessageType.getNumber(), GroupChatRequestMessageOuterClass.GroupChatRequestMessage.class);
        messageMap.put(MessageType.GroupChatResponseMessageType.getNumber(), GroupChatResponseMessageOuterClass.GroupChatResponseMessage.class);
        messageMap.put(MessageType.GroupRemoteChatRequestMessageType.getNumber(), GroupRemoteChatRequestMessageOuterClass.GroupRemoteChatRequestMessage.class);
        messageMap.put(MessageType.PingMessageType.getNumber(), PingMessageOuterClass.PingMessage.class);
        messageMap.put(MessageType.ServerPeerConnectedMessageType.getNumber(), ServerPeerConnectedMessageOuterClass.ServerPeerConnectedMessage.class);
    }

    public static GeneratedMessageV3 parseFrom(Integer messageType,byte[] buf){
        Class<? extends GeneratedMessageV3> generatedMessageV3 =messageMap.get(messageType);
        if(generatedMessageV3 == null){
            throw new NullPointerException( messageType+"该消息类型不存在！");
        }
        try {
            Method method =  generatedMessageV3.getMethod("parseFrom", byte[].class);
            return (GeneratedMessageV3) method.invoke(null,buf);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    public static Message getMessage(GeneratedMessageV3 generatedMessage){
        try {
           Method method =  generatedMessage.getClass().getMethod("getMsg");
           method.setAccessible(true);
           return (Message) method.invoke(generatedMessage);
        }  catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static PingMessage newPingMessage(){

        PingMessage.Builder pingBuilder =  PingMessage.newBuilder();
        Message.Builder msgBuilder =  Message.newBuilder();
        msgBuilder.setMessageType(MessageType.PingMessageType);
        pingBuilder.setMsg(msgBuilder.build());
        return pingBuilder.build();
    }

    public static ServerPeerConnectedMessage newServerPeerConnectedMessage(){
        ServerPeerConnectedMessage.Builder builder =  ServerPeerConnectedMessage.newBuilder();
        Message.Builder msgBuilder =  Message.newBuilder();
        msgBuilder.setMessageType(MessageType.ServerPeerConnectedMessageType);
        builder.setMsg(msgBuilder.build());
        return builder.build();
    }

}
