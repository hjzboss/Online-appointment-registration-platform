package com.hjznb.yygh.controller;

import com.hjznb.yygh.common.result.Result;
import com.hjznb.yygh.model.hosp.Hospital;
import com.hjznb.yygh.service.HospitalService;
import com.hjznb.yygh.vo.hosp.HospitalQueryVo;
import com.hjznb.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/2/2 20:30
 */
@Api(tags = "医院管理")
@RestController
@RequestMapping("/admin/hosp/hospital")
public class HospitalController {
    private final HospitalService hospitalService;

    public HospitalController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    //医院列表
    @ApiOperation(value = "医院列表")
    @GetMapping("list/{page}/{limit}")
    public Result listHospital(@PathVariable Integer page,
                               @PathVariable Integer limit,
                               HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> pageModel = hospitalService.selectHospPage(page, limit, hospitalQueryVo);
        return Result.ok(pageModel);
    }

    //更新医院上线状态
    @ApiOperation(value = "更新医院上线状态")
    @GetMapping("updateHospStatus/{id}/{status}")
    public Result updateHospStatus(@PathVariable String id, @PathVariable Integer status) {
        hospitalService.updateStatus(id, status);
        return Result.ok();
    }

    //医院详情信息
    @ApiOperation(value = "医院详情信息")
    @GetMapping("showHospDetail/{id}")
    public Result showHospDetail(@PathVariable String id) {
        Map<String, Object> map = hospitalService.getHospById(id);
        return Result.ok(map);
    }
}
