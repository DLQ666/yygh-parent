package com.dlq.yygh.common.utils;

import com.dlq.yygh.common.healper.JwtHelper;

import javax.servlet.http.HttpServletRequest;

/**
 *@program: yygh-parent
 *@description: 获取当前用户信息工具类
 *@author: Hasee
 *@create: 2021-03-16 23:35
 */
public class AuthContextHolder {

    //获取当前用户id
    public static Long getUserId(HttpServletRequest request){
        //从header中获取token
        String token = request.getHeader("token");
        //jwt 从token中获取userid
        Long userId = JwtHelper.getUserId(token);
        return userId;
    }

    //获取当前用户名称
    public static String getUserName(HttpServletRequest request){
        //从header中获取token
        String token = request.getHeader("token");
        //jwt 从token中获取userid
        String userName = JwtHelper.getUserName(token);
        return userName;
    }
}
