package com.dlq.yygh.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dlq.yygh.model.order.OrderInfo;
import com.dlq.yygh.vo.order.OrderCountQueryVo;
import com.dlq.yygh.vo.order.OrderCountVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *@description:
 *@author: Hasee
 *@create: 2021-03-17 20:14
 */
public interface OrderMapper extends BaseMapper<OrderInfo> {

    //查询预约统计数据的方法
    List<OrderCountVo> selectOrderCount(@Param("vo") OrderCountQueryVo orderCountQueryVo);
}
