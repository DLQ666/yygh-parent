package com.dlq.yygh.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 *@program: yygh-parent
 *@description:
 *@author: Hasee
 *@create: 2021-03-20 14:30
 */
@ComponentScan("com.dlq.yygh")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class TaskApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskApplication.class, args);
    }
}
