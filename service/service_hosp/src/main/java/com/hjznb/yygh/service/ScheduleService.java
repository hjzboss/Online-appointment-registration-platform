package com.hjznb.yygh.service;

import com.hjznb.yygh.model.hosp.Department;
import com.hjznb.yygh.model.hosp.Schedule;
import com.hjznb.yygh.vo.hosp.ScheduleOrderVo;
import com.hjznb.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Map;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/2/1 21:03
 */
public interface ScheduleService {
    //上传排班
    void save(Map<String, Object> map);
    //查询排班
    Page<Schedule> findPageDepartment(int page, int limit, ScheduleQueryVo scheduleQueryVo);
    //删除排班
    void remove(String hoscode, String hosScheduleId);
    //根据医院编号和科室编号获取排班规则
    Map<String, Object> getRuleSchedule(long page, long limit, String hoscode, String depcode);
    //根据医院编号、科室编号和工作日期，查询排班详细信息
    List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate);
    //获取排班可预约日期数据
    Map<String, Object> getBookingScheduleRule(int page, int limit, String hoscode, String depcode);
    //根据id获取排班
    Schedule getById(String id);
    //根据排班id获取预约下单数据
    ScheduleOrderVo getScheduleOrderVo(String scheduleId);
    //修改排班
    void update(Schedule schedule);
    //根据HosScheduleId查询
    Schedule getByHosScheduleId(String id);
}
