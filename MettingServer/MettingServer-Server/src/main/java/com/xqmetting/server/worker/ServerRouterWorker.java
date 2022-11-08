package com.xqmetting.server.worker;

import com.xqmetting.server.utils.ZookeeperUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class ServerRouterWorker {


    @Resource
    private CuratorFramework curatorFramework;
    private boolean inited = false;

    public void init(){

        if(inited){
            return;
        }

//        CuratorFramework curatorFramework = CuratorZKclient.getSingleton();
//        System.out.println(curatorFramework);
        //订阅节点的增加和删除事件
        CuratorCache curatorCache = CuratorCache.build(curatorFramework, ZookeeperUtils.MANAGE_PATH,CuratorCache.Options.COMPRESSED_DATA);
        curatorCache.start();
        curatorCache.listenable().addListener(new CuratorCacheListener() {
            @Override
            public void event(Type type, ChildData oldData, ChildData data) {
                System.out.println(type);
            }
        });
    }



}
