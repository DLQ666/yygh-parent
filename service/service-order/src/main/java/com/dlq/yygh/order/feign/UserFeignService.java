package com.dlq.yygh.order.feign;

import com.dlq.yygh.model.user.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *@description:
 *@author: Hasee
 *@create: 2021-03-18 15:08
 */
@Component
@FeignClient("service-user")
public interface UserFeignService {

    /**
     * 根据就诊人id 获取就诊人信息
     */
    @GetMapping("/api/user/patient/inner/get/{id}")
    Patient getPatientById(@PathVariable("id")Long id);
}
