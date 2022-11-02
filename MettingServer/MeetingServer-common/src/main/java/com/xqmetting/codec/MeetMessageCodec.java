package com.xqmetting.codec;

import com.google.protobuf.GeneratedMessageV3;
import com.xqmetting.util.ByteBufUtils;
import com.xqmetting.util.ProtoBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
public class MeetMessageCodec extends ByteToMessageCodec<GeneratedMessageV3> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, GeneratedMessageV3  message, ByteBuf out) throws Exception {
        //1. 4字节的魔数
        out.writeBytes(new byte[]{1,2,3,4});
        //2. 1字节的版本
        out.writeByte(1);
        //3.1字节的序列化方式 jdk 0, json 1,protobuf 2
        out.writeByte(2);

        //4.4 字节的指令类型
        out.writeInt(ProtoBufUtils.getMessage(message).getMessageType().getNumber());
        //5.4 字节 序列
        out.writeInt(ProtoBufUtils.getMessage(message).getSequenceId());
        //5.2 字节 无意义对齐补充
        out.writeShort(0xffff);
        byte [] msgBytes = message.toByteArray();
        //7 写入消息体长度
        out.writeInt(msgBytes.length);
        out.writeBytes(msgBytes);
        ByteBufUtils.log(out);
    }
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int magicNum = byteBuf.readInt();  //读取魔数
        byte version = byteBuf.readByte(); //读取版本号
        byte serializerType = byteBuf.readByte(); //  字节的序列化
        int messageType = byteBuf.readInt(); //读取消息类型
        int sequenceId = byteBuf.readInt(); //读取消息序列号
        byteBuf.readShort(); //读取对齐
        int msgLen = byteBuf.readInt(); //读取消息长度
        byte[] buf = new byte[msgLen];
        byteBuf.readBytes(buf,0,msgLen);
        try{
            GeneratedMessageV3 message = ProtoBufUtils.parseFrom(messageType,buf);
            list.add(message);
        }catch (Exception e){
            //抛出异常直接丢弃
            e.printStackTrace();
            log.info("消息抛出异常,消息类型:"+messageType+",cause:"+e.getMessage());
        }


    }
}
