package com.hjznb.yygh.controller.api;

import com.hjznb.yygh.common.exception.YyghException;
import com.hjznb.yygh.common.helper.HttpRequestHelper;
import com.hjznb.yygh.common.result.Result;
import com.hjznb.yygh.common.result.ResultCodeEnum;
import com.hjznb.yygh.common.utils.MD5;
import com.hjznb.yygh.model.hosp.Hospital;
import com.hjznb.yygh.service.HospitalService;
import com.hjznb.yygh.service.HospitalSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/1/22 20:52
 */
@RestController
@RequestMapping("/api/hosp")
public class ApiController {
    private final HospitalService hospitalService;
    private final HospitalSetService hospitalSetService;

    public ApiController(HospitalService hospitalService, HospitalSetService hospitalSetService) {
        this.hospitalService = hospitalService;
        this.hospitalSetService = hospitalSetService;
    }

    //上传医院接口
    @PostMapping("/saveHospital")
    public Result saveHosp(HttpServletRequest request) {
        //获取传递过来的医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(requestMap);
        //1.获取医院系统传递过来的签名,签名进行了MD5加密
        String hospSign = (String) map.get("sign");
        //2.根据传递过来的医院编码，查询数据库，查询签名
        String hoscode = (String) map.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        //3.把数据库查询签名进行MD5加密
        String encrypt = MD5.encrypt(signKey);
        boolean equals = encrypt.equals(hospSign);
        //4.判断签名是否一致
        if (!hospSign.equals(encrypt)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        //传输过程中+”转换为了“ ”，因此我们要转换回来
        String logoData = (String) map.get("logoData");
        logoData = logoData.replaceAll(" ", "+");
        map.put("logoData", logoData);
        //调用service方法
        hospitalService.save(map);
        return Result.ok();
    }

    //查询医院
    @PostMapping("hospital/show")
    public Result getHospital(HttpServletRequest request) {
        //获取传递过来的医院信息
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = HttpRequestHelper.switchMap(parameterMap);
        //获取医院编号
        String hoscode = (String) map.get("hoscode");
        //1.获取医院系统传递过来的签名,签名进行了MD5加密
        String hospSign = (String) map.get("sign");
        //2.根据传递过来的医院编码，查询数据库，查询签名
        String signKey = hospitalSetService.getSignKey(hoscode);
        //3.把数据库查询签名进行MD5加密
        String encrypt = MD5.encrypt(signKey);
        boolean equals = encrypt.equals(hospSign);
        //4.判断签名是否一致
        if (!hospSign.equals(encrypt)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        return Result.ok(hospital);
    }

}
