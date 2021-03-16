package com.dlq.yygh.user.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @program: learn_parent
 * @description: 读取微信开发者账号配置工具类
 * @author: Hasee
 * @create: 2020-07-05 16:56
 */
@Data
@Component
//注意prefix要写到最后一个 "." 符号之前
@ConfigurationProperties(prefix="wx.open")
public class UcenterProperties {
    private String appId;
    private String appSecret;
    private String redirectUri;
}
