package com.xqmetting.server.registraction;

import com.xqmetting.entity.ServerNode;

public interface ZkService {

    boolean checkNodeExists(String path) throws Exception;

    String createPersistentNode(String path) throws Exception;

    String createNode(String prefix , ServerNode serverNode) throws Exception;

}
