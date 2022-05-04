package com.hjznb.yygh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hjznb.yygh.model.order.OrderInfo;
import com.hjznb.yygh.vo.order.OrderCountQueryVo;
import com.hjznb.yygh.vo.order.OrderCountVo;
import feign.Param;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/4/27 13:30
 */
@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {
    //查询预约统计数据的方法
    List<OrderCountVo> selectOrderCount(@Param("vo") OrderCountQueryVo orderCountQueryVo);
}
