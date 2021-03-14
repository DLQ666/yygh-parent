package com.dlq.yygh.hosp.controller;

import com.dlq.yygh.common.result.Result;
import com.dlq.yygh.hosp.service.HospitalService;
import com.dlq.yygh.model.hosp.Hospital;
import com.dlq.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 *@program: yygh-parent
 *@description:
 *@author: Hasee
 *@create: 2021-03-13 14:44
 */
@RestController
@RequestMapping("/admin/hosp/hospital")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    /**
     * 查询医院列表
     */
    @GetMapping("/list/{page}/{limit}")
    public Result listHosp(@PathVariable("page") Integer page,
                           @PathVariable("limit") Integer limit,
                           HospitalQueryVo hospitalQueryVo){

        Page<Hospital> hospitalPage = hospitalService.selectHospPage(page,limit,hospitalQueryVo);
        return Result.ok(hospitalPage);
    }

    /**
     * 更新医院上线状态
     */
    @GetMapping("/updateHospStatus/{id}/{status}")
    public Result updateHospStatus(@PathVariable("id")String id,@PathVariable("status")Integer status){
        hospitalService.updateHospStatus(id,status);
        return Result.ok();
    }

    /**
     * 医院详情
     */
    @GetMapping("/showHospDetail/{id}")
    public Result<Map<String, Object>> showHospDetail(@PathVariable("id")String id){
        Map<String, Object> hospital = hospitalService.getHospById(id);
        return Result.ok(hospital);
    }
}
