package com.xqmetting.codec;

import com.xqmetting.protocol.MessageOuterClass.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

public class MeetMessageCodec extends ByteToMessageCodec<Message> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf out) throws Exception {
        //1. 4字节的魔数
        out.writeBytes(new byte[]{1,2,3,4});
        //2. 1字节的版本
        out.writeByte(1);
        //3.1字节的序列化方式 jdk 0, json 1,protobuf 2
        out.writeByte(2);
        //4.4 字节的指令类型
        out.writeInt(message.getMessageType().getNumber());
        //5.4 字节 序列
        out.writeInt(message.getSequenceId());
        //5.无意义对齐补充
        out.writeShort(0xffff);

        byte [] msgBytes = message.toByteArray();
        //7 写入消息体长度
        out.writeInt(msgBytes.length);
        out.writeBytes(msgBytes);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

    }
}
