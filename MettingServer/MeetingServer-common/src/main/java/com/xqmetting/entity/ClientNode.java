package com.xqmetting.entity;

import lombok.Data;

/**
 * @ClassName ClientNode
 * @Description TODO
 * @Author admin
 * @Date 2022/10/26 14:32
 * @Version 1.0
 **/

@Data
public class ClientNode {

    //Netty客户端IP
    private String host;

    //Netty客户端端口
    private Integer port;

    public ClientNode(String host, Integer port) {
        this.host = host;
        this.port = port;
    }
}
