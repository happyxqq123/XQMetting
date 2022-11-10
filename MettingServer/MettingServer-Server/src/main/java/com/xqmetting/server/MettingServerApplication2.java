package com.xqmetting.server;

import com.xqmetting.server.service.server.Server;
import com.xqmetting.server.utils.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MettingServerApplication2 {

    public static void main(String[] args) {
        SpringApplication.run(MettingServerApplication2.class);
        System.out.println("springboot启动成功！");
        Server server = SpringContextUtil.getBean(Server.class);
        server.startServer();
    }
}
