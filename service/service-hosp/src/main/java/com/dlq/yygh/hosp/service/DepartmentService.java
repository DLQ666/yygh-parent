package com.dlq.yygh.hosp.service;

import com.dlq.yygh.model.hosp.Department;

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
    List<Department> getDepartment(HttpServletRequest request);
}
