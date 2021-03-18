package com.dlq.yygh.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dlq.yygh.model.order.OrderInfo;

/**
 *@description:
 *@author: Hasee
 *@create: 2021-03-17 20:13
 */
public interface OrderService extends IService<OrderInfo> {
    /**
     * 创建订单
     */
    Long saveOrder(String scheduleId, Long patientId);

    /**
     * 根据订单id查询订单详情
     */
    OrderInfo getOrder(String orderId);
}
