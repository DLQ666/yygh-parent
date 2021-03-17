package com.dlq.yygh.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dlq.yygh.model.user.Patient;

import java.util.List;

/**
 *@description:
 *@author: Hasee
 *@create: 2021-03-17 13:39
 */
public interface PatientService extends IService<Patient> {

    /**
     * 获取就诊人列表
     */
    List<Patient> findAllUserId(Long userId);
}
