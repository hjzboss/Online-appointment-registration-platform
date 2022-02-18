package com.hjznb.yygh.controller;

import com.hjznb.yygh.common.result.Result;
import com.hjznb.yygh.service.DepartmentService;
import com.hjznb.yygh.vo.hosp.DepartmentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/2/18 19:29
 */
@RestController
@CrossOrigin
@Api(tags = "科室管理")
@RequestMapping("admin/hosp/department")
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @ApiOperation(value = "查询医院所有科室列表")
    @GetMapping("getDeptList/{hoscode}")
    public Result getDeptList(@PathVariable String hoscode) {
        List<DepartmentVo> list = departmentService.getDeptTree(hoscode);
        return Result.ok(list);
    }
}
