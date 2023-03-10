package com.hjznb.hospital.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hjznb.hospital.mapper.OrderInfoMapper;
import com.hjznb.hospital.mapper.ScheduleMapper;
import com.hjznb.hospital.model.OrderInfo;
import com.hjznb.hospital.model.Patient;
import com.hjznb.hospital.model.Schedule;
import com.hjznb.hospital.service.HospitalService;
import com.hjznb.hospital.util.ResultCodeEnum;
import com.hjznb.hospital.util.YyghException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class HospitalServiceImpl implements HospitalService {

    private final ScheduleMapper hospitalMapper;

    private final OrderInfoMapper orderInfoMapper;

    public HospitalServiceImpl(ScheduleMapper hospitalMapper, OrderInfoMapper orderInfoMapper) {
        this.hospitalMapper = hospitalMapper;
        this.orderInfoMapper = orderInfoMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> submitOrder(Map<String, Object> paramMap) {
        log.info(JSONObject.toJSONString(paramMap));
        String hoscode = (String) paramMap.get("hoscode");
        String depcode = (String) paramMap.get("depcode");
        String hosScheduleId = (String) paramMap.get("hosScheduleId");
        String reserveDate = (String) paramMap.get("reserveDate");
        String reserveTime = (String) paramMap.get("reserveTime");
        String amount = (String) paramMap.get("amount");
        Schedule schedule = this.getSchedule(hosScheduleId);
        if (null == schedule) {
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }

        if (!schedule.getHoscode().equals(hoscode)
                || !schedule.getDepcode().equals(depcode)
                || !schedule.getAmount().equals(amount)) {
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }

        //???????????????
        Patient patient = JSONObject.parseObject(JSONObject.toJSONString(paramMap), Patient.class);
        log.info(JSONObject.toJSONString(patient));
        //?????????????????????
        Long patientId = this.savePatient(patient);

        Map<String, Object> resultMap = new HashMap<>();
        //int availableNumber = schedule.getAvailableNumber() - 1;
        int availableNumber = schedule.getAvailableNumber();
        if (availableNumber > 0) {
            availableNumber--;
            schedule.setAvailableNumber(availableNumber);
            hospitalMapper.updateById(schedule);

            //??????????????????
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setPatientId(patientId);
            orderInfo.setScheduleId(hosScheduleId);
            int number = schedule.getReservedNumber() - schedule.getAvailableNumber();
            orderInfo.setNumber(number);
            orderInfo.setAmount(new BigDecimal(amount));
            String fetchTime = "0".equals(reserveDate) ? " 09:30???" : " 14:00???";
            orderInfo.setFetchTime(reserveTime + fetchTime);
            orderInfo.setFetchAddress("??????9?????????");
            //?????? ?????????
            orderInfo.setOrderStatus(0);
            orderInfoMapper.insert(orderInfo);

            resultMap.put("resultCode", "0000");
            resultMap.put("resultMsg", "????????????");
            //??????????????????????????????????????????????????????
            resultMap.put("hosRecordId", orderInfo.getId());
            //????????????
            resultMap.put("number", number);
            //????????????
            resultMap.put("fetchTime", reserveDate + " 09:00???");
            //????????????
            resultMap.put("fetchAddress", "??????114??????");
            //??????????????????
            resultMap.put("reservedNumber", schedule.getReservedNumber());
            //?????????????????????
            resultMap.put("availableNumber", schedule.getAvailableNumber());
        } else {
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
        System.out.println(resultMap);
        return resultMap;
    }

    @Override
    public void updatePayStatus(Map<String, Object> paramMap) {
        String hoscode = (String) paramMap.get("hoscode");
        String hosRecordId = (String) paramMap.get("hosRecordId");

        OrderInfo orderInfo = orderInfoMapper.selectById(hosRecordId);
        if (null == orderInfo) {
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
        //?????????
        orderInfo.setOrderStatus(1);
        orderInfo.setPayTime(new Date());
        orderInfoMapper.updateById(orderInfo);
    }

    @Override
    public void updateCancelStatus(Map<String, Object> paramMap) {
        String hoscode = (String) paramMap.get("hoscode");
        String hosRecordId = (String) paramMap.get("hosRecordId");

        OrderInfo orderInfo = orderInfoMapper.selectById(hosRecordId);
        if (null == orderInfo) {
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
        //?????????
        orderInfo.setOrderStatus(-1);
        orderInfo.setQuitTime(new Date());
        orderInfoMapper.updateById(orderInfo);
    }

    private Schedule getSchedule(String frontSchId) {
        return hospitalMapper.selectById(frontSchId);
    }

    /**
     * ???????????????????????????
     *
     * @param patient
     */
    private Long savePatient(Patient patient) {
        // ????????????
        return 1L;
    }


}
