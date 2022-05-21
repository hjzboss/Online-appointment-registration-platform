package com.hjznb.yygh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjznb.yygh.common.exception.YyghException;
import com.hjznb.yygh.common.helper.JwtHelper;
import com.hjznb.yygh.common.result.ResultCodeEnum;
import com.hjznb.yygh.enums.AuthStatusEnum;
import com.hjznb.yygh.mapper.UserInfoMapper;
import com.hjznb.yygh.model.user.Patient;
import com.hjznb.yygh.model.user.UserInfo;
import com.hjznb.yygh.service.PatientService;
import com.hjznb.yygh.service.UserInfoService;
import com.hjznb.yygh.vo.user.LoginVo;
import com.hjznb.yygh.vo.user.ModifyVo;
import com.hjznb.yygh.vo.user.UserAuthVo;
import com.hjznb.yygh.vo.user.UserInfoQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserInfoServiceImpl extends
        ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    private final RedisTemplate<String, String> redisTemplate;

    private final PatientService patientService;

    public UserInfoServiceImpl(RedisTemplate<String, String> redisTemplate, PatientService patientService) {
        this.redisTemplate = redisTemplate;
        this.patientService = patientService;
    }

    //用户手机号登录接口
    @Override
    public Map<String, Object> loginUser(LoginVo loginVo) {

        //从loginVo获取输入的手机号，和验证码
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();

        //判断手机号和验证码是否为空
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        //判断手机验证码和输入的验证码是否一致
        String redisCode = redisTemplate.opsForValue().get(phone);
        if (!code.equals(redisCode)) {
            throw new YyghException(ResultCodeEnum.CODE_ERROR);
        }

        //如果是微信扫码登录，则Openid有值，则绑定手机号码，执行后userInfo就!=null了不会走68行的手机号登录
        UserInfo userInfo = null;
        UserInfo userInfoOld = null;
        String openid = loginVo.getOpenid();
        if (!StringUtils.isEmpty(openid)) {
            userInfo = this.selectWxInfoOpenId(openid);
            if (null != userInfo) {
                QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
                wrapper.eq("phone", phone);
                userInfoOld = baseMapper.selectOne(wrapper);
                // 如果手机已注册，直接将openid和nickname赋值给旧数据
                if (null != userInfoOld) {
                    userInfoOld.setOpenid(openid);
                    userInfoOld.setNickName(userInfo.getNickName());
                    this.updateById(userInfoOld);
                    this.removeById(userInfo.getId());
                    userInfo = userInfoOld;
                } else {
                    userInfo.setPhone(loginVo.getPhone());
                    this.updateById(userInfo);
                }
            } else {
                throw new YyghException(ResultCodeEnum.DATA_ERROR);
            }
        }
        //如果userinfo为空，进行正常手机登录
        if (userInfo == null) {
            //判断是否第一次登录：根据手机号查询数据库，如果不存在相同手机号就是第一次登录
            QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
            wrapper.eq("phone", phone);
            userInfo = baseMapper.selectOne(wrapper);
            //如果userInfo不为null，则不执行if里面，直接去93行执行代码
            if (userInfo == null) { //第一次使用这个手机号登录
                //添加信息到数据库
                userInfo = new UserInfo();
                userInfo.setName("");
                userInfo.setPhone(phone);
                userInfo.setStatus(1);
                baseMapper.insert(userInfo);
            }
        }

        //校验是否被禁用
        if (userInfo.getStatus() == 0) {
            throw new YyghException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }

        //不是第一次登录，直接登录。假设用户是userInfo == null手机号登录的，并且已经注册过不用走73行的代码了，直接走下面的即可。
        //返回登录信息
        //返回登录用户名
        //返回token信息，token信息是用来返回给前台的，执行操作时判断用户是否登录状态，可以设置过期时间用session一样
        Map<String, Object> map = new HashMap<>();
        String name = userInfo.getName();
        //如果这个用户登录后没有去设置真实姓名，则name为空，那我们就设置该用户它在前端显示的名字为昵称
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getNickName();
        }
        //如果这个用户登录后也没有设置昵称，则name还是空，那我们就设置该用户它在前端显示的名字为它的手机号
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getPhone();
        }
        map.put("name", name);

        //jwt生成token字符串
        String token = JwtHelper.createToken(userInfo.getId(), name);
        map.put("token", token);
        return map;
    }

    @Override
    public UserInfo selectWxInfoOpenId(String openid) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", openid);
        return baseMapper.selectOne(queryWrapper);
    }

    //用户认证
    @Override
    public void userAuth(Long userId, UserAuthVo userAuthVo) {
        //根据用户id查询用户信息
        UserInfo userInfo = baseMapper.selectById(userId);
        //设置认证信息
        //认证人姓名
        userInfo.setName(userAuthVo.getName());
        //其他认证信息
        userInfo.setCertificatesType(userAuthVo.getCertificatesType());
        userInfo.setCertificatesNo(userAuthVo.getCertificatesNo());
        userInfo.setCertificatesUrl(userAuthVo.getCertificatesUrl());
        userInfo.setAuthStatus(AuthStatusEnum.AUTH_RUN.getStatus());
        //进行信息更新
        baseMapper.updateById(userInfo);
    }

    //用户列表（条件查询带分页）
    @Override
    public IPage<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo) {
        //UserInfoQueryVo获取条件值
        String name = userInfoQueryVo.getKeyword(); //用户名称
        Integer status = userInfoQueryVo.getStatus();//用户状态
        Integer authStatus = userInfoQueryVo.getAuthStatus(); //认证状态
        String createTimeBegin = userInfoQueryVo.getCreateTimeBegin(); //开始时间
        String createTimeEnd = userInfoQueryVo.getCreateTimeEnd(); //结束时间
        //对条件值进行非空判断
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            wrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(status)) {
            wrapper.eq("status", status);
        }
        if (!StringUtils.isEmpty(authStatus)) {
            wrapper.eq("auth_status", authStatus);
        }
        if (!StringUtils.isEmpty(createTimeBegin)) {
            wrapper.ge("create_time", createTimeBegin);
        }
        if (!StringUtils.isEmpty(createTimeEnd)) {
            wrapper.le("create_time", createTimeEnd);
        }
        //调用mapper的方法
        IPage<UserInfo> pages = baseMapper.selectPage(pageParam, wrapper);
        //编号变成对应值封装
        pages.getRecords().forEach(this::packageUserInfo);
        return pages;
    }

    //用户锁定
    @Override
    public void lock(Long userId, Integer status) {
        if (status == 0 || status == 1) {
            UserInfo userInfo = baseMapper.selectById(userId);
            userInfo.setStatus(status);
            baseMapper.updateById(userInfo);
        }
    }

    //用户详情
    @Override
    public Map<String, Object> show(Long userId) {
        Map<String, Object> map = new HashMap<>();
        //根据userid查询用户信息
        UserInfo userInfo = this.packageUserInfo(baseMapper.selectById(userId));
        map.put("userInfo", userInfo);
        //根据userid查询就诊人信息
        List<Patient> patientList = patientService.findAllUserId(userId);
        map.put("patientList", patientList);
        return map;
    }

    //认证审批  2通过  -1不通过
    @Override
    public void approval(Long userId, Integer authStatus) {
        if (authStatus == 2 || authStatus == -1) {
            UserInfo userInfo = baseMapper.selectById(userId);
            userInfo.setAuthStatus(authStatus);
            baseMapper.updateById(userInfo);
        }
    }

    //用户信息修改
    @Override
    public void modifyUser(ModifyVo modifyVo) {
        Long id = modifyVo.getId();
        String code = modifyVo.getCode();
        String phone = modifyVo.getPhone();
        System.out.println("{code: " + code + "\t" + "phone: " + phone + "}");
        //判断手机号和验证码是否为空
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        //判断手机验证码和输入的验证码是否一致
        String redisCode = redisTemplate.opsForValue().get(phone);
        if (!code.equals(redisCode)) {
            throw new YyghException(ResultCodeEnum.CODE_ERROR);
        }

        //判断手机号是否已使用
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.like("phone", phone);
        UserInfo isExist = baseMapper.selectOne(wrapper);
        System.out.println(isExist);
        if (isExist != null && !isExist.getId().equals(id)) {
            throw new YyghException(ResultCodeEnum.PHONE_EXIST);
        }

        log.info("----------开始修改----------");
        //按用户id查找信息后修改手机号，然后更新
        UserInfo userInfo = baseMapper.selectById(id);
        userInfo.setPhone(phone);
        baseMapper.updateById(userInfo);
        log.info("----------修改完成----------");
    }

    //编号变成对应值封装
    private UserInfo packageUserInfo(UserInfo userInfo) {
        //处理认证状态编码
        userInfo.getParam().put("authStatusString", AuthStatusEnum.getStatusNameByStatus(userInfo.getAuthStatus()));
        //处理用户状态 0  1
        String statusString = userInfo.getStatus() == 0 ? "锁定" : "正常";
        userInfo.getParam().put("statusString", statusString);
        return userInfo;
    }
}
