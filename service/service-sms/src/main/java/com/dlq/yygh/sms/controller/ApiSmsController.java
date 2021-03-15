package com.dlq.yygh.sms.controller;

import com.aliyuncs.exceptions.ClientException;
import com.dlq.yygh.common.result.Result;
import com.dlq.yygh.common.utils.FormUtils;
import com.dlq.yygh.common.utils.RandomUtils;
import com.dlq.yygh.sms.service.SmsService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @program: learn_parent
 * @description: 短信服务前端控制器
 * @author: Hasee
 * @create: 2020-07-04 15:16
 */
@Api(value = "短信管理")
@Slf4j
@RestController
@RequestMapping("/api/sms")
public class ApiSmsController {

    @Autowired
    private SmsService smsService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @GetMapping("/send/{phone}")
    public Result getCode(@PathVariable("phone") String phone) {
        //校验手机号是否合法
        if (StringUtils.isEmpty(phone) || !FormUtils.isMobile(phone)){
            log.info("手机号不正确");
            return Result.fail().message("手机号不正确").code(28001);
        }
        //从redis获取验证码，如果获取到，返回ok
        // key 手机号  value 验证码
        String code = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(code)){
            return Result.ok().message("短信已发送");
        }

        //获取不到，生成验证码，整合短信服务进行发送
        //生成验证码
        String checkCode = RandomUtils.getFourBitRandom();

        //发送验证码
        try {
            smsService.send(phone, checkCode);

            //存储验证码到redis
            redisTemplate.opsForValue().set(phone,checkCode,120, TimeUnit.MINUTES);
            return Result.ok().message("短信发送成功");
        } catch (Exception e) {
            log.error("发送短信异常："+e.getMessage());
            return Result.fail().message("短信发送失败");
        }
    }

}
