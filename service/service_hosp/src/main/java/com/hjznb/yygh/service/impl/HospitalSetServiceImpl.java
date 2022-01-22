package com.hjznb.yygh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjznb.yygh.mapper.HospitalSetMapper;
import com.hjznb.yygh.model.hosp.HospitalSet;
import com.hjznb.yygh.service.HospitalSetService;
import org.springframework.stereotype.Service;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/1/3 11:38
 */
//ServiceImpl中已注入BaseMapper，无需注入mapper
@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {

    //根据传递过来的医院编码，查询数据库，查询签名
    @Override
    public String getSignKey(String hoscode) {
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        wrapper.eq("hoscode", hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(wrapper);
        return hospitalSet.getSignKey();
    }


}
