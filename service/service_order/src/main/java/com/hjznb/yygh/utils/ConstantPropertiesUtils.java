package com.hjznb.yygh.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConstantPropertiesUtils implements InitializingBean {

    public static String APPID;
    public static String PARTNER;
    public static String PARTNERKEY;
    public static String CERT;
    @Value("${weixin.appid}")
    private String appid;
    @Value("${weixin.partner}")
    private String partner;
    @Value("${weixin.partnerkey}")
    private String partnerkey;
    @Value("${weixin.cert}")
    private String cert;

    @Override
    public void afterPropertiesSet() throws Exception {
        APPID = appid;
        PARTNER = partner;
        PARTNERKEY = partnerkey;
        CERT = cert;
    }
}

