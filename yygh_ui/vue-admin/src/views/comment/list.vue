<template>
  <div class="app-container">
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="searchObj.hosname" placeholder="医院名称"/>
      </el-form-item>
      <el-form-item>
        <el-input v-model="searchObj.depname" placeholder="科室名称"/>
      </el-form-item>
      <el-form-item>
        <el-input v-model="searchObj.title" placeholder="医生职称"/>
      </el-form-item>
      <el-form-item>
        <el-date-picker
          v-model="searchObj.createTimeBegin"
          type="date"
          placeholder="选择开始日期"
          value-format="yyyy-MM-dd"/>
      </el-form-item>
      <el-form-item>
        <el-date-picker
          v-model="searchObj.createTimeEnd"
          type="date"
          placeholder="选择截止日期"
          value-format="yyyy-MM-dd"/>
      </el-form-item>
      <el-form-item>
        <el-date-picker
          v-model="searchObj.reserveDate"
          type="date"
          placeholder="就诊日期"
          value-format="yyyy-MM-dd"/>
      </el-form-item>
      <el-form-item>
        <el-select v-model="searchObj.status" placeholder="评论状态" class="v-select patient-select">
          <el-option
            v-for="item in statusList"
            :key="item.status"
            :label="item.message"
            :value="item.status">
          </el-option>
        </el-select>
      </el-form-item>
      <el-button type="primary" icon="el-icon-search" @click="fetchData()">查询</el-button>
      <el-button type="default" @click="resetData()">清空</el-button>
      <el-button type="danger" @click="removeRows()">批量删除</el-button>
    </el-form>
    <!-- 列表 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"/>
      <el-table-column
        label="序号"
        width="60"
        align="center">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column prop="orderId" label="订单号" width="80" align="center"/>
      <el-table-column prop="hosname" label="医院名称" width="160" align="center"/>
      <el-table-column prop="reserveDate" label="就诊日期" width="100" align="center"/>
      <el-table-column prop="depname" label="科室名称" width="160" align="center"/>
      <el-table-column prop="title" label="医生职称" width="100" align="center"/>
      <el-table-column label="安排时间" width="130" align="center">
        <template slot-scope="scope">
          {{ scope.row.reserveDate }}
        </template>
      </el-table-column>
      <el-table-column prop="param.commentStatus" label="评论状态" align="center"/>
      <el-table-column prop="createTime" label="评论时间" width="156" align="center"/>
      <el-table-column label="操作" width="360" align="center">
        <template slot-scope="scope">
          <router-link :to="'/comment/show/'+scope.row.id">
            <el-button type="primary" size="mini" icon="el-icon-edit">查看</el-button>
          </router-link>
          <el-button type="primary" size="mini" icon="el-icon-edit" @click="changeStatus(scope.row.id, 1)">通过
          </el-button>
          <el-button type="danger" size="mini" icon="el-icon-edit" @click="changeStatus(scope.row.id, -1)">不通过
          </el-button>
          <el-button type="danger" size="mini" icon="el-icon-edit" @click="deleteOne(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
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
</template>
<script>
import commentApi from '@/api/comment/comment'

export default {
  data() {
    return {
      statusList: [
        {
          status: 0,
          message: '待审核'
        },
        {
          status: 1,
          message: '通过'
        },
        {
          status: -1,
          message: '未通过'
        }
      ],
      multipleSelection: [],
      listLoading: true, // 数据是否正在加载
      list: null, // banner列表
      total: 0, // 数据库中的总记录数
      page: 1, // 默认页码
      limit: 10, // 每页记录数
      searchObj: {} // 查询表单对象
    }
  },
  // 生命周期函数：内存准备完毕，页面尚未渲染
  created() {
    this.fetchData()
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
      commentApi.getCommentPageList(this.page, this.limit, this.searchObj).then(
        response => {
          debugger
          this.list = response.data.records
          this.total = response.data.total
          // 数据加载并绑定成功
          this.listLoading = false
        }
      )
    },

    // 选择框变化处理
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    // 重置查询表单
    resetData() {
      console.log('重置查询表单')
      this.searchObj = {}
      this.fetchData()
    },

    // 修改评论状态
    changeStatus(id, status) {
      commentApi.changeStatus(id, status).then(response => {
        // 刷新页面
        this.$message.warning('修改成功')
        this.fetchData(this.page)
      })
    },

    // 删除评论
    deleteOne(id) {
      this.$confirm('此操作将永久删除评论信息，是否继续？', '提示', {
        confirmButtonText: '确实',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {  //确定执行then方法
        //调用删除接口
        commentApi.deleteById(id)
          .then(response => {
            this.$message({
              type: 'success',
              message: '删除成功！'
            })
            //刷新页面
            this.fetchData(1)
          })
      })
    },

    // 批量删除
    removeRows() {
      this.$confirm('此操作将永久评论信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => { //确定执行then方法
        var idList = []
        //遍历数组得到每个id值，设置到idList里面
        for (var i = 0; i < this.multipleSelection.length; i++) {
          var obj = this.multipleSelection[i]
          var id = obj.id
          idList.push(id)
        }
        //调用接口
        commentApi.batchRemoveHospitalSet(idList)
          .then(response => {
            //提示
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            //刷新页面
            this.fetchData(1)
          })
      })
    }
  }
}
</script>
