package com.hjznb.yygh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjznb.yygh.mapper.HospitalSetMapper;
import com.hjznb.yygh.model.hosp.HospitalSet;
import com.hjznb.yygh.service.HospitalSetService;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/1/3 11:38
 */

@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {
    //ServiceImpl中已注入BaseMapper，无需注入mapper

}
