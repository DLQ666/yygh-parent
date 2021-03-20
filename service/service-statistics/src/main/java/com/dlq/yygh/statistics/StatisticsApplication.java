package com.dlq.yygh.statistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 *@program: yygh-parent
 *@description:
 *@author: Hasee
 *@create: 2021-03-20 16:08
 */
@EnableFeignClients
@ComponentScan(basePackages = {"com.dlq.yygh"})
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//取消数据源自动配置
public class StatisticsApplication {
    public static void main(String[] args) {
        SpringApplication.run(StatisticsApplication.class, args);
    }
}
