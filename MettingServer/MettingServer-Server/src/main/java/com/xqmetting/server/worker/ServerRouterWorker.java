package com.xqmetting.server.worker;

import com.alibaba.fastjson.JSONObject;
import com.xqmetting.entity.ServerNode;
import com.xqmetting.server.holder.ServerPeerSenderHolder;
import com.xqmetting.server.service.server.ServerPeerSender;
import com.xqmetting.server.utils.ZookeeperUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class ServerRouterWorker {


    @Resource
    private CuratorFramework curatorFramework;

    @Resource
    private ServerWorker serverWorker;

    private boolean inited = false;

    public void init(){

        if(inited){
            return;
        }

        CuratorCache curatorCache = CuratorCache.build(curatorFramework, ZookeeperUtils.MANAGE_PATH,CuratorCache.Options.SINGLE_NODE_CACHE);
        curatorCache.start();

        CuratorCacheListener listener = CuratorCacheListener.builder().forPathChildrenCache(ZookeeperUtils.MANAGE_PATH, curatorFramework, new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                if(event.getType()== PathChildrenCacheEvent.Type.CHILD_ADDED){
                    processAdd(event.getData());
                }
            }
        }).build();
        curatorCache.listenable().addListener(listener);
        inited = true;
//        CuratorFramework curatorFramework = CuratorZKclient.getSingleton();
//        System.out.println(curatorFramework);
        //订阅节点的增加和删除事件
    }

    /**
     * zk节点新增
     * @param data
     */
    private void processAdd(ChildData data){
        try{
            ServerPeerSender serverPeerSender = new ServerPeerSender();
            long id = ZookeeperUtils.getIdByPath(data.getPath(),ZookeeperUtils.PATH_PREFIX);
            if(data.getData() == null || data.getData().length == 0){
                log.info("新节点没有数据,path={}",data.getPath());
                return;
            }
            ServerNode serverNode = JSONObject.parseObject(data.getData(),ServerNode.class);

            serverNode.setId(id);
            if(serverWorker.getServerNode().getAddress().equals(serverNode.getAddress())){
                log.info("监听到自身节点加入，无需进行连接");
            }
            serverPeerSender.doConnectServer(serverNode);
            log.info("新节点加入:{}",serverNode);
            ServerPeerSenderHolder.addWorker(id,serverPeerSender);
        }catch (Exception e){
            e.printStackTrace();
        }

    }



}
