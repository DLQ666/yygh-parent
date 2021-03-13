package com.dlq.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSON;
import com.dlq.yygh.hosp.repository.DepartmentRepository;
import com.dlq.yygh.hosp.service.DepartmentService;
import com.dlq.yygh.model.hosp.Department;
import com.dlq.yygh.vo.hosp.DepartmentQueryVo;
import com.dlq.yygh.vo.hosp.DepartmentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    /**
     * 根据医院编号，查询医院所有科室列表
     */
    @Override
    public List<DepartmentVo> findDeptTree(String hoscode) {
        //创建list集合，用于最终数据封装
        List<DepartmentVo> result = new ArrayList<>();

        //根据医院编号，查询医院所有科室信息
        Department department = new Department();
        department.setHoscode(hoscode);
        Example<Department> example = Example.of(department);
        //所有科室列表信息
        List<Department> departmentList = departmentRepository.findAll(example);

        //现根据大科室 bigCode 进行分组，获取每个大科室里面下级子科室
        Map<String, List<Department>> departmentMap = departmentList.stream().collect(Collectors.groupingBy(Department::getBigcode));
        //遍历map集合  departmentMap
        departmentMap.forEach((string, departments) -> {
            DepartmentVo departmentVo = new DepartmentVo();
            //大科室编号
            departmentVo.setDepcode(string);
            ///大科室编号对应的全局数据
            departmentVo.setDepname(departments.get(0).getBigname());

            //封装小科室 集合
            ArrayList<DepartmentVo> children = new ArrayList<>();
            departments.forEach(item->{
                DepartmentVo vo = new DepartmentVo();
                vo.setDepcode(item.getDepcode());
                vo.setDepname(item.getDepname());
                //封装到list集合
                children.add(vo);
            });
            // 将小科室 集合 封装到大科室 里面
            //封装大科室 孩子节点
            departmentVo.setChildren(children);

            //将封装好的 DepartmentVo 封装到 最终的 方法返回的List<DepartmentVo>中 返回给前端
            result.add(departmentVo);
        });

        return result;
    }
}
