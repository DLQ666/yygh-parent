package com.dlq.yygh.hosp.repository;

import com.dlq.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *@program: yygh-parent
 *@description:
 *@author: Hasee
 *@create: 2021-03-12 17:07
 */
@Repository
public interface HospitalRepository extends MongoRepository<Hospital,String> {

    /**
     * 判断是否存在数据
     */
    Hospital getHospitalByHoscode(String hoscode);

    /**
     * 根据医院名称获取医院列表
     */
    List<Hospital> findHospitalByHosnameLike(String hosname);
}
