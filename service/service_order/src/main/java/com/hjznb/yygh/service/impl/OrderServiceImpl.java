package com.hjznb.yygh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjznb.yygh.common.exception.YyghException;
import com.hjznb.yygh.common.helper.HttpRequestHelper;
import com.hjznb.yygh.common.rabbit.constant.MqConst;
import com.hjznb.yygh.common.rabbit.service.RabbitService;
import com.hjznb.yygh.common.result.ResultCodeEnum;
import com.hjznb.yygh.enums.MsmTypeEnum;
import com.hjznb.yygh.enums.OrderStatusEnum;
import com.hjznb.yygh.hosp.client.HospitalFeignClient;
import com.hjznb.yygh.mapper.OrderInfoMapper;
import com.hjznb.yygh.model.order.OrderInfo;
import com.hjznb.yygh.model.user.Patient;
import com.hjznb.yygh.service.OrderService;
import com.hjznb.yygh.service.WeixinService;
import com.hjznb.yygh.user.client.PatientFeignClient;
import com.hjznb.yygh.vo.hosp.ScheduleOrderVo;
import com.hjznb.yygh.vo.msm.MsmVo;
import com.hjznb.yygh.vo.order.*;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/4/27 13:31
 */
@Service
public class OrderServiceImpl extends
        ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderService {
    private final PatientFeignClient patientFeignClient;
    private final HospitalFeignClient hospitalFeignClient;
    private final RabbitService rabbitService;
    @Autowired
    private WeixinService weixinService;

    public OrderServiceImpl(PatientFeignClient patientFeignClient, HospitalFeignClient hospitalFeignClient, RabbitService rabbitService) {
        this.patientFeignClient = patientFeignClient;
        this.hospitalFeignClient = hospitalFeignClient;
        this.rabbitService = rabbitService;
    }

    //????????????
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long saveOrder(String scheduleId, Long patientId) {
        //???????????????
        Patient patient = patientFeignClient.getPatientOrder(patientId);
        if (null == patient) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        //??????????????????
        ScheduleOrderVo scheduleOrderVo = hospitalFeignClient.getScheduleOrderVo(scheduleId);
        if (null == scheduleOrderVo) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        //todo:???????????????????????????
        if (new DateTime(scheduleOrderVo.getStartTime()).isAfterNow()
                || new DateTime(scheduleOrderVo.getEndTime()).isBeforeNow()) {
            throw new YyghException(ResultCodeEnum.TIME_NO);
        }
        //?????????????????????url
        SignInfoVo signInfoVo = hospitalFeignClient.getSignInfoVo(scheduleOrderVo.getHoscode());
        if (null == scheduleOrderVo) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        if (scheduleOrderVo.getAvailableNumber() <= 0) {
            throw new YyghException(ResultCodeEnum.NUMBER_NO);
        }
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(scheduleOrderVo, orderInfo);
        // ??????????????????????????????
        String outTradeNo = System.currentTimeMillis() + "" + new Random().nextInt(100);
        orderInfo.setOutTradeNo(outTradeNo);
        orderInfo.setUserId(patient.getUserId());
        orderInfo.setPatientId(patientId);
        orderInfo.setPatientName(patient.getName());
        orderInfo.setPatientPhone(patient.getPhone());
        orderInfo.setOrderStatus(OrderStatusEnum.UNPAID.getStatus());
        this.save(orderInfo);
        //?????????????????????????????????????????????url?????????????????????????????????
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("hoscode", orderInfo.getHoscode());
        paramMap.put("depcode", orderInfo.getDepcode());
        paramMap.put("hosScheduleId", orderInfo.getHosScheduleId());
        paramMap.put("reserveDate", new DateTime(orderInfo.getReserveDate()).toString("yyyy-MM-dd"));
        paramMap.put("reserveTime", orderInfo.getReserveTime());
        paramMap.put("amount", orderInfo.getAmount());
        paramMap.put("name", patient.getName());
        paramMap.put("certificatesType", patient.getCertificatesType());
        paramMap.put("certificatesNo", patient.getCertificatesNo());
        paramMap.put("sex", patient.getSex());
        paramMap.put("birthdate", patient.getBirthdate());
        paramMap.put("phone", patient.getPhone());
        paramMap.put("isMarry", patient.getIsMarry());
        paramMap.put("provinceCode", patient.getProvinceCode());
        paramMap.put("cityCode", patient.getCityCode());
        paramMap.put("districtCode", patient.getDistrictCode());
        paramMap.put("address", patient.getAddress());
        //?????????
        paramMap.put("contactsName", patient.getContactsName());
        paramMap.put("contactsCertificatesType", patient.getContactsCertificatesType());
        paramMap.put("contactsCertificatesNo", patient.getContactsCertificatesNo());
        paramMap.put("contactsPhone", patient.getContactsPhone());
        paramMap.put("timestamp", HttpRequestHelper.getTimestamp());
        String sign = HttpRequestHelper.getSign(paramMap, signInfoVo.getSignKey());
        paramMap.put("sign", sign);
        JSONObject result = HttpRequestHelper.sendRequest(paramMap, signInfoVo.getApiUrl() + "/order/submitOrder");
        System.out.println(result);
        //????????????????????????????????????????????????????????????
        if (result.getInteger("code") == 200) {
            JSONObject jsonObject = result.getJSONObject("data");
            //??????????????????????????????????????????????????????

            String hosRecordId = jsonObject.getString("hosRecordId");
            //????????????
            Integer number = jsonObject.getInteger("number");
            //????????????
            String fetchTime = jsonObject.getString("fetchTime");
            //????????????
            String fetchAddress = jsonObject.getString("fetchAddress");
            //????????????
            orderInfo.setHosRecordId(hosRecordId);
            orderInfo.setNumber(number);
            orderInfo.setFetchTime(fetchTime);
            orderInfo.setFetchAddress(fetchAddress);
            baseMapper.updateById(orderInfo);

            //??????????????????
            Integer reservedNumber = jsonObject.getInteger("reservedNumber");
            //?????????????????????
            Integer availableNumber = jsonObject.getInteger("availableNumber");

            //??????mq?????????????????????????????????
            //??????mq???????????????????????????hosp????????????????????????????????????
            OrderMqVo orderMqVo = new OrderMqVo();
            orderMqVo.setScheduleId(scheduleId);
            orderMqVo.setReservedNumber(reservedNumber);
            orderMqVo.setAvailableNumber(availableNumber);
            //?????????????????????hosp??????????????????????????????????????????????????????????????????????????????
            MsmVo msmVo = new MsmVo();
            msmVo.setPhone(orderInfo.getPatientPhone());
            msmVo.setType(MsmTypeEnum.MSM_ORDER);
            String reserveDate = new DateTime(orderInfo.getReserveDate()).toString("yyyy-MM-dd") + (orderInfo.getReserveTime() == 0 ? "??????" : "??????");
            Map<String, Object> param = new HashMap<String, Object>() {{
                put("hosname", orderInfo.getHosname());
                put("fetchTime", fetchTime);
                put("fetchAddress", fetchAddress);
                put("reserveDate", reserveDate);
                put("title", orderInfo.getHosname() + "|" + orderInfo.getDepname() + "|" + orderInfo.getTitle());
                put("name", patient.getName());
            }};
            msmVo.setParam(param);

            orderMqVo.setMsmVo(msmVo);
            rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_ORDER, MqConst.ROUTING_ORDER, orderMqVo);

        } else {
            throw new YyghException(result.getString("message"), ResultCodeEnum.FAIL.getCode());
        }
        return orderInfo.getId();
    }

    //???????????????????????????????????????
    @Override
    public IPage<OrderInfo> selectPage(Page<OrderInfo> pageParam, OrderQueryVo orderQueryVo) {
        //orderQueryVo???????????????
        String hosname = orderQueryVo.getHosname(); //????????????
        String patientName = orderQueryVo.getPatientName(); //???????????????
        String orderStatus = orderQueryVo.getOrderStatus(); //????????????
        String reserveDate = orderQueryVo.getReserveDate();//????????????
        String outTradeNo = orderQueryVo.getOutTradeNo();//?????????
        Long userId = orderQueryVo.getUserId();//??????id
        String createTimeBegin = orderQueryVo.getCreateTimeBegin();
        String createTimeEnd = orderQueryVo.getCreateTimeEnd();
        //??????????????????????????????
        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(userId)) {
            wrapper.eq("user_id", userId);
        }
        if (!StringUtils.isEmpty(hosname)) {
            wrapper.like("hosname", hosname);
        }
        if (!StringUtils.isEmpty(patientName)) {
            wrapper.like("patient_name", patientName);
        }
        if (!StringUtils.isEmpty(outTradeNo)) {
            wrapper.like("out_trade_no", outTradeNo);
        }
        if (!StringUtils.isEmpty(orderStatus)) {
            wrapper.eq("order_status", orderStatus);
        }
        if (!StringUtils.isEmpty(reserveDate)) {
            wrapper.ge("reserve_date", reserveDate);
        }
        if (!StringUtils.isEmpty(createTimeBegin)) {
            wrapper.ge("create_time", createTimeBegin);
        }
        if (!StringUtils.isEmpty(createTimeEnd)) {
            wrapper.le("create_time", createTimeEnd);
        }
        //??????mapper?????????
        IPage<OrderInfo> pages = baseMapper.selectPage(pageParam, wrapper);
        //???????????????????????????
        pages.getRecords().forEach(this::packOrderInfo);
        return pages;
    }

    //????????????id??????????????????
    @Override
    public OrderInfo getOrder(String orderId) {
        OrderInfo orderInfo = baseMapper.selectById(orderId);
        this.packOrderInfo(orderInfo);
        return orderInfo;
    }

    @Override
    public Map<String, Object> show(Long orderId) {
        Map<String, Object> map = new HashMap<>();
        OrderInfo orderInfo = this.getById(orderId);
        this.packOrderInfo(orderInfo);
        map.put("orderInfo", orderInfo);
        Patient patient
                = patientFeignClient.getPatientOrder(orderInfo.getPatientId());
        map.put("patient", patient);
        return map;
    }

    //????????????
    @Override
    public void patientTips() {
        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        //???????????????????????????????????????????????????????????????????????????
        wrapper.eq("reserve_date", new DateTime().toString("yyyy-MM-dd"));
        wrapper.ne("order_status", OrderStatusEnum.CANCLE.getStatus());
        List<OrderInfo> orderInfoList = baseMapper.selectList(wrapper);
        for (OrderInfo orderInfo : orderInfoList) {
            //????????????
            MsmVo msmVo = new MsmVo();
            msmVo.setType(MsmTypeEnum.MSM_PATIENT);
            msmVo.setPhone(orderInfo.getPatientPhone());
            String reserveDate = new DateTime(orderInfo.getReserveDate()).toString("yyyy-MM-dd") + (orderInfo.getReserveTime() == 0 ? "??????" : "??????");
            Map<String, Object> param = new HashMap<String, Object>() {{
                put("title", orderInfo.getHosname() + "|" + orderInfo.getDepname() + "|" + orderInfo.getTitle());
                put("reserveDate", reserveDate);
                put("name", orderInfo.getPatientName());
            }};
            msmVo.setParam(param);
            rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_MSM, MqConst.ROUTING_MSM_ITEM, msmVo);
        }
    }

    //????????????
    @Override
    public Boolean cancelOrder(Long orderId) {
        //??????????????????
        OrderInfo orderInfo = baseMapper.selectById(orderId);
        //??????????????????
        DateTime quitTime = new DateTime(orderInfo.getQuitTime());
        //????????????????????????
        if (quitTime.isBeforeNow()) {
            throw new YyghException(ResultCodeEnum.CANCEL_ORDER_NO);
        }
        //????????????????????????????????????
        SignInfoVo signInfoVo = hospitalFeignClient.getSignInfoVo(orderInfo.getHoscode());
        if (null == signInfoVo) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("hoscode", orderInfo.getHoscode());
        reqMap.put("hosRecordId", orderInfo.getHosRecordId());
        reqMap.put("timestamp", HttpRequestHelper.getTimestamp());
        String sign = HttpRequestHelper.getSign(reqMap, signInfoVo.getSignKey());
        reqMap.put("sign", sign);

        JSONObject result = HttpRequestHelper.sendRequest(reqMap,
                signInfoVo.getApiUrl() + "/order/updateCancelStatus");
        System.out.println("????????????");
        //??????????????????????????????
        if (result.getInteger("code") != 200) {
            throw new YyghException(result.getString("message"), ResultCodeEnum.FAIL.getCode());
        } else {
            System.out.println("??????????????????");
            //????????????????????????????????????
            if ((orderInfo.getOrderStatus().intValue() == OrderStatusEnum.PAID.getStatus().intValue()) || (orderInfo.getOrderStatus().intValue() == OrderStatusEnum.UNPAID.getStatus().intValue())) {
                if (orderInfo.getOrderStatus().intValue() == OrderStatusEnum.PAID.getStatus().intValue()) {
                    Boolean isRefund = weixinService.refund(orderId);
                    if (!isRefund) {
                        throw new YyghException(ResultCodeEnum.CANCEL_ORDER_FAIL);
                    }
                }
                //??????????????????
                orderInfo.setOrderStatus(OrderStatusEnum.CANCLE.getStatus());
                baseMapper.updateById(orderInfo);
                System.out.println("????????????");
                //??????mq?????????????????????????????????
                OrderMqVo orderMqVo = new OrderMqVo();
                orderMqVo.setScheduleId(orderInfo.getHosScheduleId());
                MsmVo msmVo = new MsmVo();
                msmVo.setPhone(orderInfo.getPatientPhone());
                msmVo.setType(MsmTypeEnum.MSM_CANCEL);
                String reserveDate = new DateTime(orderInfo.getReserveDate()).toString("yyyy-MM-dd") + (orderInfo.getReserveTime() == 0 ? "??????" : "??????");
                Map<String, Object> param = new HashMap<String, Object>() {{
                    put("date", reserveDate);
                    put("hosname", orderInfo.getHosname());
                    put("name", orderInfo.getPatientName());
                }};
                msmVo.setParam(param);
                orderMqVo.setMsmVo(msmVo);
                rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_ORDER, MqConst.ROUTING_ORDER, orderMqVo);
            }
        }
        return true;
    }

    @Override
    public Map<String, Object> getCountMap(OrderCountQueryVo orderCountQueryVo) {
        Map<String, Object> map = new HashMap<>();

        List<OrderCountVo> orderCountVoList
                = baseMapper.selectOrderCount(orderCountQueryVo);
        //????????????
        List<String> dateList
                = orderCountVoList.stream().map(OrderCountVo::getReserveDate).collect(Collectors.toList());
        //????????????
        List<Integer> countList
                = orderCountVoList.stream().map(OrderCountVo::getCount).collect(Collectors.toList());
        map.put("dateList", dateList);
        map.put("countList", countList);
        return map;
    }

    //??????????????????
    @Override
    public void updateCommentStatus(Long orderId, Integer status) {
        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("id", orderId);
        OrderInfo orderInfo = baseMapper.selectOne(wrapper);
        orderInfo.setCommentStatus(status);
        this.updateById(orderInfo);
    }


    private void packOrderInfo(OrderInfo orderInfo) {
        orderInfo.getParam().put("orderStatusString", OrderStatusEnum.getStatusNameByStatus(orderInfo.getOrderStatus()));
    }


}
