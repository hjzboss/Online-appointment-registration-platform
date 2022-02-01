package com.hjznb.yygh.repository;

import com.hjznb.yygh.model.hosp.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/2/1 17:11
 *
 * 科室
 */
@Repository
public interface DepartmentRepository extends MongoRepository<Department, String> {
    //根据hoscode和depcode来查询科室
    Department getDepartmentByHoscodeAndDepcode(String hoscode, String depcode);
}
