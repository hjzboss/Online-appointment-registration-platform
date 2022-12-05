# 陕医通网上预约挂号平台

#### 介绍
基于springboot和vue实现，使用spring cloud alibaba微服务，能够完成预约挂号和网上就诊

#### 软件架构
* common：工具类
* hospital-manage：医院接口模拟平台，模拟医院上传数据
* model：基本对象类
* service-clien：客户端类，用于微服务之间feign远程调用
* service-gateway：网关
* service：主要微服务模块目录
* yygh-ui：前端页面
