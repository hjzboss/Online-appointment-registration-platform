package com.hjznb.yygh.service;

import com.hjznb.yygh.model.hosp.Department;
import com.hjznb.yygh.model.hosp.Schedule;
import com.hjznb.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;

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
}
