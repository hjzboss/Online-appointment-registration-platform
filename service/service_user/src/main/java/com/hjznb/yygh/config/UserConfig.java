package com.hjznb.yygh.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/4/10 11:20
 */
@Configuration
@MapperScan("com.hjznb.yygh.mapper")
public class UserConfig {
}
