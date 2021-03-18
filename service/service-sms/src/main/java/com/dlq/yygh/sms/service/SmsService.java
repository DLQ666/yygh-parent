package com.dlq.yygh.sms.service;

import com.aliyuncs.exceptions.ClientException;
import com.dlq.yygh.vo.msm.MsmVo;

/**
 * @program: learn_parent
 * @description: 短信服务service层
 * @author: Hasee
 * @create: 2020-07-04 15:17
 */
public interface SmsService {

    //发送手机验证码
    void send(String mobile, String checkCode);

    //mq使用发送短信
    boolean send(MsmVo msmVo);
}
