package com.xqmetting.server.registraction;

import com.xqmetting.entity.ServerNode;
import com.xqmetting.registry.ServiceRegistry;
import com.xqmetting.server.utils.ZookeeperUtils;
import com.xqmetting.server.worker.ServerWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class ZookeeperServiceRegistryImpl implements ServiceRegistry {

    @Resource
    private ZkService zkService;

    @Resource
    private ServerWorker serverWorker;

    @Resource
    private CuratorFramework curatorFramework;


    @Override
    public void register(ServerNode serverNode) {
        try {
            String pathRegistered =  zkService.createNode(ZookeeperUtils.PATH_PREFIX, serverNode);
            //为node设置ID,
            serverNode.setId(ZookeeperUtils.getIdByPath(pathRegistered,ZookeeperUtils.PATH_PREFIX));
            log.info("本地节点, path={}, id={}",pathRegistered,serverNode.getId());
            serverWorker.setServerNode(serverNode);
            byte [] data = curatorFramework.getData().forPath(pathRegistered);
            String sdata = new String(data, StandardCharsets.UTF_8);
            log.info("data:{}",sdata);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
