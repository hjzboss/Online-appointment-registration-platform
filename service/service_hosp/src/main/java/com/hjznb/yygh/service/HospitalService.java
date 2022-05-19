package com.hjznb.yygh.service;

import com.hjznb.yygh.model.hosp.Hospital;
import com.hjznb.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
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
    Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo, boolean isAdmin);

    //更新医院上线状态
    void updateStatus(String id, Integer status);

    //根据id值获取医院信息
    Map<String, Object> getHospById(String id);

    //根据hoscode获取医院名称
    String getHospName(String hoscode);

    //根据医院名称查询
    List<Hospital> findByHosname(String hosname);

    //根据医院编号获取详情信息
    Map<String, Object> item(String hoscode);
}
