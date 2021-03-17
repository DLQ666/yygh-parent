package com.dlq.yygh.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dlq.yygh.model.user.UserInfo;
import com.dlq.yygh.vo.user.LoginVo;
import com.dlq.yygh.vo.user.UserAuthVo;
import com.dlq.yygh.vo.user.UserInfoQueryVo;

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

    /**
     * 用户认证接口
     */
    void userAuth(Long userId, UserAuthVo userAuthVo);

    /**
     * 用户列表 （条件分页查询）
     */
    IPage<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo);

    /**
     * 用户锁定
     */
    void lock(Long userId, Integer status);

    /**
     * 用户详情
     */
    Map<String, Object> show(Long userId);

    /**
     * 认证审批
     */
    void approval(Long userId, Integer authStatus);
}
