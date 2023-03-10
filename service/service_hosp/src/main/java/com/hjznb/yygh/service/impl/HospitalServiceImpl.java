package com.hjznb.yygh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hjznb.yygh.cmn.client.DictFeignClient;
import com.hjznb.yygh.common.exception.YyghException;
import com.hjznb.yygh.common.result.ResultCodeEnum;
import com.hjznb.yygh.model.hosp.Hospital;
import com.hjznb.yygh.repository.HospitalRepository;
import com.hjznb.yygh.service.HospitalService;
import com.hjznb.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/1/22 20:50
 */
@Service
public class HospitalServiceImpl implements HospitalService {
    private final HospitalRepository hospitalRepository;
    private final DictFeignClient dictFeignClient;

    public HospitalServiceImpl(HospitalRepository hospitalRepository, DictFeignClient dictFeignClient) {
        this.hospitalRepository = hospitalRepository;
        this.dictFeignClient = dictFeignClient;
    }

    @Override
    public void save(Map<String, Object> map) {
        //把参数map集合转换为对象
        String mapString = JSONObject.toJSONString(map);
        Hospital hospital = JSONObject.parseObject(mapString, Hospital.class);
        //判断是否存在数据
        String hoscode = hospital.getHoscode();
        Hospital hospitalExist = hospitalRepository.getHospitalByHoscode(hoscode);
        //如果存在，进行修改
        if (hospitalExist != null) {
            hospital.setId(hospitalExist.getId());
            hospital.setStatus(hospitalExist.getStatus());
            hospital.setCreateTime(hospitalExist.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            //mongodb不允许重复id，需要先删除再添加
            hospitalRepository.delete(hospitalExist);
            hospitalRepository.save(hospital);
        } else {
            //如果不存在，进行添加
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }

    }

    @Override
    public Hospital getByHoscode(String hoscode) {
        return hospitalRepository.getHospitalByHoscode(hoscode);
    }

    //条件查询带分页
    @Override
    public Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo, boolean isAdmin) {
        Pageable of = PageRequest.of(page - 1, limit);
        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreCase(true);
        Hospital hospital = new Hospital();
        if (!isAdmin) {
            hospitalQueryVo.setStatus(1); //只返回上线的医院
        }
        BeanUtils.copyProperties(hospitalQueryVo, hospital);
        Example<Hospital> example = Example.of(hospital, matcher);
        Page<Hospital> pages = hospitalRepository.findAll(example, of);
        //获取查询list集合，为hospital对象，遍历进行医院等级封装
        pages.getContent().forEach(this::setHospitalHosType);
        return pages;
    }

    @Override
    public void updateStatus(String id, Integer status) {
        Optional<Hospital> byId = hospitalRepository.findById(id);
        if (byId.isPresent()) {
            //更新status
            Hospital hospital = byId.get();
            hospitalRepository.delete(hospital);
            hospital.setStatus(status);
            hospital.setUpdateTime(new Date());
            hospitalRepository.save(hospital);
        } else {
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
    }

    @Override
    public Map<String, Object> getHospById(String id) {
        Map<String, Object> result = new HashMap<>();
        Optional<Hospital> byId = hospitalRepository.findById(id);
        if (byId.isPresent()) {
            Hospital hospital = byId.get();
            // 添加额外信息
            this.setHospitalHosType(hospital);
            result.put("hospital", hospital);
            result.put("bookingRule", hospital.getBookingRule());
            hospital.setBookingRule(null);
            return result;
        } else {
            return null;
        }
    }

    @Override
    public String getHospName(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        if (hospital != null) {
            return hospital.getHosname();
        }
        return null;
    }

    //根据医院名称查询
    @Override
    public List<Hospital> findByHosname(String hosname) {
        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreCase(true);
        Hospital hospital = new Hospital();
        hospital.setHosname(hosname);
        hospital.setStatus(1);
        Example<Hospital> example = Example.of(hospital, matcher);
        return hospitalRepository.findAll(example);
    }

    //返回医院详情
    @Override
    public Map<String, Object> item(String hoscode) {
        Map<String, Object> result = new HashMap<>();
        //医院详情
        Hospital hospital = this.getByHoscode(hoscode);
        this.setHospitalHosType(hospital);
        result.put("hospital", hospital);
        //预约规则
        result.put("bookingRule", hospital.getBookingRule());
        //不需要重复返回
        hospital.setBookingRule(null);
        return result;
    }

    //给hospital对象封装属性
    private void setHospitalHosType(Hospital item) {
        //调用cmn模块查询等级属性的名字
        String hostype = dictFeignClient.getName("Hostype", item.getHostype());
        //查询省 市 地区
        String provinceString = dictFeignClient.getName(item.getProvinceCode());
        String cityString = dictFeignClient.getName(item.getCityCode());
        String districtString = dictFeignClient.getName(item.getDistrictCode());

        item.getParam().put("fullAddress", provinceString + cityString + districtString);
        item.getParam().put("hostypeString", hostype);
    }
}
