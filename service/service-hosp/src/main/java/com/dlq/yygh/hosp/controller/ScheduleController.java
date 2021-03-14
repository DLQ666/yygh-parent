package com.dlq.yygh.hosp.controller;

import com.dlq.yygh.common.result.Result;
import com.dlq.yygh.hosp.service.ScheduleService;
import com.dlq.yygh.model.hosp.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *@program: yygh-parent
 *@description:
 *@author: Hasee
 *@create: 2021-03-13 23:01
 */
@CrossOrigin
@RestController
@RequestMapping("/admin/hosp/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    /**
     *  根据医院编号和科室编号，查询排班规则数据
     */
    @GetMapping("/getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getScheduleRule(@PathVariable("page")long page,
                                  @PathVariable("limit")long limit,
                                  @PathVariable("hoscode")String hoscode,
                                  @PathVariable("depcode")String depcode){

        Map<String,Object> map = scheduleService.getRuleSchedule(page,limit,hoscode,depcode);
        return Result.ok(map);
    }

    /**
     * 根据医院编号、科室编号和工作日期，查询排班详细信息
     */
    @GetMapping("/getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public Result getScheduleDetail(@PathVariable("hoscode")String hoscode,
                                    @PathVariable("depcode")String depcode,
                                    @PathVariable("depcode")String workDate){
        List<Schedule> list = scheduleService.getDetailSchedule(hoscode,depcode,workDate);
        return Result.ok(list);
    }

}
