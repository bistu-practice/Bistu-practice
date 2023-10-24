package com.disaster.disastercode;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.disaster.disastercode.mapper")
public class DisasterCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DisasterCodeApplication.class, args);
    }

}
