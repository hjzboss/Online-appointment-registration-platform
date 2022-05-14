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

    //根据dictCode和value查询
    @ApiOperation(value = "根据数据dictCode和value查询")
    @GetMapping("getName/{dictCode}/{value}")
    public String getName(@PathVariable String dictCode, @PathVariable String value) {
        String dictName = dictService.getDictName(dictCode, value);
        return dictName;
    }

    //根据value查询
    @ApiOperation(value = "根据value查询")
    @GetMapping("getName/{value}")
    public String getName(@PathVariable String value) {
        return dictService.getDictName("", value);
    }

    //根据dictCode获取下级节点
    @ApiOperation(value = "根据dictCode获取下级节点")
    @GetMapping("findByDictCode/{dictCode}")
    public Result findByDictCode(@PathVariable String dictCode) {
        List<Dict> list = dictService.findByDictCode(dictCode);
        return Result.ok(list);
    }
}
