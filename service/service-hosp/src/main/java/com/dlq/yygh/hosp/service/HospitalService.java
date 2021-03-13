package com.dlq.yygh.hosp.service;

import com.dlq.yygh.model.hosp.Hospital;
import com.dlq.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 *@description:
 *@author: Hasee
 *@create: 2021-03-12 17:09
 */
public interface HospitalService {
    /**
     * 上传医院接口
     */
    void save(Map<String, Object> paramMap);

    /**
     * 查询医院接口
     */
    Hospital getByHoscode(String hoscode);

    /**
     * 查询医院列表
     */
    Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);
}
