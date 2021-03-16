package com.dlq.yygh.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dlq.yygh.model.user.UserInfo;
import com.dlq.yygh.vo.user.LoginVo;

import java.util.Map;

/**
 *@description:
 *@author: Hasee
 *@create: 2021-03-15 17:12
 */
public interface UserInfoService extends IService<UserInfo> {
    /**
     * 用户手机号登录
     */
    Map<String, Object> loginUser(LoginVo loginVo);

    UserInfo getByOpenId(String openid);
}
