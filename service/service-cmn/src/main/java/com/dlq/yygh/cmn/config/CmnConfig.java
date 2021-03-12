package com.dlq.yygh.cmn.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *@program: common-util
 *@description:
 *@author: Hasee
 *@create: 2021-03-10 15:53
 */
@Configuration
@MapperScan("com.dlq.yygh.cmn.mapper")
@ComponentScan(basePackages = "com.dlq.yygh")
public class CmnConfig {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
