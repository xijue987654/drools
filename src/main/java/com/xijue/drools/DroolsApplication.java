package com.xijue.drools;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = {"com.xijue.drools.mapper"})
public class DroolsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DroolsApplication.class, args);
    }

}
