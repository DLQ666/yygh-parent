package com.dlq.yygh.hosp.controller;

import com.dlq.yygh.hosp.service.HospitalSetService;
import com.dlq.yygh.model.hosp.HospitalSet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *@program: common-util
 *@description:
 *@author: Hasee
 *@create: 2021-03-10 15:44
 */
@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

    @ApiOperation(value = "获取所有医院设置")
    @GetMapping("/findAll")
    public List<HospitalSet> findAllHospitalSet() {
        List<HospitalSet> list = hospitalSetService.list();
        return list;
    }

    @ApiOperation(value = "逻辑删除医院设置")
    @DeleteMapping("/{id}")
    public boolean deleteHospitalSet(@PathVariable("id") Long id) {
        boolean flag = hospitalSetService.removeById(id);
        return flag;
    }
}
