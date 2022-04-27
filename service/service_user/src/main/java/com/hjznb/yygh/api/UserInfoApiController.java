package com.hjznb.yygh.api;

import com.hjznb.yygh.common.result.Result;
import com.hjznb.yygh.common.utils.AuthContextHolder;
import com.hjznb.yygh.model.user.UserInfo;
import com.hjznb.yygh.service.UserInfoService;
import com.hjznb.yygh.vo.user.LoginVo;
import com.hjznb.yygh.vo.user.UserAuthVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Api(tags = "用户管理（前台）")
@RestController
@RequestMapping("/api/user")
public class UserInfoApiController {

    private final UserInfoService userInfoService;

    public UserInfoApiController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    //用户手机号登录接口
    @ApiOperation(value = "用户手机号登录接口")
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo) {
        Map<String, Object> info = userInfoService.loginUser(loginVo);
        return Result.ok(info);
    }

    //用户认证接口
    @ApiOperation(value = "用户认证接口")
    @PostMapping("auth/userAuth")
    public Result userAuth(@RequestBody UserAuthVo userAuthVo, HttpServletRequest request) {
        //传递两个参数，第一个参数用户id，第二个参数认证数据vo对象
        userInfoService.userAuth(AuthContextHolder.getUserId(request), userAuthVo);
        return Result.ok();
    }

    //获取用户id信息接口
    @ApiOperation(value = "获取用户id信息接口")
    @GetMapping("auth/getUserInfo")
    public Result getUserInfo(HttpServletRequest request) {
        Long userId = AuthContextHolder.getUserId(request);
        // System.out.println(userId);
        UserInfo userInfo = userInfoService.getById(userId);
        return Result.ok(userInfo);
    }
}
