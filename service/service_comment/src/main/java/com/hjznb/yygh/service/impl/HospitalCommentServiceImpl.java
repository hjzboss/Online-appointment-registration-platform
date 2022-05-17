package com.hjznb.yygh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjznb.yygh.common.exception.YyghException;
import com.hjznb.yygh.common.result.ResultCodeEnum;
import com.hjznb.yygh.enums.CommentStatusEnum;
import com.hjznb.yygh.enums.OrderCommentStatusEnum;
import com.hjznb.yygh.mapper.HospitalCommentMapper;
import com.hjznb.yygh.model.hosp.HospitalComment;
import com.hjznb.yygh.order.client.OrderFeignClient;
import com.hjznb.yygh.service.HospitalCommentService;
import com.hjznb.yygh.vo.hosp.CommentQueryVo;
import com.hjznb.yygh.vo.hosp.HospitalCommentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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

    /**
     * 上传评论
     *
     * @param hospitalCommentVo 用户端上传的评论
     */
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

    /**
     * 根据订单号获取评论
     *
     * @param outTradeNo 订单号
     * @return 查询到的评论对象
     */
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


    /**
     * 条件分页查询
     *
     * @param pageParam      分页对象
     * @param commentQueryVo 条件查询对象
     * @return 分页评论数据
     */
    @Override
    public IPage<HospitalComment> selectPage(Page<HospitalComment> pageParam, CommentQueryVo commentQueryVo) {
        //orderQueryVo获取条件值
        String hosname = commentQueryVo.getHosname(); //医院名称
        Date reserveDate = commentQueryVo.getReserveDate();//安排时间
        String depname = commentQueryVo.getDepname(); //科室名称
        String outTradeNo = commentQueryVo.getOutTradeNo(); //订单编号
        Integer status = commentQueryVo.getStatus(); //评论状态
        String title = commentQueryVo.getTitle(); //医生职称
        String createTimeBegin = commentQueryVo.getCreateTimeBegin(); //创建时间开始
        String createTimeEnd = commentQueryVo.getCreateTimeEnd(); //创建时间结束
        //对条件值进行非空判断
        QueryWrapper<HospitalComment> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(hosname)) {
            wrapper.like("hosname", hosname);
        }
        if (!StringUtils.isEmpty(title)) {
            wrapper.like("title", title);
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
        if (!StringUtils.isEmpty(createTimeBegin)) {
            wrapper.ge("create_time", createTimeBegin);
        }
        if (!StringUtils.isEmpty(createTimeEnd)) {
            wrapper.le("create_time", createTimeEnd);
        }
        //调用mapper的方法
        IPage<HospitalComment> hospitalCommentPage = baseMapper.selectPage(pageParam, wrapper);
        List<HospitalComment> records = hospitalCommentPage.getRecords();
        records.forEach(this::packageComment);
        hospitalCommentPage.setRecords(records);
        return hospitalCommentPage;
    }


    /**
     * 封装审核数据
     *
     * @param hospitalComment 待封装的评论
     */
    private void packageComment(HospitalComment hospitalComment) {
        Integer status = hospitalComment.getStatus();
        if (status.equals(CommentStatusEnum.COMMENT_WAIT.getStatus())) {
            hospitalComment.getParam().put("commentStatus", "待审核");
        }
        if (status.equals(CommentStatusEnum.COMMENT_PASS.getStatus())) {
            hospitalComment.getParam().put("commentStatus", "通过");
        }
        if (status.equals(CommentStatusEnum.COMMENT_FAIL.getStatus())) {
            hospitalComment.getParam().put("commentStatus", "未通过");
        }
    }

    /**
     * 根据id修改评论状态
     *
     * @param id     评论id
     * @param status 要修改的状态
     */
    @Override
    public void updateStatusById(Integer id, Integer status) {
        //根据id获取评论并修改评论状态
        HospitalComment hospitalComment = baseMapper.selectById(id);
        hospitalComment.setStatus(status);
        String outTradeNo = hospitalComment.getOutTradeNo();
        //修改订单表的状态
        if (status.equals(CommentStatusEnum.COMMENT_PASS.getStatus())) {
            orderFeignClient.updateCommentOrder(OrderCommentStatusEnum.COMMENT_FINISH.getStatus(), outTradeNo);
        } else if (status.equals(CommentStatusEnum.COMMENT_FAIL.getStatus())) {
            orderFeignClient.updateCommentOrder(OrderCommentStatusEnum.NO_COMMENT.getStatus(), outTradeNo);
        } else {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        baseMapper.updateById(hospitalComment);
    }

    /**
     * 根据医院名字获取评论分页数据并计算平均分
     *
     * @param hoscode 医院名字
     * @param limit   要获取的数据量
     * @return 评论列表和平均分的封装对象
     */
    @Override
    public Map<String, Object> selectPageAndStar(String hoscode, Long limit) {
        QueryWrapper<HospitalComment> wrapper = new QueryWrapper<>();
        wrapper.eq("hoscode", hoscode);
        wrapper.orderByDesc("id"); //根据id逆序排列
        Map<String, Object> result = new HashMap<>(); //返回的结果
        List<HospitalComment> records; //返回的列表数据
        Double average; //平均分
        Page<HospitalComment> hospitalCommentPage = new Page<>(1, limit);
        //查询数据库中医院所有评论
        List<HospitalComment> all = baseMapper.selectList(wrapper);
        //如果数据库中医院评论数小于limit，则直接返回总数;否则返回limit分页数
        if (all.size() <= limit) {
            records = all;
        } else {
            IPage<HospitalComment> page = baseMapper.selectPage(hospitalCommentPage, wrapper);
            records = page.getRecords();
        }
        //计算平均分
        average = averageStar(records);
        result.put("list", records);
        result.put("average", average);
        return result;
    }

    //根据id获取评论并包装
    @Override
    public HospitalComment getCommentById(Integer id) {
        HospitalComment byId = this.getById(id);
        this.packageComment(byId);
        return byId;
    }

    /**
     * 获取平均分
     *
     * @param hospitalCommentList 所需计算平均分的列表
     * @return 平均分
     */
    private Double averageStar(List<HospitalComment> hospitalCommentList) {
        double average = 0;
        double size = hospitalCommentList.size();
        for (HospitalComment item : hospitalCommentList) {
            average += item.getStar();
        }
        return (average / size);
    }
}
