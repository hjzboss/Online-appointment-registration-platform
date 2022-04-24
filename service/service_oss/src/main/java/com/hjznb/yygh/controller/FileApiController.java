package com.hjznb.yygh.controller;

import com.hjznb.yygh.common.result.Result;
import com.hjznb.yygh.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/4/24 11:06
 */
@Api(tags = "文件上传")
@RestController
@RequestMapping("/api/oss/file")
public class FileApiController {
    private final FileService fileService;

    public FileApiController(FileService fileService) {
        this.fileService = fileService;
    }

    //上传文件到阿里云oss
    @ApiOperation(value = "上传文件到阿里云oss")
    @PostMapping("fileUpload")
    public Result fileUpload(MultipartFile file) {
        //获取上传文件
        String url = fileService.upload(file);
        return Result.ok(url);
    }
}
