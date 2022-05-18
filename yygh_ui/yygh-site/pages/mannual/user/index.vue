<template>
  <!-- header -->
  <div class="nav-container page-component">
    <!--左侧导航 #start -->
    <div class="nav left-nav">
      <div class="nav-item ">
        <span class="v-link clickable dark" onclick="window.location='/mannual'"> 常见问题 </span>
      </div>
      <div class="nav-item selected">
        <span class="v-link selected dark" onclick="window.location='/mannual/user'"> 账号指南 </span>
      </div>
      <div class="nav-item">
        <span class="v-link clickable dark" onclick="window.location='/mannual/order'"> 预约指南 </span>
      </div>
    </div>
    <!-- 左侧导航 #end -->
    <!-- 右侧内容 #start -->
    <div class="page-container">
      <div class="hospital-detail">
        <h3>网站预约挂号如何注册和登录？</h3>
        <h4>第一步</h4>
        <p>进入陕医通预约挂号统一平台网站首页，点击“登录/注册”链接。</p>
        <p><img src="~assets/images/1.png" alt=""></p>
        <h4>第二步</h4>
        <p>根据系统提示填写手机号验证码，并输入姓名和证件号码，完成实名认证，提交注册。</p>
        <p><img src="~assets/images/2.png" alt=""></p>
        <p><img src="~assets/images/3.png" alt=""></p>
        <h4>第三步</h4>
        <p>完成注册后可添加就诊人操作挂号。</p>
        <p><img src="~assets/images/4.png" alt=""></p>
        <h4>温馨提示</h4>
        <p>身份证号和姓名必须是患者本人的，要跟身份证上的保持一致。</p>
        <p>还有手机号这一项，建议用户正确填写自己的真实有效号码，以便正常的接收平台下发的短信，短信服务通道对以下情况的用户不能提供短信下发服务，具体请查阅《为什么我老是收不到验证码》。</p>
        <h3>为什么需要提交实名信息？</h3>
        <p>因为挂号预约时，医院需要实名信息与医保卡关联并进行登记，为您的就诊带来方便。同时为抑制倒号行为，保障就诊患者权益，到医院取号时也需要核对患者实名信息的。
          所以，请务必注册您的真实有效信息，否则我们将无法提供服务。</p>
        <h3>一个手机号可以注册几个用户？</h3>
        <p>一个手机号可以注册1个用户，但是可以绑定4个就诊人</p>
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
    return {
    }
  },
  methods: {
  }
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
