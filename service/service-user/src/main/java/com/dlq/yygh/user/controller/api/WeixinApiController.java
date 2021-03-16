package com.dlq.yygh.user.controller.api;

import com.dlq.yygh.common.exception.YyghException;
import com.dlq.yygh.common.healper.JwtHelper;
import com.dlq.yygh.common.result.Result;
import com.dlq.yygh.common.result.ResultCodeEnum;
import com.dlq.yygh.model.user.UserInfo;
import com.dlq.yygh.user.service.UserInfoService;
import com.dlq.yygh.user.util.HttpClientUtils;
import com.dlq.yygh.user.util.UcenterProperties;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 *@program: yygh-parent
 *@description:
 *@author: Hasee
 *@create: 2021-03-16 13:03
 */
@Slf4j
@Controller//注意这里没有配置 @RestController
@RequestMapping("/api/ucenter/wx")
public class WeixinApiController {

    @Autowired
    private UcenterProperties ucenterProperties;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private UserInfoService userInfoService;

    /**
     * 1、生成微信扫描二维码
     * 返回生成二维码需要参数
     */
    @ResponseBody
    @GetMapping("/getLoginParam")
    public Result genQrConnect(){
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("appid",ucenterProperties.getAppId());
            map.put("scope", "snsapi_login");
            String redirectUri = ucenterProperties.getRedirectUri();
            redirectUri = URLEncoder.encode(redirectUri, "UTF-8");
            map.put("redirectUri",redirectUri);
            //生成随机state，防止csrf击攻
            String state = UUID.randomUUID().toString();
            map.put("state", state);
            //将state存入redis
            redisTemplate.opsForValue().set("wx_open_state", state, 120, TimeUnit.MINUTES);

            return Result.ok(map);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 微信扫码后进行回掉的方法
     */
    @GetMapping("/callback")
    public String callback(String code, String state) {
        log.info("callback被调用");
        log.info("code:" + code);
        log.info("state:" + state);
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(state)) {
            log.error("非法回调请求");
            throw new YyghException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        //从redis中中拿state
        String openState = redisTemplate.opsForValue().get("wx_open_state");
        if (!state.equals(openState)) {
            log.error("非法回调请求");
            throw new YyghException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        //https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
        //携带code临时票据，和appid以及appsecrent请求access_token和openid（微信唯一标识）
        String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
        //组装参数?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
        HashMap<String, String> accessTokenParam = new HashMap<>();
        accessTokenParam.put("appid", ucenterProperties.getAppId());
        accessTokenParam.put("secret", ucenterProperties.getAppSecret());
        accessTokenParam.put("code", code);
        accessTokenParam.put("grant_type", "authorization_code");
        HttpClientUtils client = new HttpClientUtils(accessTokenUrl, accessTokenParam);

        String result = "";
        try {
            //发送请求：组装完整的url字符串、发送请求
            client.get();
            //得到响应
            result = client.getContent();
            log.info("result = " + result);
        } catch (Exception e) {
            log.error("获取access_token失败");
            throw new YyghException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        Gson gson = new Gson();
        HashMap<String, Object> resultMap = gson.fromJson(result, HashMap.class);

        //失败的相应结果
        Object errcode = resultMap.get("errcode");
        if (errcode != null) {
            Double errorcode = (Double) errcode;
            String errormsg = (String) resultMap.get("errmsg");
            log.error("获取access_token失败" + "code: " + errorcode + ", message:" + errormsg);
            throw new YyghException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        //解析出结果中的access_token和openid
        String accessToken = (String) resultMap.get("access_token");
        String openid = (String) resultMap.get("openid");
        log.info("accessToken:" + accessToken);
        log.info("openid:" + openid);

        //去数据库中查此人是否扫码登录过
        UserInfo userInfo = userInfoService.getByOpenId(openid);
        if (userInfo == null){
            //如果当前用户不存在，则去微信的资源服务器获取用户个人信息（携带access_token）====并创建新用户添加到数据库
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo";
            //组装参数?access_token=ACCESS_TOKEN&openid=OPENID
            HashMap<String, String> baseUserInfoParam = new HashMap<>();
            baseUserInfoParam.put("access_token", accessToken);
            baseUserInfoParam.put("openid", openid);
            client = new HttpClientUtils(baseUserInfoUrl, baseUserInfoParam);

            String resultUserInfo = "";
            try {
                client.get();
                resultUserInfo = client.getContent();
            } catch (Exception e) {
                throw new YyghException(ResultCodeEnum.FETCH_USERINFO_ERROR);
            }
            HashMap<String, Object> resultUserInfoMap = gson.fromJson(resultUserInfo, HashMap.class);
            //失败的相应结果
            errcode = resultUserInfoMap.get("errcode");
            if (errcode != null) {
                Double errorcode = (Double) errcode;
                String errormsg = (String) resultMap.get("errmsg");
                log.error("获取用户信息失败" + "code: " + errorcode + ", message:" + errormsg);
                throw new YyghException(ResultCodeEnum.FETCH_USERINFO_ERROR);
            }

            //解析出结果中的用户个人信息
            String nickname = (String) resultUserInfoMap.get("nickname");
            String avatar = (String) resultUserInfoMap.get("headimgurl");
            Double sex = (Double) resultUserInfoMap.get("sex");

            //在本地数据库中插入当前微信用户的信息（使用微信账号在本地服务注册新用户）
            userInfo = new UserInfo();
            userInfo.setOpenid(openid);
            userInfo.setNickName(nickname);
            userInfo.setStatus(1);
            userInfoService.save(userInfo);
        }

        //如果当前用户已经存在，则直接使用当前用户的信息登录（生成jwt的过程）
        //member=>jwt
        //jwt生成 token字符串
        Map<String, String> map = new HashMap<>();
        String name = userInfo.getName();
        if (StringUtils.isEmpty(name)){
            name = userInfo.getNickName();
        }
        if (StringUtils.isEmpty(name)){
            name = userInfo.getPhone();
        }
        map.put("name", name);

        //判断userInfo是否有手机号，如果手机号为空，返回openid
        // 如果手机号不为空，返回openid值是空字符串
        //在前端判断：如果openid不为空，绑定手机号，如果openid为空，就不需要绑定手机号
        if (StringUtils.isEmpty(userInfo.getPhone())){
            map.put("openid", userInfo.getOpenid());
        }else {
            map.put("openid","");
        }
        String token = JwtHelper.createToken(userInfo.getId(), name);
        map.put("token", token);
        try {
            String encode = URLEncoder.encode(name, "UTF-8");
            return "redirect:"+ ucenterProperties.getBaseUri() + "/weixin/callback?token="+map.get("token")+"&openid="+ map.get("openid")+"&name="+ encode;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

    }
}
