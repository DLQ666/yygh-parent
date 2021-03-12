package com.dlq.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSON;
import com.dlq.yygh.hosp.repository.DepartmentRepository;
import com.dlq.yygh.hosp.service.DepartmentService;
import com.dlq.yygh.model.hosp.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
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
            departmentRepository.save(departmentExist);
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
    public List<Department> getDepartment(HttpServletRequest request) {
        return null;
    }
}
