package com.xqmetting.message;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName Message
 * @Description TODO
 * @Author admin
 * @Date 2022/10/26 15:33
 * @Version 1.0
 **/
@Data
public abstract class Message  implements Serializable {

    /**
     * 根据消息类型字节，获得对应消息class
     */
    public static Class<? extends Message> getMessageClass(int messageType){
        return messageClasses.get(messageType);
    }

    private int sequenceId;
    private int messageType;

    public abstract int getMessageType();


    private static final Map<Integer, Class<? extends Message>> messageClasses = new HashMap<>();

    static{


    }

    public static void main(String[] args) {
        System.out.println("a");
    }
}
