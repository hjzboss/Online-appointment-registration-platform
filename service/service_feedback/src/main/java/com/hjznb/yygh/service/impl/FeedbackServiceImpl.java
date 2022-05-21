package com.hjznb.yygh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjznb.yygh.common.exception.YyghException;
import com.hjznb.yygh.common.result.ResultCodeEnum;
import com.hjznb.yygh.mapper.FeedbackMapper;
import com.hjznb.yygh.model.hosp.HospitalComment;
import com.hjznb.yygh.model.user.Feedback;
import com.hjznb.yygh.service.FeedbackService;
import com.hjznb.yygh.vo.feedback.FeedbackQueryVo;
import com.hjznb.yygh.vo.feedback.FeedbackVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/5/21 10:51
 */
@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements FeedbackService {

    /**
     * 保存反馈
     *
     * @param feedbackVo 保存对象
     */
    @Override
    public void saveFeedback(FeedbackVo feedbackVo) {
        Long userId = feedbackVo.getUserId();
        Feedback feedback = new Feedback();
        BeanUtils.copyProperties(feedbackVo, feedback);
        System.out.println(feedback);
        //每个用户每天只可反馈一次
        QueryWrapper<Feedback> wrapper = new QueryWrapper<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(formatter.format(new Date()));
        wrapper.like("create_time", formatter.format(new Date()));
        wrapper.eq("user_id", userId);
        List<Feedback> list = baseMapper.selectList(wrapper);
        System.out.println(list);
        if (list == null || list.size() != 0) {
            throw new YyghException(ResultCodeEnum.HAVE_FEEDBACK_TODAY);
        }
        this.save(feedback);
    }

    /**
     * 条件分页查询
     *
     * @param pageParam       分页对象
     * @param feedbackQueryVo 查询对象
     * @return 分页结果
     */
    @Override
    public IPage<Feedback> selectPage(Page<Feedback> pageParam, FeedbackQueryVo feedbackQueryVo) {
        Long userId = feedbackQueryVo.getUserId();
        String createTimeBegin = feedbackQueryVo.getCreateTimeBegin();
        String createTimeEnd = feedbackQueryVo.getCreateTimeEnd();
        QueryWrapper<Feedback> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(userId)) {
            wrapper.eq("user_id", userId);
        }
        if (!StringUtils.isEmpty(createTimeBegin)) {
            wrapper.ge("create_time", createTimeBegin);
        }
        if (!StringUtils.isEmpty(createTimeEnd)) {
            wrapper.le("create_time", createTimeEnd);
        }
        return baseMapper.selectPage(pageParam, wrapper);
    }
}
