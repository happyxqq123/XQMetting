package com.xqmetting.server.utils;

import com.google.protobuf.Any;
import com.xqmetting.protocol.MessageOuterClass.MessageType;
import com.xqmetting.protocol.MessageOuterClass.Message;
import com.xqmetting.protocol.message.PingMessageOuterClass.PingMessage;
import com.xqmetting.protocol.message.ServerPeerConnectedMessageOuterClass.ServerPeerConnectedMessage;

public class ProtoBufUtils {

    public static Message newPingMessage(){
        PingMessage pingMessage = PingMessage.newBuilder().build();
        Message.Builder msgBuilder = Message.newBuilder();
        msgBuilder.setMessageType(MessageType.PingMessageType);
        msgBuilder.setData(Any.pack(pingMessage));
        return msgBuilder.build();
    }

    public static Message newServerPeerConnectedMessage(){
        ServerPeerConnectedMessage serverPeerConnectedMessage = ServerPeerConnectedMessage.newBuilder().build();
        Message.Builder msgBuilder = Message.newBuilder();
        msgBuilder.setMessageType(MessageType.ServerPeerConnectedMessageType);
        msgBuilder.setData(Any.pack(serverPeerConnectedMessage));
        return msgBuilder.build();
    }

}
