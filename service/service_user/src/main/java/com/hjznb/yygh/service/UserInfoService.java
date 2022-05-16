package com.hjznb.yygh.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hjznb.yygh.model.user.UserInfo;
import com.hjznb.yygh.vo.user.LoginVo;
import com.hjznb.yygh.vo.user.ModifyVo;
import com.hjznb.yygh.vo.user.UserAuthVo;
import com.hjznb.yygh.vo.user.UserInfoQueryVo;

import java.util.Map;

public interface UserInfoService extends IService<UserInfo> {
    //用户手机号登录接口
    Map<String, Object> loginUser(LoginVo loginVo);

    //根据openid判断
    UserInfo selectWxInfoOpenId(String openid);

    //用户认证
    void userAuth(Long userId, UserAuthVo userAuthVo);

    //用户列表（条件查询带分页）
    IPage<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo);

    //用户锁定
    void lock(Long userId, Integer status);

    //用户详情
    Map<String, Object> show(Long userId);

    //认证审批
    void approval(Long userId, Integer authStatus);

    //用户信息修改
    void modifyUser(ModifyVo modifyVo);
}
