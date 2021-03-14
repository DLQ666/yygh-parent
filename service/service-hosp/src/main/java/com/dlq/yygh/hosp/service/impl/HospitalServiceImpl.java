package com.dlq.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dlq.yygh.hosp.feign.CmnFeignService;
import com.dlq.yygh.hosp.repository.HospitalRepository;
import com.dlq.yygh.hosp.service.HospitalService;
import com.dlq.yygh.model.hosp.Hospital;
import com.dlq.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *@program: yygh-parent
 *@description:
 *@author: Hasee
 *@create: 2021-03-12 17:09
 */
@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;
    @Autowired
    private CmnFeignService cmnFeignService;

    /**
     * 上传医院接口
     */
    @Override
    public void save(Map<String, Object> paramMap) {
        //把参数map集合转换成对象
        String mapString = JSONObject.toJSONString(paramMap);
        Hospital hospital = JSONObject.parseObject(mapString, Hospital.class);

        //判断是否存在数据
        String hoscode = hospital.getHoscode();
        Hospital hospitalExist = hospitalRepository.getHospitalByHoscode(hoscode);


        //存在就修改
        if (hospitalExist != null){
            hospital.setId(hospitalExist.getId());
            hospital.setStatus(hospitalExist.getStatus());
            hospital.setCreateTime(hospitalExist.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }else {
            //如果不存在则添加
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }

    }

    /**
     * 查询医院接口
     */
    @Override
    public Hospital getByHoscode(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        return hospital;
    }

    /**
     * 查询医院列表
     */
    @Override
    public Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        if (page <= 0) {
            page = 1;
        }
        if (limit <= 0){
            limit = 10;
        }
        // 创建Pageable对象
        Pageable pageable = PageRequest.of(page-1,limit);
        //创建条件匹配器
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        //hospitalQueryVo 转 Hospital
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo,hospital);
        //创建对象
        Example<Hospital> example = Example.of(hospital,matcher);
        //调用方法直接查询
        Page<Hospital> pages = hospitalRepository.findAll(example, pageable);

        //获取查询list集合，遍历进行医院等级封装
        pages.getContent().forEach(this::setHospitalHosType);
        return pages;
    }

    /**
     * 更新医院上线状态
     */
    @Override
    public void updateHospStatus(String id, Integer status) {
        //根据id查询医院信息
        Hospital hospital = hospitalRepository.findById(id).get();
        //设置要修改的值
        hospital.setStatus(status);
        hospital.setUpdateTime(new Date());
        hospitalRepository.save(hospital);
    }

    /**
     * 医院详情
     */
    @Override
    public Map<String, Object> getHospById(String id) {
        HashMap<String, Object> map = new HashMap<>();
        Hospital hospital = this.setHospitalHosType(hospitalRepository.findById(id).get());
        //医院的基本信息（包含医院等级）
        map.put("hospital",hospital);
        //单独处理更直观
        map.put("bookingRule", hospital.getBookingRule());
        //不需要重复返回
        hospital.setBookingRule(null);
        return map;
    }

    @Override
    public String getHosName(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        if (hospital != null){
            return hospital.getHosname();
        }
        return null;
    }

    /**
     * 根据医院名称获取医院列表
     */
    @Override
    public List<Hospital> findByHosname(String hosname) {
        return hospitalRepository.findHospitalByHosnameLike(hosname);
    }

    //获取查询list集合，遍历 进行 医院等级封装
    private Hospital setHospitalHosType(Hospital hospital){
        //根据dictCode和value获取医院等级名称
        String hostypeString = cmnFeignService.getName("Hostype", hospital.getHostype());
        //查询省 市 地区
        String provinceString = cmnFeignService.getName(hospital.getProvinceCode());
        String cityString = cmnFeignService.getName(hospital.getCityCode());
        String districtString = cmnFeignService.getName(hospital.getDistrictCode());
        hospital.getParam().put("hostypeString",hostypeString);
        hospital.getParam().put("fullAddress",provinceString+" "+cityString+" "+districtString);
        return hospital;
    }
}
