package com.hjznb.yygh.service;

import com.hjznb.yygh.model.hosp.Hospital;
import com.hjznb.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

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
    //医院列表（条件查询带分页）
    Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);
}