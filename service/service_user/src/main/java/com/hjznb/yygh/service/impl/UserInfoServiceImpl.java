package com.hjznb.yygh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjznb.yygh.common.exception.YyghException;
import com.hjznb.yygh.common.helper.JwtHelper;
import com.hjznb.yygh.common.result.ResultCodeEnum;
import com.hjznb.yygh.common.utils.MD5;
import com.hjznb.yygh.mapper.UserInfoMapper;
import com.hjznb.yygh.model.user.UserInfo;
import com.hjznb.yygh.service.UserInfoService;
import com.hjznb.yygh.vo.user.LoginVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        //获取用户名和密码
        String username = loginVo.getUsername();
        String password = loginVo.getPassword();
        //校验参数
        if (StringUtils.isEmpty(username) ||
                StringUtils.isEmpty(password)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        //用户名已被使用
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        //获取会员，如果不存在则需要先注册
        UserInfo userInfo = baseMapper.selectOne(queryWrapper);
        if (null == userInfo) {
            throw new YyghException(ResultCodeEnum.USERNAME_ERROR);
        }
        //校验密码
        if(!userInfo.getPassword().equals(MD5.encrypt(password))) {
            throw new YyghException(ResultCodeEnum.PASSWORD_ERROR);
        }
        //校验是否被禁用
        if (userInfo.getStatus() == 0) {
            throw new YyghException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }
        //返回登录信息
        Map<String, Object> map = new HashMap<>();
        String name = userInfo.getName();
        if (StringUtils.isEmpty(name)) {
            name = username;
        }
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getUsername();
        }
        map.put("name", name);
        //jwt生成token字符串
        String token = JwtHelper.createToken(userInfo.getId(), name);
        map.put("token", token);
        return map;

    }

    // TODO: 用户注册
    @Override
    public Map<String, Object> regist(LoginVo loginVo) {

        return null;
    }
}

