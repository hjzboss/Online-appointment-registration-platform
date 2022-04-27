package com.hjznb.yygh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hjznb.yygh.model.hosp.HospitalSet;
import com.hjznb.yygh.vo.order.SignInfoVo;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/1/3 11:34
 */
public interface HospitalSetService extends IService<HospitalSet> {

    String getSignKey(String hoscode);
    //获取医院签名信息
    SignInfoVo getSignInfoVo(String hoscode);
}
