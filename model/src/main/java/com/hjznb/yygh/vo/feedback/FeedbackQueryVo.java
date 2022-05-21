package com.hjznb.yygh.vo.feedback;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/5/21 11:29
 */
@Data
@ApiModel(description = "Feedback")
public class FeedbackQueryVo {
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "创建时间")
    private String createTimeBegin;
    private String createTimeEnd;
}
