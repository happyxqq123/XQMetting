package com.xqmetting.entity;

import lombok.Data;

@Data
public class SessionCache {

    //用户的id
    private String userId;
    private String sessionId;

    //节点信息
    private ServerNode serverNode;

    public SessionCache(String userId, String sessionId, ServerNode serverNode) {
        this.userId = userId;
        this.sessionId = sessionId;
        this.serverNode = serverNode;
    }

    public SessionCache()
    {
        userId = "";
        sessionId = "";
        serverNode = new ServerNode("unKnown", 0);
    }

}
