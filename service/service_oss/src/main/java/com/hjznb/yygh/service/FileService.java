package com.hjznb.yygh.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/4/24 11:00
 */
public interface FileService {
    //上传文件到阿里云oss
    String upload(MultipartFile file);
}

