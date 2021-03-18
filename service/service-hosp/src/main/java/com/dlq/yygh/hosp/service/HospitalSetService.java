package com.dlq.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dlq.yygh.model.hosp.HospitalSet;
import com.dlq.yygh.vo.order.SignInfoVo;

/**
 *@description:
 *@author: Hasee
 *@create: 2021-03-10 15:33
 */
public interface HospitalSetService extends IService<HospitalSet> {

    String getSignKey(String hoscode);

    /**
     * 获取医院签名信息
     */
    SignInfoVo getSignInfoVo(String hoscode);
}
