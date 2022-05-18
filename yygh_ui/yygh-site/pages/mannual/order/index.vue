<template>
  <!-- header -->
  <div class="nav-container page-component">
    <!--左侧导航 #start -->
    <div class="nav left-nav">
      <div class="nav-item ">
        <span class="v-link clickable dark" onclick="window.location='/mannual'"> 常见问题 </span>
      </div>
      <div class="nav-item ">
        <span class="v-link clickable dark" onclick="window.location='/mannual/user'"> 账号指南 </span>
      </div>
      <div class="nav-item selected">
        <span class="v-link selected dark" onclick="window.location='/mannual/order'"> 预约指南 </span>
      </div>
    </div>
    <!-- 左侧导航 #end -->
    <!-- 右侧内容 #start -->
    <div class="page-container">
      <div class="hospital-detail">
        <h2>网站挂号流程</h2>
        <p>使用统一平台网站预约挂号服务前请仔细阅读以下使用说明。</p>
        <h3>一、注册登录</h3>
        <p>登录网上预约挂号统一平台，首次预约挂号须进行在线实名制注册，通过注册账号登录，进入预约挂号流程。（已注册用户直接登录开始预约挂号）</p>
        <p><img src="~assets/images/1.png" alt=""></p>
        <h3>二、选择医院/科室/疾病</h3>
        <p>您可以通过多种搜索方式找到您所要预约的医院。</p>
        <p><img src="~assets/images/help_search.png" alt=""></p>
        <p>进入医院页面 选择需要预约的科室</p>
        <p><img src="~assets/images/8.png" alt=""></p>
        <h3>三、选择日期/医生</h3>
        <p>选择就诊日期，蓝色代表有可预约号源，点击预约；灰色代表号源已约满或停诊；白色代表无号源，无法点击。</p>
        <p><img src="~assets/images/help_source.png" alt=""></p>
        <h3>四、填写预约信息并短信验证</h3>
        <p>点击”确认挂号”在弹出本次预约成功信息时，即为成功预约。</p>
        <p><img src="~assets/images/9.png" alt=""></p>
        <p><img src="~assets/images/10.png" alt=""></p>
        <p><img src="~assets/images/11.png" alt=""></p>
        <h3>五、接收预约成功短信</h3>
        <p>用户手机接收到预约成功短信。</p>
        <h3>六、医院就诊</h3>
        <p>就诊当日，请用户在规定的时间段内，前往医院凭就诊者本人预约登记时的有效证件就诊。</p>
        <h2>如何查询/取消预约</h2>
        <h3>方法一：</h3>
        <p>进入用户的”个人中心”，进入”个人中心>挂号订单”中的预约挂号列表，查看或取消您的预约订单，点击取消即可。</p>
        <p><img src="~assets/images/13.png" alt=""></p>
        <p><img src="~assets/images/14.png" alt=""></p>
        <p><img src="~assets/images/15.png" alt=""></p>
        <p><img src="~assets/images/12.png" alt=""></p>
      </div>
    </div>
    <!-- 右侧内容 #end -->
  </div>
  <!-- footer -->
</template>
<script>
import '~/assets/css/hospital_personal.css'
import '~/assets/css/hospital.css'

export default {
  data() {
    return {}
  },
  methods: {}
}
</script>
<style>
.hospital-detail .info-wrapper {
  width: 100%;
  padding-left: 0;
  padding-top: 0;
  margin-top: 0;
  flex-direction: inherit;
}

.hospital-detail .info-wrapper .text {
  font-size: 14px;

}

.hospital-detail .content-wrapper p {
  text-indent: 0;
}
</style>
