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
        UserInfo userInfo = null;
        //微信登录绑定用户名
        if (!StringUtils.isEmpty(loginVo.getOpenid())) {
            userInfo = this.selectWxInfoOpenId(loginVo.getOpenid());
            if (null != userInfo) {
                userInfo.setUsername(loginVo.getUsername());
                userInfo.setPassword(MD5.encrypt(password));
                userInfo.setPhone(loginVo.getPhone());
                this.updateById(userInfo);
            } else {
                throw new YyghException(ResultCodeEnum.DATA_ERROR);
            }
        }
        // 如果为空，则是用户名直接登录
        if (userInfo == null) {
            //用户名已被使用
            QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", username);
            //获取会员，如果不存在则需要先注册
            userInfo = baseMapper.selectOne(queryWrapper);
            if (null == userInfo) {
                throw new YyghException(ResultCodeEnum.USERNAME_ERROR);
            }
            //校验密码
            if (!userInfo.getPassword().equals(MD5.encrypt(password))) {
                throw new YyghException(ResultCodeEnum.PASSWORD_ERROR);
            }
        }
        //校验是否被禁用
        if (userInfo.getStatus() == 0) {
            throw new YyghException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }
        //返回登录信息
        Map<String, Object> map = new HashMap<>();
        String name = userInfo.getNickName();
        if (StringUtils.isEmpty(name)) {
            name = username;
        }
        map.put("name", name);
        //jwt生成token字符串
        String token = JwtHelper.createToken(userInfo.getId(), name);
        map.put("token", token);
        return map;
    }

    @Override
    public void regist(LoginVo loginVo) {
        //获取用户名和密码
        String username = loginVo.getUsername();
        String password = loginVo.getPassword();
        String phone = loginVo.getPhone();
        Map<String, Object> map = new HashMap<>();
        //校验参数
        if (StringUtils.isEmpty(username) ||
                StringUtils.isEmpty(password) || StringUtils.isEmpty(phone)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        //用户名已被使用
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        //注册会员信息
        UserInfo userInfo = baseMapper.selectOne(queryWrapper);
        if (null == userInfo) {
            userInfo = new UserInfo();
            userInfo.setName("");
            userInfo.setPhone(phone);
            userInfo.setStatus(1);
            userInfo.setUsername(username);
            userInfo.setPassword(MD5.encrypt(password));
            this.save(userInfo);
        } else {
            throw new YyghException(ResultCodeEnum.USER_EXIST);
        }
    }

    /**
     * 根据微信openid获取用户信息
     *
     * @param openid 微信openid
     * @return
     */
    @Override
    public UserInfo selectWxInfoOpenId(String openid) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", openid);
        return baseMapper.selectOne(queryWrapper);
    }
}

