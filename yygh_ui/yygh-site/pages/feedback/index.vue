<template>
  <!-- header -->
  <div class="page-component nav-container">
    <!--左侧导航 #start -->
    <div class="nav left-nav">
      <div class="nav-item selected">
        <span class="v-link selected dark" onclick="window.location='/feedback'"> 意见反馈 </span>
      </div>
      <div class="nav-item ">
        <span class="v-link clickable dark" onclick="window.location='/feedback/record'"> 反馈记录 </span>
      </div>
    </div>
    <!-- 左侧导航 #end -->
    <div class="page-container">
      <div class="hospital-detail">
        <h2>意见反馈</h2>
        <br>
        <p>
          <el-input
            type="textarea"
            :rows="15"
            placeholder="请输入您的意见，我们会根据您的意见认真改进"
            v-model="feedback.feedback"
            maxlength="500"
            show-word-limit>
          </el-input>
        </p>
        <br>
        <br>
        <br>
        <p align="center">
          <el-button type="primary" @click="postFeedback">递交意见</el-button>
        </p>
      </div>
    </div>
  </div>
  <!-- footer -->
</template>
<script>
import '~/assets/css/hospital_personal.css'
import '~/assets/css/hospital.css'
import feedApi from '@/api/feedback/feedback'

export default {
  data() {
    return {
      feedback: {}
    }
  },
  methods: {
    postFeedback() {
      console.log(this.feedback)
      feedApi.postFeedback(this.feedback).then(response => {
        this.$alert('提交成功，谢谢您的反馈', '成功', {
          confirmButtonText: '确定',
          callback: action => {
            window.location.reload()
          }
        });
      })
    }
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
