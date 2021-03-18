package com.dlq.yygh.hosp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dlq.yygh.model.hosp.Schedule;
import com.dlq.yygh.vo.hosp.ScheduleOrderVo;
import com.dlq.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 *@description:
 *@author: Hasee
 *@create: 2021-03-13 12:21
 */
public interface ScheduleMapper extends BaseMapper<Schedule> {
}
