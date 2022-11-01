package com.xqmetting.server.worker;


import com.xqmetting.entity.ServerNode;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class ServerWorker {
    private ServerNode serverNode;
}
