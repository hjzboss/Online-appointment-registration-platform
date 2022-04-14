<template>
  <div class="header-container">
    <div class="wrapper">
      <!-- logo -->
      <div class="left-wrapper v-link selected">
        <img
          style="width: 50px"
          width="50"
          height="50"
          src="~assets/images/logo.png"
        />
        <span class="text">陕医通预约挂号统一平台</span>
      </div>
      <!-- 搜索框 -->
      <div class="search-wrapper">
        <div class="hospital-search animation-show">
          <el-autocomplete
            class="search-input small"
            prefix-icon="el-icon-search"
            placeholder="点击输入医院名称"
            @select="handleSelect"
          >
            <span
              slot="suffix"
              class="search-btn v-link highlight clickable selected"
            >搜索
            </span>
          </el-autocomplete>
        </div>
      </div>
      <!-- 右侧 -->
      <div class="right-wrapper">
        <span class="v-link clickable">帮助中心</span>
        <span
          v-if="name === ''"
          class="v-link clickable"
          @click="showLogin()"
          id="loginDialog"
        >登录/注册</span
        >
        <el-dropdown v-if="name !== ''" @command="loginMenu">
          <span class="el-dropdown-link">
            {{ name }}<i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu class="user-name-wrapper" slot="dropdown">
            <el-dropdown-item command="/user">实名认证</el-dropdown-item>
            <el-dropdown-item command="/order">挂号订单</el-dropdown-item>
            <el-dropdown-item command="/patient">就诊人管理</el-dropdown-item>
            <el-dropdown-item command="/logout" divided
            >退出登录
            </el-dropdown-item
            >
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>
    <!-- 登录弹出层 -->
    <el-dialog
      :visible.sync="dialogUserFormVisible"
      style="text-align: left"
      top="50px"
      :append-to-body="true"
      width="960px"
      v-if="dialogUserFormVisible"
    >
      <div class="container">
        <!-- 用户名账号登录 #start -->
        <div
          class="operate-view"
          v-if="dialogAtrr.showLoginType === 'username'"
        >
          <div class="wrapper" style="width: 100%">
            <div class="mobile-wrapper" style="position: static; width: 70%">
              <el-form>
                <el-form-item>
                  <span class="title">{{ dialogAtrr.labelTips }}</span>
                  <el-input
                    v-model="dialogAtrr.inputValue"
                    :placeholder="dialogAtrr.placeholder"
                    :maxlength="dialogAtrr.maxlength"
                    class="v-input input"
                  >
                  </el-input>
                </el-form-item>
              </el-form>
              <el-form>
                <el-form-item>
                  <span class="title">密码</span>
                  <el-input
                    v-model="dialogAtrr.inputValue1"
                    :maxlength="20"
                    class="v-input input"
                    show-password
                  >
                  </el-input>
                </el-form-item>
              </el-form>
              <div class="send-button v-button" @click="login()">
                {{ dialogAtrr.loginBtn }}
              </div>
              <div class="send-button v-button" @click="regist()">
                注册
              </div>
              <div class="bottom">
                <div class="wechat1" @click="weixinLogin()"><span
                  class="iconfont icon"></span></div>
                <span class="third-text"> 第三方账号登录 </span></div>

            </div>
          </div>
        </div>
        <!-- 用户名账号登录 #end -->

        <!-- 用户名账号注册 #start -->
        <div
          class="operate-view"
          v-if="dialogAtrr.showLoginType === 'regist'"
        >
          <div class="wrapper" style="width: 100%">
            <div class="mobile-wrapper" style="position: static; width: 70%">
              <el-form>
                <el-form-item>
                  <span class="title">{{ dialogAtrr.labelTips }}</span>
                  <el-input
                    v-model="dialogAtrr.inputValue"
                    :placeholder="dialogAtrr.placeholder"
                    :maxlength="dialogAtrr.maxlength"
                    class="v-input"
                  >
                  </el-input>
                </el-form-item>
              </el-form>
              <el-form>
                <el-form-item>
                  <span class="title">密码</span>
                  <el-input
                    v-model="dialogAtrr.inputValue1"
                    :maxlength="20"
                    class="v-input"
                    show-password
                  >
                  </el-input>
                </el-form-item>
                <el-form-item>
                  <span class="title">确认密码</span>
                  <el-input
                    v-model="dialogAtrr.inputValue2"
                    :maxlength="20"
                    class="v-input"
                    show-password
                  >
                  </el-input>
                </el-form-item>
                <el-form-item>
                  <span class="title">手机号</span>
                  <el-input
                    v-model="dialogAtrr.inputValue3"
                    :maxlength="11"
                    class="v-input"
                    :placeholder="dialogAtrr.placeholder1"
                  >
                  </el-input>
                </el-form-item>
              </el-form>
              <div class="send-button1 v-button" @click="registSend()">
                {{ dialogAtrr.loginBtn }}
              </div>
            </div>
          </div>
        </div>
        <!-- 用户名账号注册 #end -->

        <!-- 微信登录 #start -->
        <div class="operate-view" v-if="dialogAtrr.showLoginType === 'weixin'">
          <div class="wrapper wechat" style="height: 400px">
            <div>
              <div id="weixinLogin"></div>
            </div>
            <div class="bottom wechat" style="margin-top: -80px">
              <div class="phone-container">
                <div class="phone-wrapper" @click="usernameLogin()">
                  <span class="iconfont icon"></span>
                </div>
                <span class="third-text"> 微信扫码登录 </span>
              </div>
            </div>
          </div>
        </div>
        <!-- 微信登录 #end -->

        <div class="info-wrapper">
          <div class="code-wrapper">
            <div><img src="//img.114yygh.com/static/web/code_login_wechat.png" class="code-img">
              <div class="code-text">微信扫一扫关注
              </div>
              <div class="code-text"> “快速预约挂号”</div>
            </div>
            <div class="wechat-code-wrapper">
              <img
                src="//img.114yygh.com/static/web/code_app.png"
                class="code-img"
              />
              <div class="code-text">扫一扫下载</div>
              <div class="code-text">“预约挂号”APP</div>
            </div>
          </div>
          <div class="slogan">
            <div>xxxxxx官方指定平台</div>
            <div>快速挂号 安全放心</div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import cookie from "js-cookie";
