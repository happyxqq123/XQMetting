package com.xqmetting.server.registraction;

import com.xqmetting.server.utils.SpringContextUtil;
import org.apache.curator.framework.CuratorFramework;

public class CuratorZKclient {

    private static CuratorFramework singleton = null;

    public static CuratorFramework getSingleton(){
        if(singleton == null){
            singleton = SpringContextUtil.getBean(CuratorFramework.class);
        }
        return singleton;
    }
}
