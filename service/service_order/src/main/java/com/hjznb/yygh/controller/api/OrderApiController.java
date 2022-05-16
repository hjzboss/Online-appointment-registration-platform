package com.hjznb.yygh.controller.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hjznb.yygh.common.result.Result;
import com.hjznb.yygh.common.utils.AuthContextHolder;
import com.hjznb.yygh.enums.OrderStatusEnum;
import com.hjznb.yygh.model.order.OrderInfo;
import com.hjznb.yygh.service.OrderService;
import com.hjznb.yygh.vo.order.OrderCountQueryVo;
import com.hjznb.yygh.vo.order.OrderQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/4/27 13:33
 */
@Api(tags = "订单接口")
@RestController
@RequestMapping("/api/order/orderInfo")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ApiOperation(value = "创建订单")
    @PostMapping("auth/submitOrder/{scheduleId}/{patientId}")
    public Result submitOrder(
            @ApiParam(name = "scheduleId", value = "排班id", required = true)
            @PathVariable String scheduleId,
            @ApiParam(name = "patientId", value = "就诊人id", required = true)
            @PathVariable Long patientId) {
        return Result.ok(orderService.saveOrder(scheduleId, patientId));
    }

    //订单列表（条件查询带分页）
    @ApiOperation(value = "订单列表（条件查询带分页）")
    @GetMapping("auth/{page}/{limit}")
    public Result list(@PathVariable Long page,
                       @PathVariable Long limit,
                       OrderQueryVo orderQueryVo, HttpServletRequest request) {
        //设置当前用户id
        orderQueryVo.setUserId(AuthContextHolder.getUserId(request));
        Page<OrderInfo> pageParam = new Page<>(page, limit);
        IPage<OrderInfo> pageModel =
                orderService.selectPage(pageParam, orderQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "获取订单状态")
    @GetMapping("auth/getStatusList")
    public Result getStatusList() {
        return Result.ok(OrderStatusEnum.getStatusList());
    }

    @ApiOperation(value = "根据订单id查询订单详情")
    @GetMapping("auth/getOrders/{orderId}")
    public Result getOrders(@PathVariable String orderId) {
        OrderInfo orderInfo = orderService.getOrder(orderId);
        return Result.ok(orderInfo);
    }

    @ApiOperation(value = "取消预约")
    @GetMapping("auth/cancelOrder/{orderId}")
    public Result cancelOrder(@PathVariable Long orderId) {
        Boolean isOrder = orderService.cancelOrder(orderId);
        return Result.ok(isOrder);
    }

    @ApiOperation(value = "获取订单统计数据")
    @PostMapping("inner/getCountMap")
    public Map<String, Object> getCountMap(@RequestBody OrderCountQueryVo orderCountQueryVo) {
        return orderService.getCountMap(orderCountQueryVo);
    }

    @ApiOperation(value = "修改订单评论状态")
    @PutMapping("inner/commentStatus/{outTradeNo}/{status}")
    public Result updateCommentOrder(@PathVariable Integer status, @PathVariable String outTradeNo) {
        orderService.updateCommentStatus(outTradeNo, status);
        return Result.ok();
    }
}
