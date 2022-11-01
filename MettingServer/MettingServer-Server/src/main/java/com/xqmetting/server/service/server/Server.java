package com.xqmetting.server.service.server;


import com.xqmetting.server.service.Service;
import io.netty.channel.EventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component("mettingServer")
@Slf4j
public class Server extends Service implements InitializingBean {


    private static final String MANAGE_PATH = "/meet/nodes";

    public static final String PATH_PREFIX = MANAGE_PATH + "/seq-";

   // @Resource
   // private ZkService zkService;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public void startServer(){

    }


    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(socketConfig);
    }

    @Override
    public void shutdown() {

    }
}
