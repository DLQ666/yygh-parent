package com.dlq.yygh.hosp.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *@description:
 *@author: Hasee
 *@create: 2021-03-13 16:00
 */
@FeignClient("service-cmn")
public interface CmnFeignService {

    //根据dictcode和value查询
    @GetMapping("/admin/cmn/dict/getName/{dictCode}/{value}")
    String getName(@PathVariable("dictCode")String dictCode, @PathVariable("value")String value);

    //根据value查询
    @GetMapping("/admin/cmn/dict/getName/{value}")
    String getName(@PathVariable("value")String value);
}
