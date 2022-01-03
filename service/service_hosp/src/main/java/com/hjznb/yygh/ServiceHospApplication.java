package com.hjznb.yygh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/1/2 22:08
 */
@SpringBootApplication
@ComponentScan("com.hjznb") //因为引入的工具类common的依赖的包名和这里不一样，要设置扫描
public class ServiceHospApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceHospApplication.class, args);
    }
}
