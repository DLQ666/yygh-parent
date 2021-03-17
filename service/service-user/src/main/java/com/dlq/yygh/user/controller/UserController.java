package com.dlq.yygh.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dlq.yygh.common.result.Result;
import com.dlq.yygh.model.user.Patient;
import com.dlq.yygh.model.user.UserInfo;
import com.dlq.yygh.user.service.UserInfoService;
import com.dlq.yygh.vo.user.UserInfoQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 *@program: yygh-parent
 *@description:
 *@author: Hasee
 *@create: 2021-03-17 14:39
 */
@RestController
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 用户列表 （条件分页查询）
     */
    @GetMapping("/{page}/{limit}")
    public Result list(@PathVariable("page") long page,
                       @PathVariable("limit") long limit,
                       UserInfoQueryVo userInfoQueryVo) {
        if (page <= 0) {
            page = 1;
        }
        if (limit <= 0) {
            limit = 10;
        }
        Page<UserInfo> pageParam = new Page<>(page, limit);
        IPage<UserInfo> pageModel = userInfoService.selectPage(pageParam, userInfoQueryVo);
        return Result.ok(pageModel);
    }

    /**
     * 用户锁定
     */
    @GetMapping("/lock/{userId}/{status}")
    public Result lockUser(@PathVariable("userId") Long userId, @PathVariable("status") Integer status) {
        userInfoService.lock(userId, status);
        return Result.ok();
    }

    /**
     * 用户详情
     */
    @GetMapping("/show/{userId}")
    public Result show(@PathVariable("userId")Long userId){
        Map<String,Object> map = userInfoService.show(userId);
        return Result.ok(map);
    }
}
