package com.hjznb.yygh.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hjznb.yygh.common.result.Result;
import com.hjznb.yygh.model.hosp.Hospital;
import com.hjznb.yygh.model.hosp.HospitalComment;
import com.hjznb.yygh.model.order.OrderInfo;
import com.hjznb.yygh.service.HospitalCommentService;
import com.hjznb.yygh.vo.hosp.HospitalCommentVo;
import com.hjznb.yygh.vo.hosp.HospitalQueryVo;
import com.hjznb.yygh.vo.order.OrderQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/5/16 23:33
 */
@Api(tags = "管理员评论管理接口")
@RestController
@RequestMapping("/admin/comment")
public class CommentController {
    private final HospitalCommentService hospitalCommentService;

    public CommentController(HospitalCommentService hospitalCommentService) {
        this.hospitalCommentService = hospitalCommentService;
    }

    @ApiOperation(value = "条件获取分页列表")
    @GetMapping("{page}/{limit}")
    public Result getCommentPageList(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(name = "hospitalCommentVo", value = "查询对象", required = false) HospitalCommentVo hospitalCommentVo) {
        Page<HospitalComment> pageParam = new Page<>(page, limit);
        IPage<HospitalComment> pageModel = hospitalCommentService.selectPage(pageParam, hospitalCommentVo);
        return Result.ok(pageModel);
    }
}
