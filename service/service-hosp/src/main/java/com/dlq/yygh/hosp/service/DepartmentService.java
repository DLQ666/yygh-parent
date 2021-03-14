package com.dlq.yygh.hosp.service;

import com.dlq.yygh.model.hosp.Department;
import com.dlq.yygh.vo.hosp.DepartmentQueryVo;
import com.dlq.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 *@description:
 *@author: Hasee
 *@create: 2021-03-12 20:49
 */
public interface DepartmentService {
    /**
     * 上传科室接口
     */
    void saveDepartment(Map<String, Object> paramMap);

    /**
     * 查询科室接口
     */
    Page<Department> getPageDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo);

    /**
     * 删除科室接口
     */
    boolean delDepartment(String hoscode, String depcode);

    /**
     * 根据医院编号，查询医院所有科室列表
     */
    List<DepartmentVo> findDeptTree(String hoscode);

    /**
     * 根据科室编号、医院编号、查询科室名称
     */
    String getDepName(String hoscode, String depcode);
}
