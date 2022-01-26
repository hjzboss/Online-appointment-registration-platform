package com.hjznb.yygh.service;

import com.hjznb.yygh.model.hosp.Hospital;

import java.util.Map;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/1/22 20:50
 */
public interface HospitalService {
    //上传医院
    void save(Map<String, Object> map);
    //根据医院编号查询
    Hospital getByHoscode(String hoscode);
}
