package com.dlq.yygh.hosp.repository;

import com.dlq.yygh.model.hosp.Department;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *@description:
 *@author: Hasee
 *@create: 2021-03-12 20:48
 */
public interface DepartmentRepository extends MongoRepository<Department,String> {

    Department getDepartmentByHoscodeAndDepcode(String hoscode, String depcode);

}
