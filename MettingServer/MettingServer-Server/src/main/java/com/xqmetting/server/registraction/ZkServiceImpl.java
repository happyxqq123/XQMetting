package com.xqmetting.server.registraction;

import com.google.gson.Gson;
import com.xqmetting.entity.ServerNode;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

public class ZkServiceImpl implements ZkService{

    @Resource
    private CuratorFramework curatorFramework;


    @Override
    public boolean checkNodeExists(String path) throws Exception {
        Stat stat = curatorFramework.checkExists().forPath(path);
        return stat == null? false:true;
    }

    @Override
    public String createPersistentNode(String path) throws Exception {
        String pathRegistered = curatorFramework.create()
                .creatingParentsIfNeeded()
                .withProtection()
                .withMode(CreateMode.PERSISTENT)
                .forPath(path);
        return pathRegistered;
    }

    @Override
    public String createNode(String path, ServerNode serverNode) throws Exception {
        byte [] payload = new Gson().toJson(serverNode).getBytes(StandardCharsets.UTF_8);
        String pathRegistered = curatorFramework.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath(path,payload);
        return pathRegistered;
    }
}
