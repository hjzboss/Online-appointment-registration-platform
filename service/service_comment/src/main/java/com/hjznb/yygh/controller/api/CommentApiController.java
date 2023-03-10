package com.hjznb.yygh.controller.api;

import com.hjznb.yygh.common.result.Result;
import com.hjznb.yygh.service.HospitalCommentService;
import com.hjznb.yygh.vo.comment.HospitalCommentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/5/16 16:47
 */
@Api(tags = "供用户端调用评论的接口")
@RestController
@RequestMapping("/api/comment")
public class CommentApiController {
    private final HospitalCommentService hospitalCommentService;

    public CommentApiController(HospitalCommentService hospitalCommentService) {
        this.hospitalCommentService = hospitalCommentService;
    }

    @ApiOperation(value = "提交评论")
    @PostMapping("/postComment")
    public Result postHospitalComment(
            @RequestBody HospitalCommentVo hospitalCommentVo) {
        hospitalCommentService.saveComment(hospitalCommentVo);
        return Result.ok();
    }

    @ApiOperation(value = "根据订单号获取评论")
    @GetMapping("/getComment/{orderId}")
    public Result getHospitalComment(@PathVariable Long orderId) {
        return Result.ok(hospitalCommentService.getHospitalCommentByOrderId(orderId));
    }

    @ApiOperation(value = "获取医院评论并计算平均分")
    @GetMapping("/getAllComment/{hoscode}/{limit}")
    public Result getAllCommentAndStar(@PathVariable String hoscode, @PathVariable Long limit) {
        Map<String, Object> map = hospitalCommentService.selectPageAndStar(hoscode, limit);
        return Result.ok(map);
    }
}
