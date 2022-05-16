package com.hjznb.yygh.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/5/15 13:12
 */

@Data
@ApiModel(description = "修改对象")
public class ModifyVo {

    @ApiModelProperty(value = "用户ID")
    private Long id;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "验证码")
    private String code;
}
