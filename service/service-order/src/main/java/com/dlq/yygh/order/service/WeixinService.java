package com.dlq.yygh.order.service;

import java.util.Map;

/**
 *@description:
 *@author: Hasee
 *@create: 2021-03-19 15:07
 */
public interface WeixinService {

    /**
     * 下单 生成二维码
     */
    Map createNative(Long orderId);

    /**
     * 调用微信接口实现支付状态查询
     */
    Map<String, String> queryPayStatus(Long orderId);

    /***
     * 退款
     */
    Boolean refund(Long orderId);
}
