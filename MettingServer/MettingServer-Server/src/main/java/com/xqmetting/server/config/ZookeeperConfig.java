package com.xqmetting.server.config;

import lombok.Data;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ConfigurationProperties(prefix = "curator")
public class ZookeeperConfig {

    @Value("${curator.retryCount}")
    private int retryCount;
    @Value("${curator.elapsedTimeMs}")
    private int elapsedTimeMs;
    @Value("${curator.connectString}")
    private String connectString;
    @Value("${curator.sessionTimeoutMs}")
    private int sessionTimeoutMs;
    @Value("${curator.connectionTimeoutMs}")
    private int connectionTimeoutMs;
    @Value("${curator.namespace}")
    private String namespace;

    @Bean(initMethod = "start",name = "curatorFramework")
    public CuratorFramework curatorFramework(){
        return CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .namespace(namespace)
                .sessionTimeoutMs(sessionTimeoutMs)
                .connectionTimeoutMs(connectionTimeoutMs)
                .retryPolicy(new RetryNTimes(retryCount,elapsedTimeMs)).build();
    }

}
