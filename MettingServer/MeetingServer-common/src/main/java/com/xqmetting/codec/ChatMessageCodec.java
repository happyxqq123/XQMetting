package com.xqmetting.codec;

import com.xqmetting.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

public class ChatMessageCodec extends ByteToMessageCodec<Message> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        //1. 4字节的魔数
        byteBuf.writeBytes(new byte[]{1,2,3,4});
        //2. 1字节的版本
        byteBuf.writeByte(1);
        //3.1字节的序列化方式 jdk 0, json 1,protobuf 2
        byteBuf.writeByte(2);

    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

    }
}
