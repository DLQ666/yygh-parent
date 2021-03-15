package com.dlq.yygh.user.service;

import com.dlq.yygh.vo.user.LoginVo;

import java.util.Map;

/**
 *@description:
 *@author: Hasee
 *@create: 2021-03-15 17:12
 */
public interface UserInfoService {
    /**
     * 用户手机号登录
     */
    Map<String, Object> loginUser(LoginVo loginVo);
}
