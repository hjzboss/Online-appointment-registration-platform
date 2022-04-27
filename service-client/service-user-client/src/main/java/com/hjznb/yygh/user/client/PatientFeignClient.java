package com.hjznb.yygh.user.client;

import com.hjznb.yygh.model.user.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/4/27 13:37
 */
@FeignClient(value = "service-user")
@Repository
public interface PatientFeignClient {
    //根据就诊人id获取就诊人信息
    @GetMapping("/api/user/patient/inner/get/{id}")
    public Patient getPatientOrder(@PathVariable("id") Long id);
}
