package com.xqmetting.registry;

import com.xqmetting.entity.ServerNode;

public interface ServiceRegistry {


    /**
     * 注册当前服务到zookeeper中
     * @param serverNode
     */
    void register(ServerNode serverNode);




}
