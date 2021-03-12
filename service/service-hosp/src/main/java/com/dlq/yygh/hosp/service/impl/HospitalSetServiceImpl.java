package com.dlq.yygh.hosp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dlq.yygh.hosp.mapper.HospitalSetMapper;
import com.dlq.yygh.hosp.service.HospitalSetService;
import com.dlq.yygh.model.hosp.HospitalSet;
import org.springframework.stereotype.Service;

/**
 *@program: common-util
 *@description:
 *@author: Hasee
 *@create: 2021-03-10 15:34
 */
@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {


    @Override
    public String getSignKey(String hoscode) {
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        wrapper.eq("hoscode", hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(wrapper);
        return hospitalSet.getSignKey();
    }
}
