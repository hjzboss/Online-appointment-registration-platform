package com.hjznb.yygh.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hjznb.yygh.common.result.Result;
import com.hjznb.yygh.model.hosp.HospitalComment;
import com.hjznb.yygh.model.user.Feedback;
import com.hjznb.yygh.service.FeedbackService;
import com.hjznb.yygh.vo.comment.CommentQueryVo;
import com.hjznb.yygh.vo.feedback.FeedbackQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/5/21 11:23
 */
@Api(tags = "管理员反馈管理接口")
@RestController
@RequestMapping("/admin/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @ApiOperation(value = "条件获取分页列表")
    @GetMapping("{page}/{limit}")
    public Result getFeedbackPageList(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(name = "hospitalCommentVo", value = "查询对象", required = false) FeedbackQueryVo feedbackQueryVo) {
        Page<Feedback> pageParam = new Page<>(page, limit);
        IPage<Feedback> pageModel = feedbackService.selectPage(pageParam, feedbackQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "根据id删除反馈")
    @DeleteMapping("{id}")
    public Result deleteFeedback(@PathVariable Long id) {
        feedbackService.removeById(id);
        return Result.ok();
    }

    @ApiOperation(value = "根据id获取反馈")
    @GetMapping("{id}")
    public Result getFeedback(@PathVariable Long id) {
        return Result.ok(feedbackService.getById(id));
    }

    @ApiOperation(value = "根据id批量删除反馈")
    @DeleteMapping("batchRemove")
    public Result batchRemoveHospitalSet(@RequestBody List<Long> idList) {
        feedbackService.removeByIds(idList);
        return Result.ok();
    }

    @ApiOperation(value = "修改反馈状态")
    @PutMapping("{id}/{status}")
    public Result changeStatus(@PathVariable Long id, @PathVariable Long status) {
        Feedback byId = feedbackService.getById(id);
        byId.setStatus(status);
        feedbackService.updateById(byId);
        return Result.ok();
    }

    @ApiOperation(value = "回复反馈")
    @PostMapping("{id}/{reply}")
    public Result replyFeedback(@PathVariable Long id, @PathVariable String reply) {
        Feedback byId = feedbackService.getById(id);
        byId.setReply(reply);
        feedbackService.updateById(byId);
        return Result.ok();
    }
}
