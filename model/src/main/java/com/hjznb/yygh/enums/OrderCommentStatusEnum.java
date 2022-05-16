package com.hjznb.yygh.enums;

/**
 * 订单评论状态枚举类
 *
 * @author hjz
 * @version 1.0
 * @date 2022/5/16 19:18
 */
public enum OrderCommentStatusEnum {
    NO_COMMENT(0, "未评论"),
    COMMENT_AUTH(1, "审核中"),
    COMMENT_FINISH(2, "已评论"),
    ;

    private Integer status;
    private String message;

    OrderCommentStatusEnum(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
