package com.hjznb.yygh.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hjznb.yygh.model.order.OrderInfo;
import com.hjznb.yygh.vo.order.OrderQueryVo;

import java.util.Map;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/4/27 13:31
 */
public interface OrderService extends IService<OrderInfo> {
    //保存订单
    Long saveOrder(String scheduleId, Long patientId);

    //订单列表，条件查询带分页
    IPage<OrderInfo> selectPage(Page<OrderInfo> pageParam, OrderQueryVo orderQueryVo);

    //根据订单id查询订单详情
    OrderInfo getOrder(String orderId);

    //订单详情
    Map<String,Object> show(Long orderId);

    //就诊通知
    void patientTips();
}

