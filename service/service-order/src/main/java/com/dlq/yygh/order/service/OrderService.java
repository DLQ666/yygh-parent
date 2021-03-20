package com.dlq.yygh.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dlq.yygh.model.order.OrderInfo;
import com.dlq.yygh.vo.order.OrderCountQueryVo;
import com.dlq.yygh.vo.order.OrderQueryVo;

import java.util.List;
import java.util.Map;

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

    /**
     * 订单列表
     */
    IPage<OrderInfo> selectPage(Page<OrderInfo> pageParam, OrderQueryVo orderQueryVo);

    /**
     * 取消预约 --- 退款
     */
    Boolean cancelOrder(Long orderId);

    /**
     * 就诊通知
     */
    void patientTips();

    /**
     * 预约统计
     */
    Map<String,Object> getCountMap(OrderCountQueryVo orderCountQueryVo);
}
