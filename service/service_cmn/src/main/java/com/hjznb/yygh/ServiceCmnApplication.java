package com.hjznb.yygh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/1/15 16:34
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.hjznb"}) // 该注解默认会扫描该类所在的包下所有的配置类
public class ServiceCmnApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCmnApplication.class, args);
    }
}
