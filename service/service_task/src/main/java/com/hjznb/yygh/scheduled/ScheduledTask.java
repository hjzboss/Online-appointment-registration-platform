package com.hjznb.yygh.scheduled;

import com.hjznb.yygh.common.rabbit.constant.MqConst;
import com.hjznb.yygh.common.rabbit.service.RabbitService;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务，发送给order模块，提醒病人就诊
 */
@Component
@EnableScheduling
public class ScheduledTask {

    private final RabbitService rabbitService;

    public ScheduledTask(RabbitService rabbitService) {
        this.rabbitService = rabbitService;
    }

    /**
     * 每天8点执行方法，就医提醒
     */
    //cron表达式，设置执行间隔
    //0/30 * * * * ?  每30秒执行一次,用于测试
    @Scheduled(cron = "0 0 8 * * ?")
    public void taskPatient() {
        rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_TASK, MqConst.ROUTING_TASK_8, " ");
    }
}
