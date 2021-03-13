package com.dlq.yygh.hosp.repository;

import com.dlq.yygh.model.hosp.Hospital;
import com.dlq.yygh.model.hosp.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 *@program: yygh-parent
 *@description:
 *@author: Hasee
 *@create: 2021-03-12 17:07
 */
@Repository
public interface ScheduleRepository extends MongoRepository<Schedule,String> {

    //判断是否存在数据
    Schedule getScheduleByHoscodeAndHosScheduleId(String hoscode, String hosScheduleId);
}
