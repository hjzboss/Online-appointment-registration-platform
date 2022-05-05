package com.hjznb.yygh.enums;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/5/5 13:31
 */
public enum MsmTypeEnum {
    MSM_ORDER(0, "预约"),
    MSM_CANCEL(1, "取消预约"),
    MSM_PATIENT(2, "就诊"),
    ;

    private Integer type;
    private String message;

    MsmTypeEnum(int type, String message) {
        this.type = type;
        this.message = message;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
