package com.hjznb.yygh.vo.excep;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/4/11 22:33
 */

import lombok.Data;

/**
 * 异常vo
 */
@Data
public class ExcepVo {
    private Integer code;
    private String message;

    public ExcepVo(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
