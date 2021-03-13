package com.dlq.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSON;
import com.dlq.yygh.hosp.repository.DepartmentRepository;
import com.dlq.yygh.hosp.service.DepartmentService;
import com.dlq.yygh.model.hosp.Department;
import com.dlq.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 *@program: yygh-parent
 *@description:
 *@author: Hasee
 *@create: 2021-03-12 20:49
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    /**
     * 上传科室接口
     */
    @Override
    public void saveDepartment(Map<String, Object> paramMap) {
        //paramMap 转换为 Department对象
        String jsonString = JSON.toJSONString(paramMap);
        Department department = JSON.parseObject(jsonString, Department.class);

        //根据医院编号 和 科室编号查询
        Department departmentExist = departmentRepository.getDepartmentByHoscodeAndDepcode(department.getHoscode(),department.getDepcode());
        //判断
        if (departmentExist != null){
            department.setId(departmentExist.getId());
            department.setCreateTime(departmentExist.getCreateTime());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }else {
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }
    }

    /**
     * 查询科室接口
     */
    @Override
    public Page<Department> getPageDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo) {
        if (page <= 0) {
            page = 1;
        }
        if (limit <= 0){
            limit = 10;
        }
        //创建pageable对象，设置当前页和每页记录数
        Pageable pageable = PageRequest.of(page-1, limit);
        //创建Example对象
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo, department);
        department.setIsDeleted(0);
        Example<Department> example = Example.of(department,matcher);
        Page<Department> all = departmentRepository.findAll(example, pageable);
        return all;
    }

    @Override
    public boolean delDepartment(String hoscode, String depcode) {
        //根据医院编号和科室编号查询
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (department == null) {
            return false;
        }
        departmentRepository.deleteById(department.getId());
        return true;
    }
}
