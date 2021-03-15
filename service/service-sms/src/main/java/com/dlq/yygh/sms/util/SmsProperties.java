package com.dlq.yygh.sms.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @program: learn_parent
 * @description: 读取配置文件中阿里云账号信息工具类
 * @author: Hasee
 * @create: 2020-07-04 15:10
 */
@Data
@Component
//注意prefix要写到最后一个 "." 符号之前
@ConfigurationProperties(prefix="aliyun.sms")
public class SmsProperties {
    private String regionId;
    private String keyId;
    private String keySecret;
    private String templateCode;
    private String signName;
}
