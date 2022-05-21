package com.hjznb.yygh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/5/21 10:44
 */
@EnableFeignClients(basePackages = "com.hjznb")
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan("com.hjznb") //因为引入的工具类common的依赖的包名和这里不一样，要设置扫描
@MapperScan("com.hjznb.yygh.mapper")
public class FeedbackApplication {
    public static void main(String[] args) {
        SpringApplication.run(FeedbackApplication.class, args);
    }
}
