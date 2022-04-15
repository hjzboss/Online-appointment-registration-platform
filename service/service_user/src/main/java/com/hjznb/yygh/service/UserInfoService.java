package com.hjznb.yygh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hjznb.yygh.model.user.UserInfo;
import com.hjznb.yygh.vo.user.LoginVo;

import java.util.Map;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/4/10 11:21
 */
public interface UserInfoService extends IService<UserInfo> {
    //会员登录
    Map<String, Object> login(LoginVo loginVo);

    //会员注册
    void regist(LoginVo loginVo);

    UserInfo selectWxInfoOpenId(String openid);
}
