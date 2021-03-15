package com.dlq.yygh.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 *@program: yygh-parent
 *@description:
 *@author: Hasee
 *@create: 2021-03-15 19:37
 */
@ComponentScan("com.dlq.yygh")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//取消数据源自动配置
public class SmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmsApplication.class, args);
    }
}
