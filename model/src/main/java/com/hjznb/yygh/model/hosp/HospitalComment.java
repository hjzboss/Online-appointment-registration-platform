package com.hjznb.yygh.model.hosp;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hjznb.yygh.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/5/16 16:20
 */
@Data
@ApiModel(description = "医院评论")
@TableName("hospital_comment")
public class HospitalComment extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单号")
    @TableField("out_trade_no")
    private String outTradeNo;

    @ApiModelProperty(value = "医院名称")
    @TableField("hosname")
    private String hosname;

    @ApiModelProperty(value = "医院编号")
    @TableField("hoscode")
    private String hoscode;

    @ApiModelProperty(value = "就诊时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("reserve_date")
    private Date reserveDate;

    @ApiModelProperty(value = "科室名称")
    @TableField("depname")
    private String depname;

    @ApiModelProperty(value = "医生职称")
    @TableField("title")
    private String title;

    @ApiModelProperty(value = "评级")
    @TableField("star")
    private Integer star;

    @ApiModelProperty(value = "状态")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "评论")
    @TableField("comment")
    private String comment;
}
