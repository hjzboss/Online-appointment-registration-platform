package com.hjznb.yygh.controller;

import com.hjznb.yygh.common.result.Result;
import com.hjznb.yygh.model.hosp.Schedule;
import com.hjznb.yygh.service.ScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/2/19 22:26
 */
@Api(tags = "排班管理")
@RestController
@RequestMapping("/admin/hosp/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    //根据医院的编号 和 科室编号，查询排班规则数据
    @ApiOperation("查询排班规则数据")
    @GetMapping("getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getScheduleRule(@PathVariable Integer page,
                                  @PathVariable Integer limit,
                                  @PathVariable String depcode,
                                  @PathVariable String hoscode) {
        Map<String, Object> map = scheduleService.getRuleSchedule(page, limit, hoscode, depcode);
        return Result.ok(map);
    }

    //根据医院编号、科室编号和工作日期，查询排班详细信息
    @ApiOperation(value = "查询排班详细信息")
    @GetMapping("getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public Result getScheduleDetail(@PathVariable String hoscode, @PathVariable String depcode, @PathVariable String workDate) {
        List<Schedule> list = scheduleService.getDetailSchedule(hoscode, depcode, workDate);
        return Result.ok(list);
    }
}
