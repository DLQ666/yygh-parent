package com.dlq.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dlq.yygh.common.exception.YyghException;
import com.dlq.yygh.common.healper.JwtHelper;
import com.dlq.yygh.common.result.ResultCodeEnum;
import com.dlq.yygh.model.user.UserInfo;
import com.dlq.yygh.user.mapper.UserInfoMapper;
import com.dlq.yygh.user.service.UserInfoService;
import com.dlq.yygh.vo.user.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 *@program: yygh-parent
 *@description:
 *@author: Hasee
 *@create: 2021-03-15 17:13
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 用户手机号登录
     */
    @Override
    public Map<String, Object> loginUser(LoginVo loginVo) {
        //从 loginVo 中获取输入的手机号，验证码
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();

        //判断手机号和验证码是否为空
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        // 判断手机验证码和输入的验证码是否一致
        String redisCode = redisTemplate.opsForValue().get(phone);
        if (!code.equals(redisCode)){
            throw new YyghException(ResultCodeEnum.CODE_ERROR);
        }

        //绑定手机号码
        UserInfo userInfo = null;
        if(!StringUtils.isEmpty(loginVo.getOpenid())) {
            userInfo = this.getByOpenId(loginVo.getOpenid());
            if(null != userInfo) {
                userInfo.setPhone(loginVo.getPhone());
                this.updateById(userInfo);
            } else {
                throw new YyghException(ResultCodeEnum.DATA_ERROR);
            }
        }
        if (userInfo == null){
            //判断是否是第一次登录 ： 根据手机号查询数据库，如果不存在相同手机号就是第一次登陆
            QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
            wrapper.eq("phone", phone);
            userInfo = baseMapper.selectOne(wrapper);
            //userInfo 为空说明用户没注册过  第一次登录
            if (userInfo == null){
                //添加信息到数据库
                userInfo = new UserInfo();
                userInfo.setName("");
                userInfo.setPhone(phone);
                userInfo.setStatus(1);
                baseMapper.insert(userInfo);
            }
        }



        //不是第一次登录，直接进行登录
        // 校验账户是否被禁用
        if (userInfo.getStatus() == 0){
            throw new YyghException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }
        //返回登录信息
        //返回用户信息
        //返回token信息
        //返回页面显示名称
        Map<String, Object> map = new HashMap<>();
        String name = userInfo.getName();
        if(StringUtils.isEmpty(name)) {
            name = userInfo.getNickName();
        }
        if(StringUtils.isEmpty(name)) {
            name = userInfo.getPhone();
        }
        map.put("name", name);

        //jwt生成 token字符串
        String token = JwtHelper.createToken(userInfo.getId(), name);
        map.put("token", token);
        return map;
    }

    @Override
    public UserInfo getByOpenId(String openid) {
        if (openid == null){
            return null;
        }
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", openid);
        UserInfo userInfo = baseMapper.selectOne(wrapper);
        return userInfo;
    }
}
