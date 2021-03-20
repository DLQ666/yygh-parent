package com.dlq.yygh.statistics.feign;

import com.dlq.yygh.vo.order.OrderCountQueryVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 *@description:
 *@author: Hasee
 *@create: 2021-03-20 16:12
 */
@FeignClient("service-order")
public interface OrderFeignService {

    @PostMapping("/api/order/orderInfo/inner/getCountMap")
    Map<String, Object> getCountMap(@RequestBody OrderCountQueryVo orderCountQueryVo);
}
