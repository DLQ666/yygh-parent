package com.dlq.yygh.hosp.repository;

import com.dlq.yygh.model.hosp.Hospital;
import com.dlq.yygh.model.hosp.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

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

    /**
     * 根据医院编号、科室编号和工作日期，查询排班详细信息
     */
    List<Schedule> findScheduleByHoscodeAndDepcodeAndWorkDate(String hoscode, String depcode, Date toDate);
}
