package com.hjznb.yygh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hjznb.yygh.model.hosp.Department;
import com.hjznb.yygh.repository.DepartmentRepository;
import com.hjznb.yygh.service.DepartmentService;
import com.hjznb.yygh.vo.hosp.DepartmentQueryVo;
import com.hjznb.yygh.vo.hosp.DepartmentVo;
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
 * @author hjz
 * @version 1.0
 * @date 2022/2/1 17:13
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    //上传科室
    @Override
    public void save(Map<String, Object> map) {
        //转换为Department对象
        String mapString = JSONObject.toJSONString(map);
        Department department = JSONObject.parseObject(mapString, Department.class);
        //查询部门
        Department departmentExist = departmentRepository.getDepartmentByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());
        //判断是否存在
        if (departmentExist != null) {
            department.setId(departmentExist.getId());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            department.setCreateTime(departmentExist.getCreateTime());
            departmentRepository.delete(departmentExist);
            departmentRepository.save(department);
        } else {
            department.setCreateTime(new Date());
            department.setIsDeleted(0);
            department.setUpdateTime(new Date());
            departmentRepository.save(department);
        }

    }

    @Override
    public Page<Department> findPageDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        //传来的page从1开始，而page是从0开始的
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        //创建Example对象
        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo, department);
        department.setIsDeleted(0);
        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
                .withIgnoreCase(true); //改变默认大小写忽略方式：忽略大小写

        //创建实例
        Example<Department> example = Example.of(department, matcher);

        return departmentRepository.findAll(example, pageable);
    }

    @Override
    public void remove(String hoscode, String depcode) {
        Department exist = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (exist != null) {
            departmentRepository.deleteById(exist.getId());
        }
    }

    //根据医院编号，查询所有科室列表
    @Override
    public List<DepartmentVo> getDeptTree(String hoscode) {
        ArrayList<DepartmentVo> result = new ArrayList<>();
        //根据hoscode查询所有科室
        Department query = new Department();
        query.setHoscode(hoscode);
        Example<Department> of = Example.of(query);
        List<Department> all = departmentRepository.findAll(of);
        //将科室按大编号bigcode分组，获取每个大科室下的所有小科室
        Map<String, List<Department>> collect = all.stream().collect(Collectors.groupingBy(Department::getBigcode));
        for (Map.Entry<String, List<Department>> entry : collect.entrySet()) {
            String bigCode = entry.getKey();
            List<Department> departmentList = entry.getValue();
            //封装大科室
            DepartmentVo departmentVo = new DepartmentVo();
            departmentVo.setDepcode(bigCode);
            departmentVo.setDepname(departmentList.get(0).getBigname());
            //封装小科室
            ArrayList<DepartmentVo> children = new ArrayList<>();
            for (Department department : departmentList) {
                DepartmentVo child = new DepartmentVo();
                child.setDepcode(department.getDepcode());
                child.setDepname(department.getDepname());
                children.add(child);
            }
            //将小科室放入大科室中，在将大科室放到result中
            departmentVo.setChildren(children);
            result.add(departmentVo);
        }
        return result;
    }
}
