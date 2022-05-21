package com.hjznb.yygh.controller.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hjznb.yygh.common.result.Result;
import com.hjznb.yygh.common.utils.AuthContextHolder;
import com.hjznb.yygh.model.user.Feedback;
import com.hjznb.yygh.service.FeedbackService;
import com.hjznb.yygh.vo.feedback.FeedbackQueryVo;
import com.hjznb.yygh.vo.feedback.FeedbackVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/5/21 10:47
 */
@Api(tags = "供用户端调用反馈的接口")
@RestController
@RequestMapping("/api/feedback")
public class FeedbackApiController {

    private final FeedbackService feedbackService;


    public FeedbackApiController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @ApiOperation(value = "提交反馈")
    @PostMapping("postFeedback")
    public Result postFeedback(
            @RequestBody FeedbackVo feedbackVo, HttpServletRequest request) {
        feedbackVo.setUserId(AuthContextHolder.getUserId(request));
        feedbackService.saveFeedback(feedbackVo);
        return Result.ok();
    }

    @ApiOperation(value = "分页获取反馈")
    @GetMapping("getFeedback/{page}/{limit}")
    public Result getFeedback(HttpServletRequest request, @PathVariable Long limit, @PathVariable Long page) {
        FeedbackQueryVo feedbackQueryVo = new FeedbackQueryVo();
        feedbackQueryVo.setUserId(AuthContextHolder.getUserId(request));
        Page<Feedback> pageParam = new Page<>(page, limit);
        IPage<Feedback> pageModel = feedbackService.selectPage(pageParam, feedbackQueryVo);
        return Result.ok(pageModel);
    }
}
