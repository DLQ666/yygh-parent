package com.dlq.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dlq.yygh.enums.DictEnum;
import com.dlq.yygh.model.user.Patient;
import com.dlq.yygh.user.feign.CmnFeignService;
import com.dlq.yygh.user.mapper.PatientMapper;
import com.dlq.yygh.user.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *@program: yygh-parent
 *@description:
 *@author: Hasee
 *@create: 2021-03-17 13:39
 */
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper,Patient> implements PatientService {

    @Autowired
    private CmnFeignService cmnFeignService;

    /**
     * 获取就诊人列表
     */
    @Override
    public List<Patient> findAllUserId(Long userId) {
        //根据userId查询所有的就诊人列表
        List<Patient> list = baseMapper.selectList(new QueryWrapper<Patient>().eq("user_id", userId));
        //远程调用数据字典微服务 得到编码对应具体内容，查询数据字典表内容
        //其他参数封装
        list.forEach(this::packPatient);

        return list;
    }

    /**
     * 根据id获取就诊人信息
     */
    @Override
    public Patient getPatientId(Long id) {
        Patient patient = baseMapper.selectById(id);
        this.packPatient(patient);
        return patient;
    }

    /**
     * Patient 其他参数封装
     */
    private void packPatient(Patient patient) {
        //根据证件类型编码，获取证件类型具体指
        String certificatesTypeString = cmnFeignService.getName(
                DictEnum.CERTIFICATES_TYPE.getDictCode(), patient.getCertificatesType());

        //联系人证件类型
        String contactsCertificatesTypeString =
                cmnFeignService.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(),patient.getContactsCertificatesType());
        //省
        String provinceString = cmnFeignService.getName(patient.getProvinceCode());
        //市
        String cityString = cmnFeignService.getName(patient.getCityCode());
        //区
        String districtString = cmnFeignService.getName(patient.getDistrictCode());
        patient.getParam().put("certificatesTypeString", certificatesTypeString);
        patient.getParam().put("contactsCertificatesTypeString", contactsCertificatesTypeString);
        patient.getParam().put("provinceString", provinceString);
        patient.getParam().put("cityString", cityString);
        patient.getParam().put("districtString", districtString);
        patient.getParam().put("fullAddress", provinceString + cityString + districtString + patient.getAddress());
    }
}
