package com.hjznb.yygh.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hjznb.yygh.common.result.Result;
import com.hjznb.yygh.model.hosp.HospitalComment;
import com.hjznb.yygh.service.HospitalCommentService;
import com.hjznb.yygh.vo.comment.CommentQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @ApiParam(name = "hospitalCommentVo", value = "查询对象", required = false) CommentQueryVo commentQueryVo) {
        Page<HospitalComment> pageParam = new Page<>(page, limit);
        IPage<HospitalComment> pageModel = hospitalCommentService.selectPage(pageParam, commentQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "根据id获取评论")
    @GetMapping("getComment/{id}")
    public Result getById(@PathVariable Integer id) {
        return Result.ok(hospitalCommentService.getCommentById(id));
    }

    @ApiOperation(value = "修改评论状态")
    @PutMapping("updateStatus/{id}/{status}")
    public Result changeStatus(@PathVariable Integer id, @PathVariable Integer status) {
        hospitalCommentService.updateStatusById(id, status);
        return Result.ok();
    }

    @ApiOperation(value = "根据id删除评论")
    @DeleteMapping("deleteComment/{id}")
    public Result deleteById(@PathVariable Integer id) {
        boolean flag = hospitalCommentService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "根据id批量删除评论")
    @DeleteMapping("batchRemove")
    public Result batchRemoveHospitalSet(@RequestBody List<Long> idList) {
        hospitalCommentService.removeByIds(idList);
        return Result.ok();
    }

}
