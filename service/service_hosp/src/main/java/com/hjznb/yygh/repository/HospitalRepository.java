package com.hjznb.yygh.repository;

import com.hjznb.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/1/22 20:48
 */
@Repository
public interface HospitalRepository extends MongoRepository<Hospital, String> {
    //判断是否存在数据
    Hospital getHospitalByHoscode(String hoscode);
    //根据医院名字查询
    List<Hospital> findHospitalByHosnameLike(String hosname);
}
