package com.dlq.yygh.sms.service;

import com.aliyuncs.exceptions.ClientException;

/**
 * @program: learn_parent
 * @description: 短信服务service层
 * @author: Hasee
 * @create: 2020-07-04 15:17
 */
public interface SmsService {
    void send(String mobile, String checkCode);
}
