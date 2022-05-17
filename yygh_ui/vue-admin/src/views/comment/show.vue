<template>
  <div class="app-container">
    <h4>评论信息</h4>
    <table class="table table-striped table-condenseda table-bordered" width="100%">
      <tbody>
      <tr>
        <th width="15%">订单交易号</th>
        <td width="35%"><b style="font-size: 14px">{{ commentData.outTradeNo }}</b></td>
        <th width="15%">医院名称</th>
        <td width="35%">{{ commentData.hosname }}</td>
      </tr>
      <tr>
        <th>科室名称</th>
        <td>{{ commentData.depname }}</td>
        <th>医生职称</th>
        <td>{{ commentData.title }}</td>
      </tr>
      <tr>
        <th>安排日期</th>
        <td>{{ commentData.reserveDate }}</td>
        <th>星级评价</th>
        <td>
          <el-rate
            v-model="commentData.star"
            disabled
            show-score
            text-color="#ff9900"
            score-template="{value}">
          </el-rate>
        </td>
      </tr>
      <tr>
        <th>订单状态</th>
        <td>{{ commentData.param.commentStatus }}</td>
        <th>评论时间</th>
        <td>{{ commentData.createTime }}</td>
      </tr>
      </tbody>
    </table>
    <h4>用户评论</h4>
    <table class="table table-striped table-condenseda table-bordered" width="100%">
      <tbody>
      <tr>
        <td>{{ commentData.comment }}</td>
      </tr>
      <br>
      <el-row>
        <el-button @click="back">返回</el-button>
      </el-row>
      </tbody>
    </table>
  </div>
</template>
<script>
// 引入组件
import commentApi from '@/api/comment/comment'

export default {
  data() {
    return {
      commentData: null
    }
  },

  // 生命周期方法（在路由切换，组件不变的情况下不会被调用）
  created() {
    console.log('form created ......')
    this.init()
  },
  methods: {
    // 表单初始化
    init() {
      const id = this.$route.params.id
      console.log(id)
      this.fetchDataById(id)
    },
    // 根据id查询记录
    fetchDataById(id) {
      commentApi.getById(id).then(response => {
        console.log(response.data)
        this.commentData = response.data
      })
    },
    back() {
      this.$router.push({path: '/comment/list'})
    }
  }
}
</script>
