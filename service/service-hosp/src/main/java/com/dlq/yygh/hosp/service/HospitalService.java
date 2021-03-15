package com.dlq.yygh.hosp.service;

import com.dlq.yygh.model.hosp.Hospital;
import com.dlq.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
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

    /**
     * 更新医院上线状态
     */
    void updateHospStatus(String id, Integer status);

    /**
     * 医院详情
     */
    Map<String, Object> getHospById(String id);

    /**
     * 根据医院编号得到医院名称
     */
    String getHosName(String hoscode);

    /**
     * 根据医院名称获取医院列表
     */
    List<Hospital> findByHosname(String hosname);

    /**
     * 根据医院编号获取医院详情信息
     */
    Map<String, Object> item(String hoscode);
}
