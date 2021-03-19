package com.dlq.yygh.order.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @program: learn_parent
 * @description: 读取配置类中微信支付配置信息工具类
 * @author: Hasee
 * @create: 2020-07-07 14:59
 */
@Data
@Component
@ConfigurationProperties(prefix="weixin.pay")
public class WeixinPayProperties {
    private String appId;
    private String partner;
    private String partnerKey;
    private String notifyUrl;
    private String orderqueryUrl;
    private String unifiedorderUrl;

}
