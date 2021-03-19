package com.dlq.yygh.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dlq.yygh.model.order.PaymentInfo;
import com.dlq.yygh.model.order.RefundInfo;

/**
 *@description:
 *@author: Hasee
 *@create: 2021-03-19 19:38
 */
public interface RefundInfoService extends IService<RefundInfo> {

    /**
     * 保存退款记录
     */
    RefundInfo saveRefundInfo(PaymentInfo paymentInfo);
}
