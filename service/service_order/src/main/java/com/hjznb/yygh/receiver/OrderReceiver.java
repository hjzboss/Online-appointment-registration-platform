package com.hjznb.yygh.receiver;


import com.hjznb.yygh.common.rabbit.constant.MqConst;
import com.hjznb.yygh.service.OrderService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * 定时任务的监听器，监听来自service_task的消息
 */
@Component
@Slf4j
public class OrderReceiver {

    private final OrderService orderService;

    public OrderReceiver(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 提醒病人就诊
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_TASK_8, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_TASK),
            key = {MqConst.ROUTING_TASK_8}
    ))
    public void patientTips(Object test, Message message, Channel channel) throws IOException {
        log.info("---方法执行---");
        orderService.patientTips();
    }

}
