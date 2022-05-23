<template>
  <div class="app-container">
    <el-form :inline="true" class="demo-form-inline">
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
      <el-table-column prop="createTime" label="创建时间" width="180" align="center"/>
      <el-table-column prop="feedback" label="反馈信息" width="280" align="center"/>
      <el-table-column prop="param.status" label="反馈状态" width="180" align="center"/>
      <el-table-column prop="reply" label="回复信息" width="380" align="center"/>
      <el-table-column label="操作" width="360" align="center">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" icon="el-icon-edit"
                     @click="show(scope.$index)">查看
          </el-button>
          <el-button type="danger" size="mini" icon="el-icon-edit" @click="deleteOne(scope.row.id)">删除</el-button>
          <el-button type="danger" size="mini" icon="el-icon-edit" @click="change(scope.row.id, 0)" v-if="scope.row.status === 1">修改</el-button>
          <el-button type="primary" size="mini" icon="el-icon-edit" @click="change(scope.row.id, 1)" v-if="scope.row.status === 0">修改</el-button>
          <el-button type="primary" size="mini" icon="el-icon-edit" @click="replyFeedback(scope.row.id)">回复</el-button>
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
import feedApi from '@/api/feedback/feedback'

export default {
  data() {
    return {
      statusList: [
        {
          status: 0,
          message: '待解决'
        },
        {
          status: 1,
          message: '已解决'
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
      feedApi.getFeedbackPageList(this.page, this.limit, this.searchObj).then(
        response => {
          debugger
          this.list = response.data.records
          this.total = response.data.total
          // 数据加载并绑定成功
          this.listLoading = false
        }
      )
    },

    // 重置查询表单
    resetData() {
      console.log('重置查询表单')
      this.searchObj = {}
      this.fetchData()
    },

    // 删除评论
    deleteOne(id) {
      this.$confirm('此操作将永久删除评论信息，是否继续？', '提示', {
        confirmButtonText: '确实',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {  //确定执行then方法
        //调用删除接口
        feedApi.deleteFeedback(id)
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

    // 选择框变化处理
    handleSelectionChange(selection) {
      this.multipleSelection = selection
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
        feedApi.batchRemoveHospitalSet(idList)
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
    },

    show(index) {
      this.$alert(this.list[index].feedback, '反馈信息', {
        confirmButtonText: '确定'
      });
    },

    change(id, status) {
      feedApi.changeStatus(id, status).then(response => {
        this.$message.warning('修改成功！')
        //刷新页面
        this.fetchData(1)
      })
    },

    replyFeedback(id) {
      this.$prompt('请输入回复信息', '回复', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^.+$/,
        inputErrorMessage: '不能为空！'
      }).then(({ value }) => {
        feedApi.replyFeedback(id, value).then(response => {
          this.$message.warning('回复成功！')
          //刷新页面
          this.fetchData(1)
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消输入'
        });
      });
    }
  }
}
</script>
