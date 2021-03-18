package com.dlq.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dlq.yygh.model.hosp.Schedule;
import com.dlq.yygh.vo.hosp.ScheduleOrderVo;
import com.dlq.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 *@description:
 *@author: Hasee
 *@create: 2021-03-13 12:21
 */
public interface ScheduleService extends IService<Schedule> {
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

    /**
     *  根据医院编号和科室编号，查询排班规则数据
     */
    Map<String, Object> getRuleSchedule(long page, long limit, String hoscode, String depcode);

    /**
     * 根据医院编号、科室编号和工作日期，查询排班详细信息
     */
    List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate);

    /**
     * 获取可预约排班数据
     */
    Map<String,Object> getBookingScheduleRule(Integer page, Integer limit, String hoscode, String depcode);

    /**
     * 根据排班id获取排班数据
     */
    Schedule getScheduleById(String scheduleId);

    /**
     * 根据排班id获取预约下单数据
     */
    ScheduleOrderVo getScheduleOrderVo(String scheduleId);

    /**
     * 更新排班数据 用于MQ
     */
    void update(Schedule schedule);
}
