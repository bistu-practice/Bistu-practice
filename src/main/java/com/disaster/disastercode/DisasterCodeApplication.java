package com.disaster.disastercode;

import cn.dev33.satoken.annotation.SaCheckLogin;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan("com.disaster.disastercode.mapper")
@EnableAspectJAutoProxy
public class DisasterCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DisasterCodeApplication.class, args);
    }

}
