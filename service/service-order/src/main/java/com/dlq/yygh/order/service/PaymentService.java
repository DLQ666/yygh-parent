package com.dlq.yygh.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dlq.yygh.model.order.OrderInfo;
import com.dlq.yygh.model.order.PaymentInfo;

import java.util.Map;

/**
 *@description:
 *@author: Hasee
 *@create: 2021-03-19 15:12
 */
public interface PaymentService extends IService<PaymentInfo> {

    /**
     * 向支付记录表中添加信息
     */
    void savePaymentInfo(OrderInfo order, Integer status);

    /**
     * 更改订单状态，处理支付结果
     */
    void paySuccess(String out_trade_no, Integer status, Map<String, String> resultMap);

    /**
     * 获取支付记录
     */
    PaymentInfo getPaymentInfo(Long orderId, Integer paymentType);
}
