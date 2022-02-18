package com.hjznb.yygh.service;

import com.hjznb.yygh.model.hosp.Department;
import com.hjznb.yygh.vo.hosp.DepartmentQueryVo;
import com.hjznb.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/2/1 17:13
 */
public interface DepartmentService {
    //上传科室
    void save(Map<String, Object> map);
    //查询科室
    Page<Department> findPageDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo);
    //删除科室
    void remove(String hoscode, String depcode);
    //根据hoscode查询科室和所有下级科室
    List<DepartmentVo> getDeptTree(String hoscode);
}
