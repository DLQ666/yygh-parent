package com.dlq.yygh.user.controller.api;

import com.dlq.yygh.common.result.Result;
import com.dlq.yygh.common.utils.AuthContextHolder;
import com.dlq.yygh.model.user.Patient;
import com.dlq.yygh.user.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *@program: yygh-parent
 *@description:
 *@author: Hasee
 *@create: 2021-03-17 13:38
 */
@RestController
@RequestMapping("/api/user/patient")
public class PatientApiController {

    @Autowired
    private PatientService patientService;

    /**
     * 获取就诊人列表
     */
    @GetMapping("/auth/findAll")
    public Result getPatientList(HttpServletRequest request){
        Long userId = AuthContextHolder.getUserId(request);
        List<Patient> list = patientService.findAllUserId(userId);
        return Result.ok(list);
    }

    //添加就诊人
    @PostMapping("/auth/save")
    public Result savePatient(@RequestBody Patient patient,HttpServletRequest request){
        //获取当前登录的用户id
        Long userId = AuthContextHolder.getUserId(request);
        patient.setUserId(userId);
        patientService.save(patient);
        return Result.ok();
    }

    //根据id获取就诊人信息

    //修改就诊人

    //删除就诊人
}
