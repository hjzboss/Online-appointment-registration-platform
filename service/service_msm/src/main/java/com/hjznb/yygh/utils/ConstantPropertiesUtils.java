package com.hjznb.yygh.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 容联云工具类，读取配置文件内容
 */

@Component
public class ConstantPropertiesUtils implements InitializingBean {

    public static String SID;
    public static String TOKEN;
    public static String APPID;
    @Value("${rly.sid}")
    private String sid;
    @Value("${rly.token}")
    private String token;
    @Value("${rly.appId}")
    private String appId;

    @Override
    public void afterPropertiesSet() throws Exception {
        SID = sid;
        TOKEN = token;
        APPID = appId;
    }

}
