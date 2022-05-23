package com.hjznb.yygh.model.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hjznb.yygh.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/5/21 10:20
 */
@Data
@ApiModel(description = "Feedback")
@TableName("feedback")
public class Feedback extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "反馈信息")
    @TableField("feedback")
    private String feedback;

    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "回复信息")
    @TableField("reply")
    private String reply;

    @ApiModelProperty(value = "状态")
    @TableField("status")
    private Long status;
}
