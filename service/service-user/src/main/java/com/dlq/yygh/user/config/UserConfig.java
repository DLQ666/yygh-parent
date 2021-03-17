package com.dlq.yygh.user.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *@program: yygh-parent
 *@description:
 *@author: Hasee
 *@create: 2021-03-15 17:18
 */
@ComponentScan(basePackages = "com.dlq.yygh")
@MapperScan("com.dlq.yygh.user.mapper")
@Configuration
public class UserConfig {
}
