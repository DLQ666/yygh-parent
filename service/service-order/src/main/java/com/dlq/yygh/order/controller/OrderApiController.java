package com.dlq.yygh.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dlq.yygh.common.result.Result;
import com.dlq.yygh.common.utils.AuthContextHolder;
import com.dlq.yygh.enums.OrderStatusEnum;
import com.dlq.yygh.helper.HttpRequestHelper;
import com.dlq.yygh.model.order.OrderInfo;
import com.dlq.yygh.model.user.UserInfo;
import com.dlq.yygh.order.service.OrderService;
import com.dlq.yygh.vo.order.OrderCountQueryVo;
import com.dlq.yygh.vo.order.OrderQueryVo;
import com.dlq.yygh.vo.user.UserInfoQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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

    /**
     * 订单列表
     */
    @GetMapping("/auth/{page}/{limit}")
    public Result list(@PathVariable("page") long page,
                       @PathVariable("limit") long limit,
                       OrderQueryVo orderQueryVo, HttpServletRequest request) {
        if (page <= 0) {
            page = 1;
        }
        if (limit <= 0) {
            limit = 10;
        }
        //设置当前用户id
        orderQueryVo.setUserId(AuthContextHolder.getUserId(request));
        Page<OrderInfo> pageParam = new Page<>(page, limit);
        IPage<OrderInfo> pageModel = orderService.selectPage(pageParam, orderQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "获取订单状态")
    @GetMapping("/auth/getStatusList")
    public Result getStatusList() {
        return Result.ok(OrderStatusEnum.getStatusList());
    }

    @ApiOperation(value = "创建订单")
    @PostMapping("/auth/submitOrder/{scheduleId}/{patientId}")
    public Result saveOrder(@PathVariable("scheduleId")String scheduleId,
                            @PathVariable("patientId")Long patientId){
        Long order = orderService.saveOrder(scheduleId,patientId);
        return Result.ok(order);
    }

    /**
     * 根据订单id查询订单详情
     */
    @GetMapping("/auth/getOrders/{orderId}")
    public Result getOrders(@PathVariable("orderId")String orderId){
        OrderInfo orderInfo = orderService.getOrder(orderId);
        return Result.ok(orderInfo);
    }

    /**
     * 取消预约 --- 退款
     */
    @GetMapping("/auth/cancelOrder/{orderId}")
    public Result cancelOrder(@PathVariable("orderId")Long orderId){
        Boolean isOrder = orderService.cancelOrder(orderId);
        return Result.ok(isOrder);
    }

    /**
     * 获取订单统计数据
     */
    @ApiOperation(value = "获取订单统计数据")
    @PostMapping("/inner/getCountMap")
    public Map<String, Object> getCountMap(@RequestBody OrderCountQueryVo orderCountQueryVo) {
        return orderService.getCountMap(orderCountQueryVo);
    }
}
