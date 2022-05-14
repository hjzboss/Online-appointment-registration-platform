<template>

  <!-- header -->
  <div class="nav-container page-component">
    <!--左侧导航 #start -->
    <div class="nav left-nav">
      <div class="nav-item">
        <span class="v-link clickable dark" onclick="window.location='/user'">实名认证 </span>
      </div>
      <div class="nav-item">
        <span class="v-link clickable dark" onclick="window.location='/order'"> 挂号订单 </span>
      </div>
      <div class="nav-item ">
        <span class="v-link clickable dark" onclick="window.location='/patient'"> 就诊人管理 </span>
      </div>
      <div class="nav-item ">
        <span class="v-link clickable dark"> 修改账号信息 </span>
      </div>
      <div class="nav-item  selected">
        <span class="v-link selected dark" onclick="window.location='/feedback'"> 意见反馈 </span>
      </div>
    </div>
    <!-- 左侧导航 #end -->

    <!-- 右侧内容 #start -->
    <div class="page-container">
      <div>
        <div class="title"> 意见反馈</div>
        <br>
        <br>
        <div>
          <el-input
            type="textarea"
            :rows="15"
            placeholder="请输入您的意见，我们会根据您的意见认真改进"
            v-model="textarea">
          </el-input>
          <br v-for="(item, index) in new Array(2)" :key="index">
          <el-row>
            <el-button type="primary" @click="btnClick()">{{ submitBnt }}</el-button>
          </el-row>
          <br v-for="(item, index) in new Array(5)" :key="index">
        </div>
      </div>
    </div>
    <!-- 右侧内容 #end -->

    <!-- 登录弹出框 -->
  </div>
  <!-- footer -->
</template>

<script>
import '~/assets/css/hospital_personal.css'
import '~/assets/css/hospital.css'
import '~/assets/css/personal.css'

export default {

  data() {
    return {
      textarea: null,
      submitBnt: '递交意见'
    }
  },

  created() {
    this.init()
  },

  methods: {
    init() {

    },

    btnClick() {
      //todo:提交意见到后台数据库
    }
  }
}
</script>
<style>
.header-wrapper .title {
  font-size: 16px;
  margin-top: 0;
}

.content-wrapper {
  margin-left: 0;
}

.patient-card .el-card__header .detail {
  font-size: 14px;
}

.page-container .title {
  letter-spacing: 1px;
  font-weight: 700;
  color: #333;
  font-size: 16px;
  margin-top: 0;
  margin-bottom: 20px;
}

.page-container .tips {
  width: 100%;
  padding-left: 0;
}

.page-container .form-wrapper {
  padding-left: 92px;
  width: 580px;
}

.form-normal {
  height: 40px;
}

.bottom-wrapper {
  width: 100%;
  padding: 0;
  margin-top: 0;
}
</style>
