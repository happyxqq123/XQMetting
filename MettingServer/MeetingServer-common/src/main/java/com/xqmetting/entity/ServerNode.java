package com.xqmetting.entity;

import lombok.Data;

@Data
public class ServerNode {

    // worker 的ID, zookeeper负责生成
    private long id;
    //Netty 服务IP
    private String host;
    //Netty 服务 端口
    private Integer port;
    //权重 （客户端连接数量)
    private Integer weight =0;

    public ServerNode(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public ServerNode(long id, String host, Integer port, Integer weight) {
        this.id = id;
        this.host = host;
        this.port = port;
        this.weight = weight;
    }

    public String getAddress(){
        return this.host+":"+this.port;
    }

    @Override
    public String toString() {
        return host+":"+port+":"+id;
    }

}
