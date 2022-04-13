package com.hjznb.yygh;

import com.hjznb.yygh.common.utils.MD5;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/4/11 21:32
 */
public class test {
    public static void main(String[] args) {
        String str = "e10adc3949ba59abbe56e057f20f883e";
        String a = MD5.encrypt("123456");
        System.out.println(a.equals(str));
    }
}
