package com.xqmetting.entity;

import lombok.Data;

/**
 * @ClassName Msg
 * @Description TODO
 * @Author admin
 * @Date 2022/10/26 14:37
 * @Version 1.0
 **/

@Data
public class Msg {


    public enum MsgType{
        LOGIN_REQUEST,
        LOGIN_RESPONSE,
        CHAT,
        LOGOUT_REQUEST,
    }
}
