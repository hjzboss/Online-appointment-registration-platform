package com.hjznb.yygh.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/1/3 11:55
 */
@Configuration
@MapperScan("com.hjznb.yygh.mapper")
public class HospConfig {
}
