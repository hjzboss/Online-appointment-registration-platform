package com.hjznb.yygh.controller.api;

import com.hjznb.yygh.common.exception.YyghException;
import com.hjznb.yygh.common.helper.HttpRequestHelper;
import com.hjznb.yygh.common.result.Result;
import com.hjznb.yygh.common.result.ResultCodeEnum;
import com.hjznb.yygh.common.utils.MD5;
import com.hjznb.yygh.model.hosp.Department;
import com.hjznb.yygh.model.hosp.Hospital;
import com.hjznb.yygh.model.hosp.Schedule;
import com.hjznb.yygh.service.DepartmentService;
import com.hjznb.yygh.service.HospitalService;
import com.hjznb.yygh.service.HospitalSetService;
import com.hjznb.yygh.service.ScheduleService;
import com.hjznb.yygh.vo.hosp.DepartmentQueryVo;
import com.hjznb.yygh.vo.hosp.ScheduleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/1/22 20:52
 */
@Api(tags = "供医院调用的接口")
@RestController
@RequestMapping("/api/hosp")
public class ApiController {
    private final HospitalService hospitalService;
    private final HospitalSetService hospitalSetService;
    private final DepartmentService departmentService;
    private final ScheduleService scheduleService;

    public ApiController(HospitalService hospitalService, HospitalSetService hospitalSetService, DepartmentService departmentService, ScheduleService scheduleService) {
        this.hospitalService = hospitalService;
        this.hospitalSetService = hospitalSetService;
        this.departmentService = departmentService;
        this.scheduleService = scheduleService;
    }

    //上传医院接口
    @ApiOperation(value = "上传医院接口")
    @PostMapping("/saveHospital")
    public Result saveHosp(HttpServletRequest request) {
        Map<String, Object> map = getAndCheck(request);
        //传输过程中+”转换为了“ ”，因此我们要转换回来
        String logoData = (String) map.get("logoData");
        logoData = logoData.replaceAll(" ", "+");
        map.put("logoData", logoData);
        //调用service方法
        hospitalService.save(map);
        return Result.ok();
    }

    //查询医院
    @ApiOperation(value = "查询医院接口")
    @PostMapping("hospital/show")
    public Result getHospital(HttpServletRequest request) {
        Map<String, Object> map = getAndCheck(request);
        String hoscode = (String) map.get("hoscode");
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        return Result.ok(hospital);
    }

    //上传科室
    @ApiOperation(value = "上传科室接口")
    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest request) {
        Map<String, Object> map = getAndCheck(request);
        departmentService.save(map);
        return Result.ok();
    }

    //查询科室
    @ApiOperation(value = "查询科室接口")
    @PostMapping("department/list")
    public Result getDepartment(HttpServletRequest request) {
        Map<String, Object> map = getAndCheck(request);
        String hoscode = (String) map.get("hoscode");
        // 当前页和每页记录数
        int page = StringUtils.isEmpty(map.get("page")) ? 1 : Integer.parseInt((String) map.get("page"));
        int limit = StringUtils.isEmpty(map.get("limit")) ? 10 : Integer.parseInt((String) map.get("limit"));

        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);
        Page<Department> pageModel = departmentService.findPageDepartment(page, limit, departmentQueryVo);
        return Result.ok(pageModel);
    }

    //删除科室接口
    @ApiOperation(value = "删除科室接口")
    @PostMapping("department/remove")
    public Result removeDepartment(HttpServletRequest request) {
        Map<String, Object> map = getAndCheck(request);
        String hoscode = (String) map.get("hoscode");
        String depcode = (String) map.get("depcode");
        departmentService.remove(hoscode, depcode);
        return Result.ok();
    }

    //上传排班接口
    @ApiOperation(value = "上传排班接口")
    @PostMapping("saveSchedule")
    public Result saveSchedule(HttpServletRequest request) {
        Map<String, Object> map = getAndCheck(request);
        scheduleService.save(map);
        return Result.ok();
    }

    //查询排班接口
    @ApiOperation(value = "查询排班接口")
    @PostMapping("schedule/list")
    public Result getSchedule(HttpServletRequest request) {
        Map<String, Object> map = getAndCheck(request);
        String hoscode = (String) map.get("hoscode");
        // 当前页和每页记录数
        int page = StringUtils.isEmpty(map.get("page")) ? 1 : Integer.parseInt((String) map.get("page"));
        int limit = StringUtils.isEmpty(map.get("limit")) ? 10 : Integer.parseInt((String) map.get("limit"));

        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        Page<Schedule> pageModel = scheduleService.findPageDepartment(page, limit, scheduleQueryVo);
        return Result.ok(pageModel);
    }

    //删除排班接口
    @ApiOperation(value = "删除排班接口")
    @PostMapping("schedule/remove")
    public Result removeSchedule(HttpServletRequest request) {
        Map<String, Object> map = getAndCheck(request);
        String hoscode = (String) map.get("hoscode");
        String hosScheduleId = (String) map.get("hosScheduleId");
        scheduleService.remove(hoscode, hosScheduleId);
        return Result.ok();
    }

    //获取医院信息并校验签名
    private Map<String, Object> getAndCheck(HttpServletRequest request) {
        //获取传递过来的医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(requestMap);
        //1.获取医院系统传递过来的签名,签名进行了MD5加密
        String hospSign = (String) map.get("sign");
        //2.根据传递过来的医院编码，查询数据库，查询签名
        String hoscode = (String) map.get("hoscode");
        if (StringUtils.isEmpty(hoscode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        String signKey = hospitalSetService.getSignKey(hoscode);
        //3.把数据库查询签名进行MD5加密
        String encrypt = MD5.encrypt(signKey);
        //4.判断签名是否一致
        if (!hospSign.equals(encrypt)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        return map;
    }

}
