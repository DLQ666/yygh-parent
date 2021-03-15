package com.dlq.yygh.sms.service.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.dlq.yygh.common.exception.YyghException;
import com.dlq.yygh.common.result.ResultCodeEnum;
import com.dlq.yygh.sms.service.SmsService;
import com.dlq.yygh.sms.util.SmsProperties;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: learn_parent
 * @description: 短信服务实现类
 * @author: Hasee
 * @create: 2020-07-04 15:18
 */
@Slf4j
@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    private SmsProperties smsProperties;

    @Override
    public void send(String mobile, String checkCode) {
        //创建配置对象
        DefaultProfile profile = DefaultProfile.getProfile(smsProperties.getRegionId(),
                smsProperties.getKeyId(), smsProperties.getKeySecret());

        //创建client对象
        IAcsClient client = new DefaultAcsClient(profile);

        //创建并组装参数
        CommonRequest request = new CommonRequest();
        //组装参数对象
        //request.setSysProtocol(ProtocolType.HTTPS);
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");

        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", smsProperties.getSignName());
        request.putQueryParameter("RegionId", smsProperties.getRegionId());
        request.putQueryParameter("TemplateCode", smsProperties.getTemplateCode());
        //验证码使用 json 格式
        Map<String, String> map = new HashMap<>();
        map.put("checkcode", checkCode);
        Gson gson = new Gson();
        String json = gson.toJson(map);
        request.putQueryParameter("TemplateParam", json);

        //发送短信
        CommonResponse response = null;
        try {

            response = client.getCommonResponse(request);

            //response.getHttpResponse().isSuccess()//注意：此处不能通过http的响应结果判断短信是否发送成功。如果发送失败的原因是欠费就判断不了
            //得到json字符串格式的返回结果
            String data = response.getData();
            //解析响应结果
            HashMap<String ,String> hashMap = gson.fromJson(data, HashMap.class);
            String code = hashMap.get("Code");
            String message = hashMap.get("Message");

            if ("isv.BUSINESS_LIMIT_CONTROL".equals(code)){
                log.error("发送短信过于频繁："+"code-"+code+"，message-"+message);
                throw new YyghException(ResultCodeEnum.SMS_SEND_ERROR_BUSINESS_LIMIT_CONTROL);
            }

            if (!"OK".equals(code)){
                log.error("短信发送失败："+"code-"+code+"，message-"+message);
                throw new YyghException(ResultCodeEnum.SMS_SEND_ERROR);
            }
        } catch (Exception e) {
            throw new YyghException(ResultCodeEnum.SMS_SEND_ERROR);
        }
    }
}
