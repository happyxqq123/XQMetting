package com.xqmetting.server.worker;


import com.xqmetting.entity.ServerNode;
import lombok.Data;

@Data
public class ServerWorker {
    private ServerNode serverNode;

    private static final ServerWorker instance = new ServerWorker();

    public static ServerWorker instance(){
        return instance;
    }
}
