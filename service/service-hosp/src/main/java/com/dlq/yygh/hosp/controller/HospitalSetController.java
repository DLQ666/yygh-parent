package com.dlq.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dlq.yygh.common.result.Result;
import com.dlq.yygh.hosp.service.HospitalSetService;
import com.dlq.yygh.model.hosp.HospitalSet;
import com.dlq.yygh.utils.MD5;
import com.dlq.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

/**
 *@program: common-util
 *@description:
 *@author: Hasee
 *@create: 2021-03-10 15:44
 */
@Api(tags = "医院设置管理")
@CrossOrigin
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

    @ApiOperation(value = "获取所有医院设置")
    @GetMapping("/findAll")
    public Result<List<HospitalSet>> findAllHospitalSet() {
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    @ApiOperation(value = "逻辑删除医院设置")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteHospitalSet(@PathVariable("id") Long id) {
        boolean flag = hospitalSetService.removeById(id);
        if (flag){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    /**
     * 分页查询+条件查询
     */
    @PostMapping("/findPageHospSet/{current}/{limit}")
    public Result<Page<HospitalSet>> findPageHospSet(@PathVariable("current") long current,
                                  @PathVariable("limit")long limit,
                                  @RequestBody(required = false) HospitalSetQueryVo queryVo){
        //current 当前页  limit 每页记录数
        //创建Page对象，传递当前页，每页记录数
        Page<HospitalSet> page = new Page<>(current, limit);
        //构建条件
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(queryVo.getHosname())){
            wrapper.like("hosname", queryVo.getHosname());
        }
        if (!StringUtils.isEmpty(queryVo.getHoscode())){
            wrapper.eq("hoscode", queryVo.getHoscode());
        }
        //调用方法实现分页查询
        Page<HospitalSet> hospitalSetPage = hospitalSetService.page(page, wrapper);
        return Result.ok(hospitalSetPage);
    }

    /**
     * 保存医院设置
     * @param hospitalSet
     * @return
     */
    @PostMapping("/saveHospitalSet")
    public Result<Boolean> saveHospitalSet(@RequestBody HospitalSet hospitalSet){
        //设置状态 1 使用     0 不能使用
        hospitalSet.setStatus(1);
        //签名秘钥
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis()+""+random.nextInt(1000)));
        boolean save = hospitalSetService.save(hospitalSet);
        if (save){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    /**
     * 根据id 查询医院设置
     */
    @GetMapping("/getHospSet/{id}")
    public Result<HospitalSet> getHospSet(@PathVariable("id")Long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }

    /**
     * 根据id 修改医院设置
     */
    @PostMapping("/updateHospSet")
    public Result<Boolean> updateHospSet(@RequestBody HospitalSet hospitalSet){
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if (flag){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    /**
     * 批量删除医院设置
     */
    @DeleteMapping("/batchRemove")
    public Result<Boolean> batchRemoveHospitalSet(@RequestBody List<Long> idList){
        hospitalSetService.removeByIds(idList);
        return Result.ok();
    }

    /**
     * 医院设置锁定和解锁
     */
    @PutMapping("/lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable("id")Long id,
                                  @PathVariable("status")Integer status){
        //根据id查询医院设置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        //设置状态
        hospitalSet.setStatus(status);
        hospitalSetService.updateById(hospitalSet);
        return Result.ok();
    }


    /**
     * 发送签名秘钥
     */
    @PutMapping("/sendKey/{id}")
    public Result lockHospitalSet(@PathVariable("id")Long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        //TODO 发送短信
        return Result.ok();
    }
}
