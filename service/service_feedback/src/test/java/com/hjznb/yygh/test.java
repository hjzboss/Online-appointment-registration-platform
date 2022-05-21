package com.hjznb.yygh;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/5/21 11:20
 */
public class test {
    public static void main(String[] args) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(formatter.format(new Date()));
    }
}
