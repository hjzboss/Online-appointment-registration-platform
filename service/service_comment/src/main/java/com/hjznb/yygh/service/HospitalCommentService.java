package com.hjznb.yygh.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hjznb.yygh.model.hosp.Hospital;
import com.hjznb.yygh.model.hosp.HospitalComment;
import com.hjznb.yygh.model.hosp.HospitalSet;
import com.hjznb.yygh.vo.hosp.HospitalCommentVo;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/5/16 16:51
 */
public interface HospitalCommentService extends IService<HospitalComment> {

    //上传评论
    void saveComment(HospitalCommentVo hospitalCommentVo);

    //根据订单id获取评论
    HospitalCommentVo getHospitalCommentByOrderId(String outTradeNo);

    //条件查询分页列表
    IPage<HospitalComment> selectPage(Page<HospitalComment> pageParam, HospitalCommentVo hospitalCommentVo);
}
