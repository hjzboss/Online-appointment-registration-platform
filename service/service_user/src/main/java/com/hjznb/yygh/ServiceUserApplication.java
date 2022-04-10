package com.hjznb.yygh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/4/10 11:08
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.hjznb")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.hjznb")
public class ServiceUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceUserApplication.class, args);
    }

}
