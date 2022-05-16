package com.hjznb.yygh.enums;

/**
 * 评论状态枚举类
 *
 * @author hjz
 * @version 1.0
 * @date 2022/5/16 19:18
 */
public enum CommentStatusEnum {
    COMMENT_WAIT(0, "待审核"),
    COMMENT_PASS(1, "审核通过"),
    COMMENT_FAIL(-1, "未通过"),
    ;

    private Integer status;
    private String message;

    CommentStatusEnum(Integer status, String message) {
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
