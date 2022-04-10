package com.hjznb.yygh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjznb.yygh.common.exception.YyghException;
import com.hjznb.yygh.common.helper.JwtHelper;
import com.hjznb.yygh.common.result.ResultCodeEnum;
import com.hjznb.yygh.mapper.UserInfoMapper;
import com.hjznb.yygh.model.user.UserInfo;
import com.hjznb.yygh.service.UserInfoService;
import com.hjznb.yygh.vo.user.LoginVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/4/10 11:23
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
        implements UserInfoService {
    //会员登录
    @Override
    public Map<String, Object> login(LoginVo loginVo) {
        //获取手机号和验证码
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();
        //校验参数
        if (StringUtils.isEmpty(phone) ||
                StringUtils.isEmpty(code)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        //TODO 校验校验验证码

        //手机号已被使用
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        //获取会员，如果第一次登录则创建信息
        UserInfo userInfo = baseMapper.selectOne(queryWrapper);
        if (null == userInfo) {
            userInfo = new UserInfo();
            userInfo.setName("");
            userInfo.setPhone(phone);
            userInfo.setStatus(1);
            this.save(userInfo);
        }
        //校验是否被禁用
        if (userInfo.getStatus() == 0) {
            throw new YyghException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }

        //TODO 记录登录

        //返回登录信息
        Map<String, Object> map = new HashMap<>();
        String name = userInfo.getName();
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getNickName();
        }
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getPhone();
        }
        map.put("name", name);
        //jwt生成token字符串
        String token = JwtHelper.createToken(userInfo.getId(), name);
        map.put("token", token);
        return map;

    }
}

