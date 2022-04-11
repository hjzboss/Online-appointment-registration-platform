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
        <span class="text">陕医通 预约挂号统一平台</span>
      </div>
      <!-- 搜索框 -->
      <div class="search-wrapper">
        <div class="hospital-search animation-show">
          <el-autocomplete
            class="search-input small"
            prefix-icon="el-icon-search"
            v-model="state"
            :fetch-suggestions="querySearchAsync"
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
          v-if="name == ''"
          class="v-link clickable"
          @click="showLogin()"
          id="loginDialog"
          >登录/注册</span
        >
        <el-dropdown v-if="name != ''" @command="loginMenu">
          <span class="el-dropdown-link">
            {{ name }}<i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu class="user-name-wrapper" slot="dropdown">
            <el-dropdown-item command="/user">实名认证</el-dropdown-item>
            <el-dropdown-item command="/order">挂号订单</el-dropdown-item>
            <el-dropdown-item command="/patient">就诊人管理</el-dropdown-item>
            <el-dropdown-item command="/logout" divided
              >退出登录</el-dropdown-item
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
      @close="closeDialog()"
    >
      <div class="container">
        <!-- 用户名账号登录 #start -->
        <div
          class="operate-view"
          v-if="dialogAtrr.showLoginType === 'username'"
        >
          <div class="wrapper" style="width: 100%">
            <div class="mobile-wrapper" style="position: static; width: 70%">
              <span class="title">{{ dialogAtrr.labelTips }}</span>
              <el-form>
                <el-form-item>
                  <el-input
                    v-model="dialogAtrr.inputValue"
                    :placeholder="dialogAtrr.placeholder"
                    :maxlength="dialogAtrr.maxlength"
                    class="input v-input"
                  >
                  </el-input>
                </el-form-item>
              </el-form>
              <span class="title">密码</span>
              <el-form>
                <el-form-item>
                  <el-input
                    v-model="userInfo.password"
                    :maxlength="20"
                    class="input v-input"
                    show-password
                  >
                  </el-input>
                </el-form-item>
              </el-form>
              <div class="send-button v-button" @click="btnClick()">
                {{ dialogAtrr.loginBtn }}
              </div>
            </div>
            <div class="bottom">
              <div class="wechat-wrapper" @click="weixinLogin()">
                <span class="iconfont icon"></span>
              </div>
              <span class="third-text"> 第三方账号登录 </span>
            </div>
          </div>
        </div>
        <!-- 用户名登录 #end -->

        <!-- 微信登录 #start -->
        <div class="operate-view" v-if="dialogAtrr.showLoginType === 'weixin'">
          <div class="wrapper wechat" style="height: 400px">
            <div>
              <div id="weixinLogin"></div>
            </div>
            <div class="bottom wechat" style="margin-top: -80px">
              <div class="phone-container">
                <div class="phone-wrapper" @click="phoneLogin()">
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
            <div>
              <img
                src="//img.114yygh.com/static/web/code_login_wechat.png"
                class="code-img"
              />
              <div class="code-text">
                <span class="iconfont icon"></span>微信扫一扫关注
              </div>
              <div class="code-text">“快速预约挂号”</div>
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
  placeholder: "请输入账号", // 输入框placeholder
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
      },

      dialogUserFormVisible: false,
      // 弹出层相关属性
      dialogAtrr: defaultDialogAtrr,
      name: "", // 用户登录显示的名称
      querySearchAsync: [],
      state: null,
    };
  },

  created() {
    this.showInfo();
  },
  methods: {
    // 绑定登录或获取验证码按钮
    btnClick() {
      // 登录
      this.login();
    },

    // 绑定登录，点击显示登录层
    showLogin() {
      this.dialogUserFormVisible = true;

      // 初始化登录层相关参数
      this.dialogAtrr = { ...defaultDialogAtrr };
    },

    // 登录
    login() {
      this.userInfo.username = this.dialogAtrr.inputValue;

      if (this.dialogAtrr.loginBtn == "正在登录...") {
        this.$message.error("请勿重复登录");
        return;
      }
      //4到16位（字母，数字，下划线，减号）
      var uPattern = /^[a-zA-Z0-9_-]{4,16}$/;
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
          // this.dialogAtrr.loginBtn = "马上登录";
          this.$message.error("请先注册！");
        });
    },

    setCookies(name, token) {
      cookie.set("token", token, { domain: "localhost" });
      cookie.set("name", name, { domain: "localhost" });
      window.location.reload();
    },
    // 关闭登录层
    closeDialog() {
      if (this.clearSmsTime) {
        clearInterval(this.clearSmsTime);
      }
    },

    showInfo() {
      let token = cookie.get("token");
      if (token) {
        this.name = cookie.get("name");
        console.log(this.name);
      }
    },

    loginMenu(command) {
      if ("/logout" == command) {
        cookie.set("name", "", { domain: "localhost" });
        cookie.set("token", "", { domain: "localhost" });

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

    phoneLogin() {
      this.dialogAtrr.showLoginType = "username";
      this.showLogin();
    },
  },
};
</script>
