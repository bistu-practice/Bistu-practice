package com.disaster.disastercode;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.disaster.disastercode.mapper")
@EnableAspectJAutoProxy
@EnableScheduling
public class DisasterCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DisasterCodeApplication.class, args);
    }

}
