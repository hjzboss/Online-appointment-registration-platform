package com.hjznb.yygh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hjznb.yygh.model.hosp.Department;
import com.hjznb.yygh.model.hosp.Schedule;
import com.hjznb.yygh.repository.ScheduleRepository;
import com.hjznb.yygh.service.HospitalService;
import com.hjznb.yygh.service.ScheduleService;
import com.hjznb.yygh.vo.hosp.BookingScheduleRuleVo;
import com.hjznb.yygh.vo.hosp.ScheduleQueryVo;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/2/1 21:04
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository repository;
    private final MongoTemplate mongoTemplate;
    private final HospitalService hospitalService;

    public ScheduleServiceImpl(ScheduleRepository repository, MongoTemplate mongoTemplate, HospitalService hospitalService) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
        this.hospitalService = hospitalService;
    }

    @Override
    public void save(Map<String, Object> map) {
        //转换为Schedule对象
        String mapString = JSONObject.toJSONString(map);
        Schedule schedule = JSONObject.parseObject(mapString, Schedule.class);

        //查询排班
        Schedule ScheduleExist = repository.getScheduleByHoscodeAndHosScheduleId(schedule.getHoscode()
                , schedule.getHosScheduleId());
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
        if (exist != null) {
            repository.deleteById(exist.getId());
        }
    }

    @Override
    public Map<String, Object> getRuleSchedule(long page, long limit, String hoscode, String depcode) {
        //1 根据医院编号 和 科室编号 查询
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode);

        //2 根据工作日workDate期进行分组
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria),//匹配条件
                Aggregation.group("workDate")//分组字段
                        .first("workDate").as("workDate")
                        //3 统计号源数量
                        .count().as("docCount")
                        .sum("reservedNumber").as("reservedNumber")
                        .sum("availableNumber").as("availableNumber"),
                //排序
                Aggregation.sort(Sort.Direction.DESC,"workDate"),
                //4 实现分页
                Aggregation.skip((page-1)*limit),
                Aggregation.limit(limit)
        );
        //调用方法，最终执行
        AggregationResults<BookingScheduleRuleVo> aggResults =
                mongoTemplate.aggregate(agg, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> bookingScheduleRuleVoList = aggResults.getMappedResults();

        //分组查询的总记录数
        Aggregation totalAgg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
        );
        AggregationResults<BookingScheduleRuleVo> totalAggResults =
                mongoTemplate.aggregate(totalAgg,
                        Schedule.class, BookingScheduleRuleVo.class);
        int total = totalAggResults.getMappedResults().size();

        //把日期对应星期获取
        for(BookingScheduleRuleVo bookingScheduleRuleVo:bookingScheduleRuleVoList) {
            Date workDate = bookingScheduleRuleVo.getWorkDate();
            String dayOfWeek = this.getDayOfWeek(new DateTime(workDate));
            bookingScheduleRuleVo.setDayOfWeek(dayOfWeek);
        }

        //设置最终数据，进行返回
        Map<String, Object> result = new HashMap<>();
        result.put("bookingScheduleRuleList",bookingScheduleRuleVoList);
        result.put("total",total);

        //获取医院名称
        String hosName = hospitalService.getHospName(hoscode);
        //其他基础数据
        Map<String, String> baseMap = new HashMap<>();
        baseMap.put("hosname",hosName);
        result.put("baseMap",baseMap);

        return result;

    }

    /**
     * 根据日期获取周几数据
     */
    private String getDayOfWeek(DateTime dateTime) {
        String dayOfWeek = "";
        switch (dateTime.getDayOfWeek()) {
            case DateTimeConstants.SUNDAY:
                dayOfWeek = "周日";
                break;
            case DateTimeConstants.MONDAY:
                dayOfWeek = "周一";
                break;
            case DateTimeConstants.TUESDAY:
                dayOfWeek = "周二";
                break;
            case DateTimeConstants.WEDNESDAY:
                dayOfWeek = "周三";
                break;
            case DateTimeConstants.THURSDAY:
                dayOfWeek = "周四";
                break;
            case DateTimeConstants.FRIDAY:
                dayOfWeek = "周五";
                break;
            case DateTimeConstants.SATURDAY:
                dayOfWeek = "周六";
            default:
                break;
        }
        return dayOfWeek;
    }

}
