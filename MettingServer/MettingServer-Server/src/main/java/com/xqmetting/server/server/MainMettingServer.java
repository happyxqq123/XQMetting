package com.xqmetting.server.server;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("mettingServer")
@Slf4j
public class MainMettingServer {

    @Value("${chat.server.port}")
    private int port;

    private static final String MANAGE_PATH = "/meet/nodes";

    public static final String PATH_PREFIX = MANAGE_PATH + "/seq-";

}
