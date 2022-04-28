package com.hjznb.yygh.service;


import com.hjznb.yygh.vo.msm.MsmVo;

public interface MsmService {

    //发送手机验证码
    boolean send(String phone, String code);
    //rabbitmq发送预约成功短信
    boolean send(MsmVo msmVo);
}
