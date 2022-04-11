<template>
  <div class="app-container">
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="searchObj.hosname" placeholder="医院名称"/>
      </el-form-item>
      <el-form-item>
        <el-input v-model="searchObj.hoscode" placeholder="医院编号"/>
      </el-form-item>
      <el-button type="primary" icon="el-icon-search" @click="getList()">查询</el-button>
    </el-form>

    <!-- 工具条,批量删除按钮 -->
    <div>
      <el-button type="danger" size="mini" @click="removeRows()">批量删除</el-button>
    </div>
    <!--@selection-change为选择复选框触发的方法-->
    <el-table :data="list" :row-key="getRowKeys" stripe style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column :reserve-selection="true" type="selection" width="55" /> <!--复选框-->
      <el-table-column type="index" width="50" />
      <el-table-column prop="hosname" label="医院名称" />
      <el-table-column prop="hoscode" label="医院编号" />
      <el-table-column prop="apiUrl" label="api基础路径" width="200" />
      <el-table-column prop="contactsName" label="联系人姓名" />
      <el-table-column prop="contactsPhone" label="联系人手机" />
      <el-table-column label="状态" width="80">
        <template slot-scope="scope"> <!--scope相当于是tableData的一行-->
          {{ scope.row.status === 1 ? "可用" : "不可用" }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="280" align="center">
        <template slot-scope="scope">
          <el-button type="danger" size="mini" icon="el-icon-delete" @click="removeDataById(scope.row.id)"/>
          <el-button v-if="scope.row.status==1" type="primary" size="mini" icon="el-icon-delete" @click="lockHostSet(scope.row.id,0)">锁定</el-button>
          <el-button v-if="scope.row.status==0" type="danger" size="mini" icon="el-icon-delete" @click="lockHostSet(scope.row.id,1)">取消锁定</el-button>
          <router-link :to="'/hospSet/edit/' + scope.row.id">
            <el-button type="primary" size="mini" icon="el-icon-edit"/>
          </router-link>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <el-pagination
      :current-page="current"
      :page-size="limit"
      :total="total"
      style="padding: 30px 0; text-align: center;"
      layout="total, prev, pager, next, jumper"
      @current-change="getList"
    />

  </div>
</template>

<script>
// 引入接口定义的js文件
import hospset from '@/api/hospset'
export default {
  // 定义变量和初始值
  data() {
    return {
      current: 1, // 当前页
      limit: 3, // 每页显示记录数
      searchObj: {}, // 条件封装对象
      list: [], // 每页数据集合
      total: 0, // 总记录数
      multipleSelection: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    // 医院设置列表
    getList(page = 1) { // 添加当前页参数
      this.current = page
      hospset
        .getHospSetList(this.current, this.limit, this.searchObj)
        .then((response) => {
          console.log(response)
          this.list = response.data.records // 返回集合赋值list
          this.total = response.data.total // 总记录数
        })
        .catch((error) => {
          console.log(error)
        })
    },
    // 删除医院设置
    removeDataById(id) {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => { // 选择确定执行then方法
        hospset.deleteHospSet(id)
          .then(() => {
            // 提示
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            // 刷新页面
            this.getList()
          }).catch(error => {
            console.log(error)
          })
      })
    },
    // 批量删除
    removeRows() {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => { // 选择确定执行then方法
        var idList = []
        // 获取所有对象的id值
        for (var i = 0; i < this.multipleSelection.length; i++) {
          idList.push(this.multipleSelection[i].id)
        }
        hospset.batchHospSet(idList)
          .then(() => {
            // 提示
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            // 刷新页面
            this.getList()
          }).catch(error => {
            console.log(error)
          })
      })
    },
    // 获取选择复选框的id值
    handleSelectionChange(selection) {
      this.multipleSelection = selection
      console.log(this.multipleSelection)
    },
    // 锁定和取消锁定
    lockHostSet(id, status) {
      hospset.lockHospSet(id, status)
        .then(response => {
          // 刷新页面
          this.getList(this.current)
        })
    },
    // 获取row的key值,为了实现分页时保存上一页的选择
    getRowKeys(row) {
      return row.id
    }
  }
}
</script>
