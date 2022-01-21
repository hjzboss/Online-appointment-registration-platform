package com.hjznb.yygh.controller;

import com.hjznb.yygh.common.result.Result;
import com.hjznb.yygh.model.cmn.Dict;
import com.hjznb.yygh.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/1/15 16:49
 */
@Api(value = "数据字典接口")
@RestController
@RequestMapping("/admin/cmn/dict")
@CrossOrigin
public class DictController {
    private final DictService dictService;

    public DictController(DictService dictService) {
        this.dictService = dictService;
    }

    //导出数据字典接口
    @GetMapping("exportData")
    public void exportDict(HttpServletResponse response) {
        dictService.exportDictData(response);
    }

    //导入数据字典
    @PostMapping("importData")
    public Result importDict(MultipartFile file) {
        dictService.importDictData(file);
        return Result.ok();
    }

    //根据数据id查询子数据列表
    @ApiOperation(value = "根据数据id查询子数据列表")
    @GetMapping("findChildData/{id}")
    public Result findChildData(@PathVariable Long id) {
        List<Dict> list = dictService.findChildData(id);
        return Result.ok(list);
    }
}
