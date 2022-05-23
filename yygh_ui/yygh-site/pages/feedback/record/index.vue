<template>
  <!-- header -->
  <div class="page-component nav-container">
    <!--左侧导航 #start -->
    <div class="nav left-nav">
      <div class="nav-item">
        <span class="v-link clickable dark" onclick="window.location='/feedback'"> 意见反馈 </span>
      </div>
      <div class="nav-item selected">
        <span class="v-link selected dark" onclick="window.location='/feedback/record'"> 反馈记录 </span>
      </div>
    </div>
    <!-- 左侧导航 #end -->

    <div class="page-container">
      <div class="hospital-detail">
        <h2>反馈记录</h2>
        <br>
        <br>
        <el-table
          :data="list"
          stripe
          style="width: 100%">
          <el-table-column
            prop="createTime"
            label="反馈时间"
            width="280">
          </el-table-column>
          <el-table-column
            prop="feedback"
            label="反馈内容"
            width="280">
          </el-table-column>
          <el-table-column
            prop="param.status"
            label="反馈状态"
            width="180">
          </el-table-column>
          <el-table-column
            prop="reply"
            label="回复信息"
            width="280">
          </el-table-column>
        </el-table>
        <br>
        <br>
        <!-- 分页组件 -->
        <el-pagination
          :current-page="page"
          :total="total"
          :page-size="limit"
          :page-sizes="[5, 10, 20, 30, 40, 50, 100]"
          style="padding: 30px 0; text-align: center;"
          layout="sizes, prev, pager, next, jumper, ->, total, slot"
          @current-change="fetchData"
          @size-change="changeSize"
        />
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
      feedback: {},
      list: [],
      page: 1,
      limit: 10,
      total: 0
    }
  },
  created() {
    this.fetchData();
  },
  methods: {
    // 当页码发生改变的时候
    changeSize(size) {
      console.log(size)
      this.limit = size
      this.fetchData(1)
    },

    // 加载banner列表数据
    fetchData(page = 1) {
      console.log('翻页。。。' + page)
      // 异步获取远程数据（ajax）
      this.page = page
      feedApi.getFeedback(this.page, this.limit).then(
        response => {
          this.list = response.data.records
          this.total = response.data.total
        }
      )
    },
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
