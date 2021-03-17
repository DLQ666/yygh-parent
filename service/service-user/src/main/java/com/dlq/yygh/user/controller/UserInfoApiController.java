package com.dlq.yygh.user.controller;

import com.dlq.yygh.common.result.Result;
import com.dlq.yygh.common.utils.AuthContextHolder;
import com.dlq.yygh.model.user.UserInfo;
import com.dlq.yygh.user.service.UserInfoService;
import com.dlq.yygh.vo.user.LoginVo;
import com.dlq.yygh.vo.user.UserAuthVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *@program: yygh-parent
 *@description:
 *@author: Hasee
 *@create: 2021-03-15 17:12
 */
@RestController
@RequestMapping("/api/user")
public class UserInfoApiController {

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 用户手机号登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo){
        Map<String,Object> map = userInfoService.loginUser(loginVo);
        return Result.ok(map);
    }

    /**
     * 用户认证接口
     */
    @PostMapping("/auth/userAuth")
    public Result userAuth(@RequestBody UserAuthVo userAuthVo, HttpServletRequest request){
        //传递两个参数，第一个参数，用户id，第二个参数认证数据vo
        userInfoService.userAuth(AuthContextHolder.getUserId(request),userAuthVo);
        return Result.ok();
    }

    /**
     * 获取用户id信息接口
     */
    @GetMapping("/auth/getUserInfo")
    public Result<UserInfo> getUserInfo(HttpServletRequest request){
        Long userId = AuthContextHolder.getUserId(request);
        UserInfo userInfo = userInfoService.getById(userId);
        return Result.ok(userInfo);
    }
}
