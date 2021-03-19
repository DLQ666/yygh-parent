package com.dlq.yygh.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dlq.yygh.common.exception.YyghException;
import com.dlq.yygh.common.result.ResultCodeEnum;
import com.dlq.yygh.enums.PaymentStatusEnum;
import com.dlq.yygh.enums.PaymentTypeEnum;
import com.dlq.yygh.enums.RefundStatusEnum;
import com.dlq.yygh.model.order.OrderInfo;
import com.dlq.yygh.model.order.PaymentInfo;
import com.dlq.yygh.model.order.RefundInfo;
import com.dlq.yygh.order.service.OrderService;
import com.dlq.yygh.order.service.PaymentService;
import com.dlq.yygh.order.service.RefundInfoService;
import com.dlq.yygh.order.service.WeixinService;
import com.dlq.yygh.order.utils.HttpClient;
import com.dlq.yygh.order.utils.WeixinPayProperties;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *@program: yygh-parent
 *@description:
 *@author: Hasee
 *@create: 2021-03-19 15:07
 */
@Slf4j
@Service
public class WeixinServiceImpl implements WeixinService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private WeixinPayProperties weixinPayProperties;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RefundInfoService refundInfoService;

    /**
     * 下单 生成二维码
     */
    @Override
    public Map createNative(Long orderId) {
        try {
            //从redis获取数据
            Map payMap = (Map) redisTemplate.opsForValue().get(orderId.toString());
            if (payMap != null) {
                return payMap;
            }

            //根据orderid获取订单信息
            OrderInfo order = orderService.getById(orderId);

            //向支付记录表中添加信息
            paymentService.savePaymentInfo(order, PaymentTypeEnum.WEIXIN.getStatus());

            //设置参数，调用微信生成二维码接口
            //把参数转换成xml格式，使用商户key进行加密
            //组装参数
            Map<String, String> params = new HashMap<>();
            //公众账号ID
            params.put("appid", weixinPayProperties.getAppId());
            //商户号
            params.put("mch_id", weixinPayProperties.getPartner());
            //随机字符串 长度要求在32位以内。推荐随机数生成算法
            params.put("nonce_str", WXPayUtil.generateNonceStr());

            String body = order.getReserveDate() + "就诊" + order.getDepname();
            //商品描述
            params.put("body", body);
            //订单号
            params.put("out_trade_no", order.getOutTradeNo());
            //标价金额(单位：分)
            params.put("total_fee", order.getAmount().multiply(new BigDecimal("100")).longValue() + "");
            //终端ip
            params.put("spbill_create_ip", "127.0.0.1");
            //通知地址(回调地址)
            params.put("notify_url", weixinPayProperties.getNotifyUrl());
            //交易类型
            params.put("trade_type", "NATIVE");

            //4调用微信生成二维码接口, httpclient调用
            HttpClient client = new HttpClient(weixinPayProperties.getUnifiedorderUrl());
            //设置map参数
            String xmlParams = WXPayUtil.generateSignedXml(params, weixinPayProperties.getPartnerKey());
            //将参数放入请求对象方法体
            client.setXmlParam(xmlParams);
            //使用https形式发送
            client.setHttps(true);
            //使用post方式发送请求
            client.post();

            //得到响应
            String resultXml = client.getContent();
            log.info("\n resultXml: \n " + resultXml);
            //转换map集合
            Map<String, String> resultMap = WXPayUtil.xmlToMap(resultXml);
            log.info("\n resultMap: \n " + resultMap);

            //错误处理
            if ("FAIL".equals(resultMap.get("return_code")) || "FAIL".equals(resultMap.get("result_code"))) {
                log.error("微信支付统一下单错误 - "
                        + "return_code: " + resultMap.get("return_code")
                        + "return_msg: " + resultMap.get("return_msg")
                        + "result_code: " + resultMap.get("result_code")
                        + "err_code: " + resultMap.get("err_code")
                        + "err_code_des: " + resultMap.get("err_code_des"));

                throw new YyghException(ResultCodeEnum.FAIL);
            }

            //封装返回结果集
            Map<String, Object> map = new HashMap<>();
            map.put("orderId", orderId);
            map.put("totalFee", order.getAmount());
            map.put("resultCode", resultMap.get("result_code"));
            //二维码url
            map.put("codeUrl", resultMap.get("code_url"));

            if (null != resultMap.get("result_code")) {
                //微信支付二维码2小时过期，可采取2小时未支付取消订单
                redisTemplate.opsForValue().set(orderId.toString(), map, 120, TimeUnit.MINUTES);
            }

            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 调用微信接口实现支付状态查询
     */
    @Override
    public Map<String, String> queryPayStatus(Long orderId) {
        try {
            // 1、根据订单id获取订单信息
            OrderInfo orderInfo = orderService.getById(orderId);

            //2、封装提交参数
            Map paramMap = new HashMap<>();
            paramMap.put("appid", weixinPayProperties.getAppId());
            paramMap.put("mch_id", weixinPayProperties.getPartner());
            paramMap.put("out_trade_no", orderInfo.getOutTradeNo());
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());

            //3、设置请求内容
            HttpClient client = new HttpClient(weixinPayProperties.getOrderqueryUrl());
            client.setXmlParam(WXPayUtil.generateSignedXml(paramMap, weixinPayProperties.getPartnerKey()));
            client.setHttps(true);
            client.post();

            //4、得到微信接口返回数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            System.out.println("resultMap=>支付状态："+resultMap);
            //5、把接口数据返回
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     * 微信退款
     */
    @Override
    public Boolean refund(Long orderId) {
        try {
            //获取支付记录信息
            PaymentInfo paymentInfo = paymentService.getPaymentInfo(orderId, PaymentTypeEnum.WEIXIN.getStatus());
            //添加信息到退款记录
            RefundInfo refundInfo = refundInfoService.saveRefundInfo(paymentInfo);
            //判断当前订单数据是否已经退款
            if(refundInfo.getRefundStatus().intValue() == RefundStatusEnum.REFUND.getStatus().intValue()) {
                return true;
            }
            //调用微信接口实现退款
            //封装需要参数
            Map<String,String> paramMap = new HashMap<>();
            paramMap.put("appid",weixinPayProperties.getAppId()); //公众账号ID
            paramMap.put("mch_id",weixinPayProperties.getPartner()); //商户编号
            paramMap.put("nonce_str",WXPayUtil.generateNonceStr());
            paramMap.put("transaction_id",paymentInfo.getTradeNo()); //微信订单号
            paramMap.put("out_trade_no",paymentInfo.getOutTradeNo()); //商户订单编号
            paramMap.put("out_refund_no","tk"+paymentInfo.getOutTradeNo()); //商户退款单号
            paramMap.put("total_fee",paymentInfo.getTotalAmount().multiply(new BigDecimal("100")).longValue()+"");
            paramMap.put("refund_fee",paymentInfo.getTotalAmount().multiply(new BigDecimal("100")).longValue()+"");
            //paramMap.put("total_fee","1");
            //paramMap.put("refund_fee","1");
            String paramXml = WXPayUtil.generateSignedXml(paramMap,weixinPayProperties.getPartnerKey());

            //设置调用接口内容
            HttpClient client = new HttpClient(weixinPayProperties.getRefund(),weixinPayProperties);
            client.setXmlParam(paramXml);
            client.setHttps(true);
            //设置证书信息
            client.setCert(true);
            client.setCertPassword(weixinPayProperties.getCert());
            client.post();

            //3、接收到 返回第三方的数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            if (null != resultMap && WXPayConstants.SUCCESS.equalsIgnoreCase(resultMap.get("result_code"))) {
                refundInfo.setCallbackTime(new Date());
                refundInfo.setTradeNo(resultMap.get("refund_id"));
                refundInfo.setRefundStatus(RefundStatusEnum.REFUND.getStatus());
                refundInfo.setCallbackContent(JSONObject.toJSONString(resultMap));
                refundInfoService.updateById(refundInfo);

                return true;
            }

            return false;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
