package com.xqmetting.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.xqmetting")
public class MettingServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MettingServerApplication.class);
    }
}