import Vue from "vue";

import userInfoApi from "@/api/userInfo";
import hospitalApi from "@/api/hosp";

const defaultDialogAtrr = {
  showLoginType: "username", // 控制手机登录与微信登录切换

  labelTips: "用户名", // 输入框提示

  inputValue: "", // 输入框绑定对象
  inputValue1: "", // 输入框绑定对象
  inputValue2: "",
  inputValue3: "",
  placeholder: "请输入账号", // 输入框placeholder
  placeholder1: "请输入手机号", // 输入框placeholder
  maxlength: 11, // 输入框长度控制

  loginBtn: "登录", // 登录按钮或获取验证码按钮文本
};

export default {
  data() {
    return {
      userInfo: {
        username: "",
        password: "",
        openid: "",
        phone: ""
      },

      dialogUserFormVisible: false,
      // 弹出层相关属性
      dialogAtrr: defaultDialogAtrr,
      name: "" // 用户登录显示的名称
    };
  },

  created() {
    this.showInfo();
  },
  mounted() {
    // 注册全局登录事件对象
    window.loginEvent = new Vue();
    // 监听登录事件
    loginEvent.$on('loginDialogEvent', function () {
      document.getElementById("loginDialog").click();
    })
    // 触发事件，显示登录层：loginEvent.$emit('loginDialogEvent')
  },

  methods: {
    // 注册
    regist() {
      this.dialogAtrr.showLoginType = "regist";
      this.dialogAtrr.loginBtn = "注册"
    },
    // 发送注册请求
    registSend() {
      //至少8位且必有数字+特殊字符+字母
      let passwordReg = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[`~!@#$%^&*()_+<>?:"{},.\/\\;'[\]])[A-Za-z\d`~!@#$%^&*()_+<>?:"{},.\/\\;'[\]]{8,}$/;
      let username = this.dialogAtrr.inputValue;
      let password = this.dialogAtrr.inputValue1;
      let passwordRepeat = this.dialogAtrr.inputValue2;
      let phone = this.dialogAtrr.inputValue3
      //4到16位（字母，数字，下划线，减号）
      let usernameReg = /^[a-zA-Z0-9_-]{4,16}$/;
      let phoneReg = /^[a-zA-Z0-9_-]{4,16}$/;
      if (!usernameReg.test(username)) {
        this.$message.error("用户名必须为4到16位的字母，数字，下划线，减号组合！");
        return;
      }
      if (!passwordReg.test(password)) {
        this.$message.error("密码至少8位且必有数字+特殊字符+字母！");
        return;
      }
      if (password !== passwordRepeat) {
        this.$message.error("两次密码输入不一致！");
        return;
      }
      if (!phoneReg.test(phone)) {
        this.$message.error("手机号码有误！");
        return;
      }
      this.userInfo.username = username;
      this.userInfo.password = password;
      this.userInfo.phone = phone;
      this.dialogAtrr.loginBtn = "正在注册...";

      userInfoApi.regist(this.userInfo)
        .then(response => {
          this.$message.info("注册成功！")
          this.usernameLogin()
        })
        .catch(e => {
          this.dialogAtrr.loginBtn = "注册";
        })
    },

    // 绑定登录，点击显示登录层
    showLogin() {
      this.dialogUserFormVisible = true;

      // 初始化登录层相关参数
      this.dialogAtrr = {...defaultDialogAtrr};
    },

    // 登录
    login() {
      this.userInfo.username = this.dialogAtrr.inputValue;
      this.userInfo.password = this.dialogAtrr.inputValue1;

      if (this.dialogAtrr.loginBtn === "正在登录...") {
        this.$message.error("请勿重复登录");
        return;
      }
      //4到16位（字母，数字，下划线，减号）
      let uPattern = /^[a-zA-Z0-9_-]{4,16}$/;
      if (!uPattern.test(this.userInfo.username)) {
        this.$message.error("用户名格式不正确");
        return;
      }
      this.dialogAtrr.loginBtn = "正在登录...";
      userInfoApi
        .login(this.userInfo)
        .then((response) => {
          console.log(response.data);
          // 登录成功 设置cookie
          this.setCookies(response.data.name, response.data.token);
        })
        .catch((e) => {
          this.dialogAtrr.loginBtn = "登录";
        });
    },

    setCookies(name, token) {
      cookie.set("token", token, {domain: "localhost"});
      cookie.set("name", name, {domain: "localhost"});
      window.location.reload();
    },

    showInfo() {
      let token = cookie.get("token");
      if (token) {
        this.name = cookie.get("name");
        console.log(this.name);
      }
    },

    loginMenu(command) {
      if ("/logout" === command) {
        cookie.set("name", "", {domain: "localhost"});
        cookie.set("token", "", {domain: "localhost"});

        //跳转页面
        window.location.href = "/";
      } else {
        window.location.href = command;
      }
    },

    handleSelect(item) {
      window.location.href = "/hospital/" + item.hoscode;
    },

    weixinLogin() {
      this.dialogAtrr.showLoginType = "weixin";
    },

    usernameLogin() {
      this.dialogAtrr.showLoginType = "username";
      this.showLogin();
    },
  },
};
</script>
