package com.hjznb.yygh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hjznb.yygh.model.hosp.Department;
import com.hjznb.yygh.model.hosp.Schedule;
import com.hjznb.yygh.repository.ScheduleRepository;
import com.hjznb.yygh.service.ScheduleService;
import com.hjznb.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/2/1 21:04
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository repository;

    public ScheduleServiceImpl(ScheduleRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Map<String, Object> map) {
        //转换为Schedule对象
        String mapString = JSONObject.toJSONString(map);
        Schedule schedule = JSONObject.parseObject(mapString, Schedule.class);

        //查询排班
        Schedule ScheduleExist = repository.getScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(), schedule.getHosScheduleId());
        //判断是否存在
        if (ScheduleExist != null) {
            schedule.setId(ScheduleExist.getId());
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(0);
            schedule.setCreateTime(ScheduleExist.getCreateTime());
            schedule.setStatus(1);
            repository.delete(ScheduleExist);
            repository.save(schedule);
        } else {
            schedule.setCreateTime(new Date());
            schedule.setIsDeleted(0);
            schedule.setUpdateTime(new Date());
            schedule.setStatus(1);
            repository.save(schedule);
        }
    }

    @Override
    public Page<Schedule> findPageDepartment(int page, int limit, ScheduleQueryVo scheduleQueryVo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        //传来的page从1开始，而page是从0开始的
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        //创建Example对象
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleQueryVo, schedule);
        schedule.setIsDeleted(0);
        schedule.setStatus(1);

        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
                .withIgnoreCase(true); //改变默认大小写忽略方式：忽略大小写

        //创建实例
        Example<Schedule> example = Example.of(schedule, matcher);

        return repository.findAll(example, pageable);
    }

    @Override
    public void remove(String hoscode, String hosScheduleId) {
        Schedule exist = repository.getScheduleByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
        if(exist != null) {
            repository.deleteById(exist.getId());
        }
    }
}
