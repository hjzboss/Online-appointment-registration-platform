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
      <div class="nav-item selected">
        <span class="v-link selected dark" onclick="window.location='/modify'"> 修改账号信息 </span>
      </div>
    </div>
    <!-- 左侧导航 #end -->

    <!-- 右侧内容 #start -->
    <div class="page-container">
      <div>
        <div class="title"> 修改手机号</div>
        <div class="form-wrapper">
          <div>
            <el-form :model="user" label-width="110px" label-position="left">
              <el-form-item prop="phone" label="当前手机号：" class="form-normal">
                <div class="name-input">
                  {{ userInfo.phone }}
                </div>
              </el-form-item>
              <el-form-item prop="newPhone" label="新手机号：" class="form-normal">
                <el-input v-model="user.phone" placeholder="请输入新手机号" class="input v-input"/>
              </el-form-item>
              <el-form-item prop="code" label="手机验证码：">
                <el-input v-model="user.code" :placeholder="dialogAtrr.labelTips"
                          :maxlength="dialogAtrr.length" class="input v-input">
                    <span slot="suffix" class="sendText v-link" v-if="dialogAtrr.second > 0">{{
                        dialogAtrr.second
                      }}s </span>
                  <span slot="suffix" class="sendText v-link highlight clickable selected"
                        v-if="dialogAtrr.second === -1" @click="getCode()">发送验证码 </span>
                  <span slot="suffix" class="sendText v-link highlight clickable selected"
                        v-if="dialogAtrr.second === 0" @click="getCode()">重新发送 </span>
                </el-input>
              </el-form-item>
            </el-form>

            <div class="bottom-wrapper">
              <div class="button-wrapper">
                <div class="v-button" @click="modify()">{{ submitBnt }}</div>
              </div>
            </div>
          </div>
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

import userInfoApi from '@/api/user/userInfo'
import smsApi from "@/api/sms/sms"

const defaultDialogAtrr = {
  loginBtn: '获取验证码', // 登录按钮或获取验证码按钮文本
  labelTips: '请输入验证码',
  length: 4,
  inputValue: null,
  sending: true,      // 是否可以发送验证码
  second: -1,        // 倒计时间  second>0 : 显示倒计时 second=0 ：重新发送 second=-1 ：什么都不显示
  clearSmsTime: null  // 倒计时定时任务引用 关闭登录层清除定时任务
}
export default {

  data() {
    return {
      user: {},
      userInfo: {
        param: {}
      },
      // 弹出层相关属性
      dialogAtrr: defaultDialogAtrr,
      submitBnt: '保存'
    }
  },

  created() {
    this.init()
  },

  methods: {
    init() {
      this.getUserInfo()
    },

    getUserInfo() {
      userInfoApi.getUserInfo().then(response => {
        this.userInfo = response.data
      })
    },

    // 修改信息
    modify() {
      if (this.submitBnt === '正在提交...') {
        this.$message.error('重复提交')
        return;
      }

      if (this.user.phone === '') {
        this.$message.error('手机号必须输入')
        return;
      }
      if (this.user.code === '') {
        this.$message.error('验证码必须输入')
        return;
      }
      if (this.user.code.length !== 4) {
        this.$message.error('验证码格式不正确')
        return;
      }

      this.user.id = this.userInfo.id
      this.dialogAtrr.loginBtn = '正在提交...'
      userInfoApi.modify(this.user).then(response => {
        alert("修改成功")
        window.location.reload()
      }).catch(e => {
        this.submitBnt = '保存'
      })
    },

    // 获取验证码
    getCode() {
      if (!(/^1[34578]\d{9}$/.test(this.userInfo.phone))) {
        this.$message.error('手机号码不正确')
        return;
      }

      // 控制重复发送
      if (!this.dialogAtrr.sending) return;

      // 发送短信验证码
      this.timeDown();
      this.dialogAtrr.sending = false;
      smsApi.sendCode(this.user.phone).then(response => {
        this.timeDown();
        this.$message.info('验证码发送成功')
      }).catch(e => {
        this.$message.error('发送失败，重新发送')
      })
    },
    // 倒计时
    timeDown() {
      if (this.clearSmsTime) {
        clearInterval(this.clearSmsTime);
      }
      this.dialogAtrr.second = 60;

      this.dialogAtrr.labelTips = '验证码已发送至' + this.user.phone
      this.clearSmsTime = setInterval(() => {
        --this.dialogAtrr.second;
        if (this.dialogAtrr.second < 1) {
          clearInterval(this.clearSmsTime);
          this.dialogAtrr.sending = true;
          this.dialogAtrr.second = 0;
        }
      }, 1000);
    },
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
