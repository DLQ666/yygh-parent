package com.dlq.yygh.hosp.service;

import com.dlq.yygh.model.hosp.Schedule;
import com.dlq.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 *@description:
 *@author: Hasee
 *@create: 2021-03-13 12:21
 */
public interface ScheduleService {
    /**
     * 上传排班接口
     */
    void saveSchedule(Map<String, Object> paramMap);

    /**
     * 查询排班接口
     */
    Page<Schedule> getPageSchedule(int page, int limit, ScheduleQueryVo scheduleQueryVo);

    /**
     * 删除排班接口
     */
    boolean delSchedule(String hoscode, String hosScheduleId);

}
