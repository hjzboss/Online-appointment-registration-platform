package com.hjznb.yygh.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hjznb.yygh.model.hosp.Hospital;
import com.hjznb.yygh.model.hosp.HospitalComment;
import com.hjznb.yygh.model.hosp.HospitalSet;
import com.hjznb.yygh.vo.hosp.CommentQueryVo;
import com.hjznb.yygh.vo.hosp.HospitalCommentVo;

import java.util.Map;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/5/16 16:51
 */
public interface HospitalCommentService extends IService<HospitalComment> {

    //上传评论
    void saveComment(HospitalCommentVo hospitalCommentVo);

    //根据订单号获取评论
    HospitalCommentVo getHospitalCommentByOrderId(String outTradeNo);

    //条件查询分页列表
    IPage<HospitalComment> selectPage(Page<HospitalComment> pageParam, CommentQueryVo commentQueryVo);

    //根据id修改评论状态
    void updateStatusById(Integer id, Integer status);

    //获取医院评论并计算平均分
    Map<String, Object> selectPageAndStar(String hoscode, Long limit);

    //根据id获取评论
    HospitalComment getCommentById(Integer id);
}
