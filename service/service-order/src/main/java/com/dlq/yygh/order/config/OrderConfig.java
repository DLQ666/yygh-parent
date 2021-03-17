package com.dlq.yygh.order.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *@program: yygh-parent
 *@description:
 *@author: Hasee
 *@create: 2021-03-17 20:17
 */
@MapperScan("com.dlq.yygh.order.mapper")
@ComponentScan("com.dlq.yygh")
@Configuration
public class OrderConfig {
}
