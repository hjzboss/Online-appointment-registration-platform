package com.hjznb.yygh.vo.hosp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/5/16 16:54
 */
@Data
@ApiModel(description = "HospitalComment")
public class HospitalCommentVo {
    @ApiModelProperty(value = "评分")
    private Integer star;

    @ApiModelProperty(value = "订单号")
    private String outTradeNo;

    @ApiModelProperty(value = "就诊日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date reserveDate;

    @ApiModelProperty(value = "医院名称")
    private String hosname;

    @ApiModelProperty(value = "医院编号")
    private String hoscode;

    @ApiModelProperty(value = "科室名称")
    private String depname;

    @ApiModelProperty(value = "医生职称")
    private String title;

    @ApiModelProperty(value = "评论")
    private String comment;

    @ApiModelProperty(value = "订单状态")
    private Integer status;
}
