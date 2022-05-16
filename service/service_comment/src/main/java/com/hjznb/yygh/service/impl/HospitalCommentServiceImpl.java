package com.hjznb.yygh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjznb.yygh.enums.CommentStatusEnum;
import com.hjznb.yygh.enums.OrderCommentStatusEnum;
import com.hjznb.yygh.mapper.HospitalCommentMapper;
import com.hjznb.yygh.model.hosp.Hospital;
import com.hjznb.yygh.model.hosp.HospitalComment;
import com.hjznb.yygh.model.order.OrderInfo;
import com.hjznb.yygh.order.client.OrderFeignClient;
import com.hjznb.yygh.service.HospitalCommentService;
import com.hjznb.yygh.vo.hosp.HospitalCommentVo;
import com.hjznb.yygh.vo.order.OrderCountQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/5/16 17:01
 */
@Service
public class HospitalCommentServiceImpl extends ServiceImpl<HospitalCommentMapper, HospitalComment> implements HospitalCommentService {

    private final OrderFeignClient orderFeignClient;

    public HospitalCommentServiceImpl(OrderFeignClient orderFeignClient) {
        this.orderFeignClient = orderFeignClient;
    }

    //上传评论
    @Override
    public void saveComment(HospitalCommentVo hospitalCommentVo) {
        String outTradeNo = hospitalCommentVo.getOutTradeNo();
        HospitalComment hospitalComment = new HospitalComment();
        BeanUtils.copyProperties(hospitalCommentVo, hospitalComment);
        hospitalComment.setCreateTime(new Date());
        hospitalComment.setStatus(CommentStatusEnum.COMMENT_WAIT.getStatus());
        orderFeignClient.updateCommentOrder(OrderCommentStatusEnum.COMMENT_AUTH.getStatus(), outTradeNo);
        this.save(hospitalComment);
    }

    //根据订单id获取评论
    @Override
    public HospitalCommentVo getHospitalCommentByOrderId(String outTradeNo) {
        //查询评论
        QueryWrapper<HospitalComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("out_trade_no", outTradeNo);
        HospitalComment hospitalComment = baseMapper.selectOne(queryWrapper);
        HospitalCommentVo result = null;
        if (hospitalComment != null) {
            result = new HospitalCommentVo();
            BeanUtils.copyProperties(hospitalComment, result);
        }
        return result;
    }

    @Override
    public IPage<HospitalComment> selectPage(Page<HospitalComment> pageParam, HospitalCommentVo hospitalCommentVo) {
        //orderQueryVo获取条件值
        String hosname = hospitalCommentVo.getHosname(); //医院名称
        Date reserveDate = hospitalCommentVo.getReserveDate();//安排时间
        String depname = hospitalCommentVo.getDepname(); //科室名称
        String outTradeNo = hospitalCommentVo.getOutTradeNo(); //订单编号
        Integer status = hospitalCommentVo.getStatus();
        //对条件值进行非空判断
        QueryWrapper<HospitalComment> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(hosname)) {
            wrapper.like("hosname", hosname);
        }
        if (!StringUtils.isEmpty(depname)) {
            wrapper.like("depname", depname);
        }
        if (!StringUtils.isEmpty(outTradeNo)) {
            wrapper.like("out_trade_no", outTradeNo);
        }
        if (!StringUtils.isEmpty(status)) {
            wrapper.eq("status", status);
        }
        if (!StringUtils.isEmpty(reserveDate)) {
            wrapper.ge("reserve_date", reserveDate);
        }
        //调用mapper的方法
        return baseMapper.selectPage(pageParam, wrapper);
    }

}
