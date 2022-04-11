package com.hjznb.yygh.common.exception;

import com.hjznb.yygh.common.result.Result;
import com.hjznb.yygh.vo.excep.ExcepVo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理类
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 异常处理方法，只要出现异常就调用此方法
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody //将结果以json形式输出
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.fail();
    }

    @ExceptionHandler(YyghException.class)
    @ResponseBody
    public Result error(YyghException e) {
        e.printStackTrace();
        return Result.fail(new ExcepVo(e.getCode(), e.getMessage()));
    }
}
