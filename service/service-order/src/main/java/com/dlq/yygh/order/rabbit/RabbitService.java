package com.dlq.yygh.order.rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *@program: yygh-parent
 *@description:
 *@author: Hasee
 *@create: 2021-03-18 17:26
 */
public class RabbitService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     *  发送消息
     * @param exchange 交换机
     * @param routingKey 路由键
     * @param message 消息
     */
    public boolean sendMessage(String exchange, String routingKey, Object message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        return true;
    }
}
