package com.xqmetting.server.worker;

import com.xqmetting.server.registraction.CuratorZKclient;
import org.apache.curator.framework.CuratorFramework;

public class ServerRouterWorker {


    private boolean inited = false;

    public void init(){

        if(inited){
            return;
        }

        CuratorFramework curatorFramework = CuratorZKclient.getSingleton();
        //订阅节点的增加和删除事件
       // CuratorCache curatorCache = CuratorCache.build(curatorFramework, ZookeeperUtils.MANAGE_PATH,CuratorCache.Options.)

    }



}
