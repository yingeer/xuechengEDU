package com.xuecheng.govern.center.com.xuecheng.govern.center;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka服务注册中心2
 *
 * @author Ying on 2020/4/18
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaGovernCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaGovernCenterApplication.class);
    }
}
