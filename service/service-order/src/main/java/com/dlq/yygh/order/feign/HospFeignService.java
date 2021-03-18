package com.dlq.yygh.order.feign;

import com.dlq.yygh.vo.hosp.ScheduleOrderVo;
import com.dlq.yygh.vo.order.SignInfoVo;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *@description:
 *@author: Hasee
 *@create: 2021-03-18 16:29
 */
@Component
@FeignClient("service-hosp")
public interface HospFeignService {

    @GetMapping("/api/hosp/hospital/inner/getScheduleOrderVo/{scheduleId}")
    ScheduleOrderVo getScheduleOrderVo(@PathVariable("scheduleId") String scheduleId);

    @GetMapping("/admin/hosp/hospitalSet/inner/getSignInfoVo/{hoscode}")
    SignInfoVo getSignInfoVo(@PathVariable("hoscode") String hoscode);
}
