<template>
  <div class="nav-container page-component">
    <!--左侧导航 #start -->
    <div class="nav left-nav">
      <div class="nav-item ">
        <span class="v-link clickable dark" onclick="window.location='/user'">实名认证 </span>
      </div>
      <div class="nav-item selected">
        <span class="v-link selected dark" onclick="window.location='/order'"> 挂号订单 </span>
      </div>
      <div class="nav-item ">
        <span class="v-link clickable dark" onclick="window.location='/patient'"> 就诊人管理 </span>
      </div>
      <div class="nav-item ">
        <span class="v-link clickable dark" onclick="window.location='/modify'"> 修改账号信息 </span>
      </div>
    </div>
    <!-- 左侧导航 #end -->

    <div class="page-container">
      <div class="order-detail">
        <div class="title" v-if="orderInfo.commentStatus === 0"> 评价</div>
        <div class="title" v-if="orderInfo.commentStatus === 1"> 评价审核中</div>
        <div class="title" v-if="orderInfo.commentStatus === 2"> 已完成评价</div>
        <el-form ref="form" :model="hospitalComment" label-width="80px">
          <br>
          <br>
          <el-form-item label="就诊信息">
            就诊日期: {{ orderInfo.reserveDate }}
            <br>
            就诊医院: {{ orderInfo.hosname }}
            <br>
            就诊科室: {{ orderInfo.depname }}
            <br>
            医生职称: {{ orderInfo.title }}
          </el-form-item>
          <el-form-item label="医院打分">
            <el-rate
              v-if="orderInfo.commentStatus === 0"
              v-model="hospitalComment.star"
              show-text
            >
            </el-rate>
            <el-rate
              v-if="orderInfo.commentStatus !== 0"
              v-model="hospitalComment.star"
              show-text
              disabled
            >
            </el-rate>
          </el-form-item>
          <el-form-item label="评价">
            <div class="comment">
              <el-input
                v-if="orderInfo.commentStatus === 0"
                type="textarea"
                :rows="10"
                placeholder="请输入内容"
                maxlength="100"
                show-word-limit
                v-model="hospitalComment.comment">
              </el-input>
              <el-input
                v-if="orderInfo.commentStatus !== 0"
                disabled
                type="textarea"
                :rows="10"
                placeholder="请输入内容"
                maxlength="100"
                show-word-limit
                v-model="hospitalComment.comment">
              </el-input>
              <el-button type="text" @click="commitComment()" v-if="orderInfo.commentStatus === 0">提交评价</el-button>
            </div>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import orderInfoApi from "@/api/order/orderInfo";
import commentApi from "@/api/comment/comment";
import '~/assets/css/hospital_personal.css'
import '~/assets/css/hospital.css'


export default {
  data() {
    return {
      orderId: null,
      orderInfo: {
        param: {}
      },
      hospitalComment: {}
    }
  },
  created() {
    this.orderId = this.$route.query.orderId
    this.init()
  },
  methods: {
    init() {
      orderInfoApi.getOrders(this.orderId).then(response => {
        console.log(response.data);
        this.orderInfo = response.data
      })
      commentApi.getHospitalComment(this.orderId).then(res => {
        if (res.data !== null) {
          console.log(res.data)
          this.hospitalComment = res.data
        }
      })
    },
    // 提交评论
    commitComment() {
      this.hospitalComment.orderId = this.orderId
      this.hospitalComment.hosname = this.orderInfo.hosname
      this.hospitalComment.hoscode = this.orderInfo.hoscode
      this.hospitalComment.title = this.orderInfo.title
      this.hospitalComment.depname = this.orderInfo.depname
      this.hospitalComment.reserveDate = this.orderInfo.reserveDate
      if (this.hospitalComment.comment === '' || this.hospitalComment.comment === null || this.hospitalComment.comment === undefined) {
        this.$message.warning('请输入评论');
        return
      }
      console.log(this.hospitalComment.comment)
      if (this.hospitalComment.star === 0 || this.hospitalComment.star === null) {
        this.$message.warning('最低为1分，请先打分');
        return
      }
      console.log(this.hospitalComment.star)
      console.log(this.hospitalComment)
      commentApi.postHospitalComment(this.hospitalComment).then(response => {
        this.$alert('提交成功，等待管理员审核后', '成功', {
          confirmButtonText: '确定',
          callback: action => {
            window.location.reload()
          }
        });
      }).catch(e => {
        this.$message.error('提交失败，请稍后再试')
      })
    }
  }
}
</script>
<style>
.info-wrapper {
  padding-left: 0;
  padding-top: 0;
}

.content-wrapper {
  color: #333;
  font-size: 14px;
  padding-bottom: 0;
}

.bottom-wrapper {
  width: 100%;
}

.button-wrapper {
  margin: 0;
}

.el-form-item {
  margin-bottom: 5px;
}

.bottom-wrapper .button-wrapper {
  margin-top: 0;
}

.comment {
  width: 80%;
}
</style>
