package com.hjznb.yygh;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/4/11 21:32
 */
public class test {
    public static void main(String[] args) {
        String username = "hjzboss";
        String regExp = "^[^0-9][\\w_]{5,9}$";
        System.out.println(username.matches(regExp));
    }
}
