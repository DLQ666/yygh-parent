package com.dlq.yygh.order.controller;

import com.dlq.yygh.common.result.Result;
import com.dlq.yygh.order.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *@program: yygh-parent
 *@description:
 *@author: Hasee
 *@create: 2021-03-17 20:12
 */
@Api(tags = "订单接口")
@RestController
@RequestMapping("/api/order/orderInfo")
public class OrderApiController {

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "创建订单")
    @PostMapping("/auth/submitOrder/{scheduleId}/{patientId}")
    public Result saveOrder(@PathVariable("scheduleId")String scheduleId,
                            @PathVariable("patientId")Long patientId){
        Long order = orderService.saveOrder(scheduleId,patientId);
        return Result.ok(order);
    }
}
