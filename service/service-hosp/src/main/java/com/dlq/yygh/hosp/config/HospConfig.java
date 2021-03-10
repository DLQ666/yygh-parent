package com.dlq.yygh.hosp.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *@program: common-util
 *@description:
 *@author: Hasee
 *@create: 2021-03-10 15:53
 */
@Configuration
@MapperScan("com.dlq.yygh.hosp.mapper")
@ComponentScan(basePackages = "com.dlq.yygh")
public class HospConfig {
}
