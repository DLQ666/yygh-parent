package com.dlq.yygh.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dlq.yygh.model.order.OrderInfo;
import com.dlq.yygh.order.mapper.OrderMapper;
import com.dlq.yygh.order.service.OrderService;
import org.springframework.stereotype.Service;

/**
 *@program: yygh-parent
 *@description:
 *@author: Hasee
 *@create: 2021-03-17 20:13
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper,OrderInfo> implements OrderService {
}
