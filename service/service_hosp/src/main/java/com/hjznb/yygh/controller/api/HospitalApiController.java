package com.hjznb.yygh.controller.api;

import com.hjznb.yygh.common.result.Result;
import com.hjznb.yygh.model.hosp.Hospital;
import com.hjznb.yygh.service.DepartmentService;
import com.hjznb.yygh.service.HospitalService;
import com.hjznb.yygh.service.ScheduleService;
import com.hjznb.yygh.vo.hosp.DepartmentVo;
import com.hjznb.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/3/17 11:46
 */
@Api(tags = "医院管理接口")
@RestController
@RequestMapping("/api/hosp/hospital")
public class HospitalApiController {
    private final HospitalService hospitalService;
    private final DepartmentService departmentService;
    private final ScheduleService scheduleService;

    public HospitalApiController(HospitalService hospitalService, DepartmentService departmentService, ScheduleService scheduleService) {
        this.hospitalService = hospitalService;
        this.departmentService = departmentService;
        this.scheduleService = scheduleService;
    }

    @ApiOperation(value = "查询医院列表")
    @GetMapping("findHospList/{page}/{limit}")
    public Result findHospList(@PathVariable Integer limit, @PathVariable Integer page, HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> hospitals = hospitalService.selectHospPage(page, limit, hospitalQueryVo);
        return Result.ok(hospitals);
    }

    @ApiOperation(value = "根据医院名称获取医院列表")
    @GetMapping("findByHosname/{hosname}")
    public Result findByHosname(
            @ApiParam(name = "hosname", value = "医院名称", required = true)
            @PathVariable String hosname) {
        return Result.ok(hospitalService.findByHosname(hosname));
    }

    @ApiOperation(value = "根据医院编号获取科室信息")
    @GetMapping("department/{hoscode}")
    public Result index(@PathVariable String hoscode) {
        List<DepartmentVo> list = departmentService.getDeptTree(hoscode);
        return Result.ok(list);
    }

    @ApiOperation(value = "根据医院编号获取详情信息")
    @GetMapping("{hoscode}")
    public Result item(@PathVariable String hoscode) {
        Map<String, Object> map = hospitalService.item(hoscode);
        return Result.ok(map);
    }

    @ApiOperation(value = "获取可预约排班数据")
    @GetMapping("auth/getBookingScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getBookingSchedule(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Integer page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Integer limit,
            @ApiParam(name = "hoscode", value = "医院code", required = true)
            @PathVariable String hoscode,
            @ApiParam(name = "depcode", value = "科室code", required = true)
            @PathVariable String depcode) {
        return Result.ok(scheduleService.getBookingScheduleRule(page, limit, hoscode, depcode));
    }

    @ApiOperation(value = "获取排班数据")
    @GetMapping("auth/findScheduleList/{hoscode}/{depcode}/{workDate}")
    public Result findScheduleList(
            @ApiParam(name = "hoscode", value = "医院code", required = true)
            @PathVariable String hoscode,
            @ApiParam(name = "depcode", value = "科室code", required = true)
            @PathVariable String depcode,
            @ApiParam(name = "workDate", value = "排班日期", required = true)
            @PathVariable String workDate) {
        return Result.ok(scheduleService.getDetailSchedule(hoscode, depcode, workDate));
    }

    @ApiOperation(value = "根据排班id获取排班数据")
    @GetMapping("getSchedule/{scheduleId}")
    public Result getSchedule(
            @ApiParam(name = "scheduleId", value = "排班id", required = true)
            @PathVariable String scheduleId) {
        return Result.ok(scheduleService.getById(scheduleId));
    }

}
