package com.hjznb.yygh.vo.feedback;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/5/21 10:24
 */
@Data
@ApiModel(description = "反馈对象")
public class FeedbackVo {

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "反馈信息")
    private String feedback;
}
