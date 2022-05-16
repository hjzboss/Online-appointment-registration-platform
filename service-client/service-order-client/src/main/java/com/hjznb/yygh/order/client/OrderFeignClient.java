package com.hjznb.yygh.order.client;

import com.hjznb.yygh.common.result.Result;
import com.hjznb.yygh.vo.order.OrderCountQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/5/6 13:33
 */
@FeignClient(value = "service-order")
@Repository
public interface OrderFeignClient {
    /**
     * 获取订单统计数据
     */
    @PostMapping("/api/order/orderInfo/inner/getCountMap")
    Map<String, Object> getCountMap(@RequestBody OrderCountQueryVo orderCountQueryVo);

    //提交评论
    @PutMapping("/api/order/orderInfo/inner/commentStatus/{outTradeNo}/{status}")
    Result updateCommentOrder(@PathVariable Integer status, @PathVariable String outTradeNo);
}
