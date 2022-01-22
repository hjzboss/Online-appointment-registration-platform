package com.hjznb.yygh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hjznb.yygh.model.hosp.HospitalSet;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/1/3 11:34
 */
public interface HospitalSetService extends IService<HospitalSet> {

    String getSignKey(String hoscode);
}
