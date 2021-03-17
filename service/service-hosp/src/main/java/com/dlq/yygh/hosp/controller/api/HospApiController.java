package com.dlq.yygh.hosp.controller.api;

import com.dlq.yygh.common.result.Result;
import com.dlq.yygh.hosp.service.DepartmentService;
import com.dlq.yygh.hosp.service.HospitalService;
import com.dlq.yygh.hosp.service.ScheduleService;
import com.dlq.yygh.model.hosp.Hospital;
import com.dlq.yygh.model.hosp.Schedule;
import com.dlq.yygh.vo.hosp.DepartmentVo;
import com.dlq.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 *@program: yygh-parent
 *@description:
 *@author: Hasee
 *@create: 2021-03-14 22:26
 */
@Api(tags = "医院管理接口")
@RestController
@RequestMapping("/api/hosp/hospital")
public class HospApiController {

    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation(value = "获取医院分页列表")
    @GetMapping("/findHosplist/{page}/{limit}")
    public Result findHosplist(@PathVariable("page")Integer page,
                               @PathVariable("limit")Integer limit,
                               HospitalQueryVo hospitalQueryVo){
        Page<Hospital> hospPages = hospitalService.selectHospPage(page, limit, hospitalQueryVo);
        return Result.ok(hospPages);
    }

    @ApiOperation(value = "根据医院名称获取医院列表")
    @GetMapping("/findByHosname/{hosname}")
    public Result findByHosname(@PathVariable("hosname") String hosname){
        List<Hospital> hospitals = hospitalService.findByHosname(hosname);
        return Result.ok(hospitals);
    }

    @ApiOperation(value = "根据医院编号获取医院科室")
    @GetMapping("/department/{hoscode}")
    public Result index(@PathVariable("hoscode") String hoscode){
        List<DepartmentVo> list = departmentService.findDeptTree(hoscode);
        return Result.ok(list);
    }

    @ApiOperation(value = "根据医院编号获取医院详情信息")
    @GetMapping("/findHospDetail/{hoscode}")
    public Result item(@PathVariable("hoscode") String hoscode){
        Map<String,Object> map = hospitalService.item(hoscode);
        return Result.ok(map);
    }

    @ApiOperation(value = "获取可预约排班数据")
    @GetMapping("/auth/getBookingScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getBookingSchedule(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Integer page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Integer limit,
            @ApiParam(name = "hoscode", value = "医院code", required = true)
            @PathVariable String hoscode,
            @ApiParam(name = "depcode", value = "科室code", required = true)
            @PathVariable String depcode) {
        return Result.ok(scheduleService.getBookingScheduleRule(page, limit, hoscode, depcode));
    }

    @ApiOperation(value = "获取排班数据")
    @GetMapping("/auth/findScheduleList/{hoscode}/{depcode}/{workDate}")
    public Result findScheduleList(
            @ApiParam(name = "hoscode", value = "医院code", required = true)
            @PathVariable String hoscode,
            @ApiParam(name = "depcode", value = "科室code", required = true)
            @PathVariable String depcode,
            @ApiParam(name = "workDate", value = "排班日期", required = true)
            @PathVariable String workDate) {
        return Result.ok(scheduleService.getDetailSchedule(hoscode, depcode, workDate));
    }

    @ApiOperation(value = "根据排班id获取排班数据")
    @GetMapping("/getSchedule/{scheduleId}")
    public Result getSchedule(@PathVariable("scheduleId")String scheduleId){
        Schedule schedule = scheduleService.getScheduleById(scheduleId);
        return Result.ok(schedule);
    }
}
