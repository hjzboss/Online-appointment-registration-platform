package com.hjznb.yygh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import com.hjznb.yygh.common.exception.YyghException;
import com.hjznb.yygh.common.result.ResultCodeEnum;
import com.hjznb.yygh.service.MsmService;
import com.hjznb.yygh.utils.ConstantPropertiesUtils;
import com.hjznb.yygh.vo.msm.MsmVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Set;

/**
 * @author hjz
 * @version 1.0
 * @date 2022/4/23 20:50
 */
@Service
public class MsmServiceImpl implements MsmService {
    @Override
    public boolean send(String phone, String code) {
        HashMap<String, Object> result = null;
        //初始化SDK
        CCPRestSmsSDK restAPI = new CCPRestSmsSDK();

        //******************************注释*********************************************
        //*初始化服务器地址和端口                                                       *
        //*沙盒环境（用于应用开发调试）：restAPI.init("sandboxapp.cloopen.com", "8883");*
        //*生产环境（用户应用上线使用）：restAPI.init("app.cloopen.com", "8883");       *
        //*******************************************************************************
        restAPI.init("app.cloopen.com", "8883");

        //******************************注释*********************************************
        //*初始化主帐号和主帐号令牌,对应官网开发者主账号下的ACCOUNT SID和AUTH TOKEN     *
        //*ACOUNT SID和AUTH TOKEN在登陆官网后，在“应用-管理控制台”中查看开发者主账号获取*
        //*参数顺序：第一个参数是ACOUNT SID，第二个参数是AUTH TOKEN。                   *
        //*******************************************************************************
        restAPI.setAccount(ConstantPropertiesUtils.SID, ConstantPropertiesUtils.TOKEN);


        //******************************注释*********************************************
        //*初始化应用ID                                                                 *
        //*测试开发可使用“测试Demo”的APP ID，正式上线需要使用自己创建的应用的App ID     *
        //*应用ID的获取：登陆官网，在“应用-应用列表”，点击应用名称，看应用详情获取APP ID*
        //*******************************************************************************
        restAPI.setAppId(ConstantPropertiesUtils.APPID);


        //******************************注释****************************************************************
        //*调用发送模板短信的接口发送短信                                                                  *
        //*参数顺序说明：                                                                                  *
        //*第一个参数:是要发送的手机号码，可以用逗号分隔，一次最多支持100个手机号                          *
        //*第二个参数:是模板ID，在平台上创建的短信模板的ID值；测试的时候可以使用系统的默认模板，id为1。    *
        //*系统默认模板的内容为“【云通讯】您使用的是云通讯短信模板，您的验证码是{1}，请于{2}分钟内正确输入”*
        //*第三个参数是要替换的内容数组。																														       *
        //**************************************************************************************************

        //**************************************举例说明***********************************************************************
        //*假设您用测试Demo的APP ID，则需使用默认模板ID 1，发送手机号是13800000000，传入参数为6532和5，则调用方式为           *
        //*result = restAPI.sendTemplateSMS("13800000000","1" ,new String[]{"6532","5"});																		  *
        //*则13800000000手机号收到的短信内容是：【云通讯】您使用的是云通讯短信模板，您的验证码是6532，请于5分钟内正确输入     *
        //*********************************************************************************************************************
        result = restAPI.sendTemplateSMS(phone, "1", new String[]{code, "5"});
        System.out.println("SDKTestGetSubAccounts result=" + result);
        if ("000000".equals(result.get("statusCode"))) {
            //正常返回输出data包体信息（map）
            HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for (String key : keySet) {
                Object object = data.get(key);
                System.out.println(key + " = " + object);
            }
            return true;
        } else {
            //异常返回输出错误码和错误信息
            System.out.println("短信发送失败！");
        }
        return false;
    }

    @Override
    public boolean send(MsmVo msmVo) {
        if (!StringUtils.isEmpty(msmVo.getPhone())) {
            int type = msmVo.getType().getType();
            String message = null;
            switch (type) {
                case 0: {
                    // 预约成功短信通知
                    String fetchAddress = (String) msmVo.getParam().get("fetchAddress");
                    String fetchTime = (String) msmVo.getParam().get("fetchTime");
                    String name = (String) msmVo.getParam().get("name");
                    String hosname = (String) msmVo.getParam().get("hosname");
                    String date = (String) msmVo.getParam().get("reserveDate");
                    String title = (String) msmVo.getParam().get("title");
                    String number = (String) msmVo.getParam().get("number");
                    message = "【" + hosname + "】提醒您：" + name + "您好，您的订单" + date + " " + title + "已预约成功，请于" + fetchTime + "在" + fetchAddress + "凭借身份证取号";
                    break;
                }
                case 1: {
                    // 取消预约短信通知
                    String name = (String) msmVo.getParam().get("name");
                    String hosname = (String) msmVo.getParam().get("hosname");
                    String date = (String) msmVo.getParam().get("date");
                    message = "【" + hosname + "】提醒您：" + name + "您好，" + date + "的预约已成功取消";
                    break;
                }
                case 2: {
                    // 就诊提醒
                    String date = (String) msmVo.getParam().get("reserveDate");
                    String title = (String) msmVo.getParam().get("title");
                    String name = (String) msmVo.getParam().get("name");
                    message = "【陕医通预约挂号平台】提醒您：" + name + "您好，您有就诊通知：" + date + " " + title + "。请及时就诊";
                    break;
                }
                default: {
                    return false;
                }
            }
            return this.send(msmVo.getPhone(), message);
        }
        return false;
    }

}
