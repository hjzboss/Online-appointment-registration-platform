<template>
  <div class="page-component">
    <div class="context-container">
      <div class="content">
        <br>
        <br>
        <h2>{{ title }}</h2>
        <div class="item v-link clickable dark" v-for="(item,index) in messageList" :key="index" @click="show(index)">
          <p>{{ item }}</p>
          <br>
          <br>
        </div>
        <br v-for="(item, index) in new Array(18)" :key="index">
        <el-pagination
          layout="prev, pager, next"
          :total="10">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  data() {
    return {
      title: null,
      type: null,
      messageList: []
    }
  },
  created() {
    this.type = this.$route.params.num;
    console.log(this.type)
    this.init();
  },
  methods: {
    init() {
      //type为0为平台公告
      if (this.type === '0' || this.type === 0) {
        this.title = '平台公告'
        this.messageList = [
          '关于延长唐都医院放假的通知',
          '临潼人民医院号源暂停更新通知',
          '红会医院号源暂停更新通知'
        ]
      }
      //1为停诊通知
      else {
        this.title = '停诊通知'
        this.messageList = [
          '核工业417呼吸内科门诊停诊公告',
          '西京医院老年医学科门诊停诊公告',
          '西安市中医院中西医结合心内科门诊停诊公告'
        ]
      }
    },
    //跳转到指定页面，如果是停诊通知要+3
    show(index) {
      let hoscode;
      if (this.type === 0 || this.type === '0') {
        hoscode = index
        window.location.href = '/notice/' + hoscode
      } else {
        hoscode = String(parseInt(index) + 3)
        window.location.href = '/notice/' + hoscode
      }
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
