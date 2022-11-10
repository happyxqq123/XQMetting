package com.xqmetting.server.holder;

import com.xqmetting.server.service.server.ServerPeerSender;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 服务器间链接管理holder
 */
public class ServerPeerSenderHolder {

    private static ConcurrentHashMap<Long, ServerPeerSender> serverSenders =new ConcurrentHashMap<>();

    public static void addWorker(Long key, ServerPeerSender serverPeerSender){
        serverSenders.put(key,serverPeerSender);
    }

    public static ServerPeerSender getWorker(Long key){
        return serverSenders.get(key);
    }

    public static void removeWorker(Long key){
        serverSenders.remove(key);
    }

    public static Map<Long,ServerPeerSender> getAll(){
        return Collections.unmodifiableMap(serverSenders);
    }


}
