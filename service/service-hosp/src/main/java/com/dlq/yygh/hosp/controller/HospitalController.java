package com.dlq.yygh.hosp.controller;

import com.dlq.yygh.common.result.Result;
import com.dlq.yygh.hosp.service.HospitalService;
import com.dlq.yygh.model.hosp.Hospital;
import com.dlq.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 *@program: yygh-parent
 *@description:
 *@author: Hasee
 *@create: 2021-03-13 14:44
 */
@CrossOrigin
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
}
