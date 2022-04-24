<template>
  <div class="app-container">
    <div class="el-toolbar">
      <div class="el-toolbar-body" style="justify-content: flex-start;">
        <el-button type="text" @click="exportData"><i class="fa fa-plus"/> 导出</el-button>
        <el-button type="text" @click="importData"><i class="fa fa-plus"/> 导入</el-button>
      </div>
    </div>

    <el-table
      :data="list"
      :load="getChildrens"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      style="width: 100%"
      row-key="id"
      border
      lazy
    > <!--数据必须有hasChildren这个属性-->
      <el-table-column label="名称" width="230" align="left">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>

      <el-table-column label="编码" width="220">
        <template slot-scope="{ row }">
          {{ row.dictCode }}
        </template>
      </el-table-column>
      <el-table-column label="值" width="230" align="left">
        <template slot-scope="scope">
          <span>{{ scope.row.value }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
    </el-table>
    <!--visible.sync：设置弹窗是否可见-->
    <el-dialog :visible.sync="dialogImportVisible" title="导入" width="480px">
      <el-form label-position="right" label-width="170px">
        <!--multiple:是否上传多个文件
            on-success：上传成功调用方法
            action：传输路径
        -->
        <el-form-item label="文件">
          <el-upload
            :multiple="false"
            :on-success="onUploadSuccess"
            :action="'http://localhost:8202/admin/cmn/dict/importData'"
            class="upload-demo">
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传xls文件，且不超过500kb</div>
          </el-upload>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogImportVisible = false">
          取消
        </el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import dict from '@/api/cmn/dict'
export default {
  data() {
    return {
      dialogImportVisible: false,
      list: [] // 数据字典列表数组
    }
  },
  created() {
    this.getDictList(1) // 查询所有数据
  },
  methods: {
    // 导出数据字典的数据
    exportData() {
      // 调用导出接口
      window.location.href = 'http://localhost:8202/admin/cmn/dict/exportData'
    },
    // 导入数据字典
    importData() {
      this.dialogImportVisible = true
    },
    // 上传成功调用方法
    onUploadSuccess() {
      // 关闭弹窗
      this.dialogImportVisible = false
      // 刷新页面
      this.getDictList(1)
    },
    // 数据字典列表
    getDictList(id) {
      dict.dictList(id).then((response) => {
        this.list = response.data
      })
    },
    // 查询下面层级的内容
    getChildrens(tree, treeNode, resolve) {
      dict.dictList(tree.id).then(response => {
        resolve(response.data)
      })
    }
  }
}
</script>
