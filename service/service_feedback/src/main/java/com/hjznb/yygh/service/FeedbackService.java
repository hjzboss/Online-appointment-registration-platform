package com.hjznb.yygh.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hjznb.yygh.model.hosp.HospitalComment;
import com.hjznb.yygh.model.user.Feedback;
import com.hjznb.yygh.vo.feedback.FeedbackQueryVo;
import com.hjznb.yygh.vo.feedback.FeedbackVo;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/5/21 10:51
 */
public interface FeedbackService extends IService<Feedback> {
    //保存反馈
    void saveFeedback(FeedbackVo feedbackVo);

    //分页查询
    IPage<Feedback> selectPage(Page<Feedback> pageParam, FeedbackQueryVo feedbackQueryVo);
}
