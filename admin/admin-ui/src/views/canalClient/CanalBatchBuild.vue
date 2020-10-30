<template>
  <div class="app-container">
    <div class="filter-container">
      <el-select v-model="form.clusterClientId" placeholder="所属主机" class="filter-item">
        <el-option-group v-for="group in options" :key="group.label" :label="group.label">
          <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.value" />
        </el-option-group>
      </el-select>
      <el-select v-model="listQuery.schema" placeholder="schemas" class="filter-item" @change="queryData()">
        <el-option v-for="item in schemas" :key="item.schemaName" :label="item.schemaName" :value="item.schemaName" />
      </el-select>
      <el-select v-model="form.category" placeholder="请选择 Adapter 种类" class="filter-item">
        <el-option key="hbase" label="HBase" value="hbase" />
        <el-option key="rdb" label="RDB" value="rdb" />
        <el-option key="es6x" label="Elastic Search 6.x" value="es6x" />
        <el-option key="es7x" label="Elastic Search 7.x" value="es7x" />
        <el-option key="kudu" label="Apache Kudu" value="kudu" />
      </el-select>
      <el-button class="filter-item" type="primary" @click="handleTemplate()">编辑模板</el-button>
      <el-button class="filter-item" type="primary" @click="dialogTipVisible = true">保存</el-button>
      <el-button class="filter-item" type="info" @click="fetchData()">刷新列表</el-button>
      <el-button class="filter-item" type="info" @click="onBack">返回</el-button>
    </div>
    <el-table
      ref="multipleTable"
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column
        type="selection"
        width="40">
      </el-table-column>
      <el-table-column label="Table" min-width="200" align="center">
        <template slot-scope="scope">
          {{ scope.row.tableSchema }}
        </template>
      </el-table-column>
      <el-table-column label="Table" min-width="200" align="center">
        <template slot-scope="scope">
            {{ scope.row.tableName }}
        </template>
      </el-table-column>
      <el-table-column label="操作" min-width="200" align="center">
        <template slot-scope="scope">
          <el-dropdown trigger="click">
            <el-button type="primary" size="mini">
              操作<i class="el-icon-arrow-down el-icon--right" />
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="previewTemplate(scope.row)">预览</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :visible.sync="dialogTemplateVisible" title="Adapter 模板 (Thymeleaf)" width="800px">
      <div class="filter-container">
        <el-button class="filter-item" type="info" @click="handleSaveTemplate()">保存</el-button>
        <el-button class="filter-item" type="success" @click="onLoadTemplate()">载入模板</el-button>
      </div>
      <editor v-model="form.content" lang="properties" theme="chrome" width="100%" :height="800" @init="editorInit" />
    </el-dialog>
    <el-dialog :visible.sync="dialogTemplatePreviewVisible" title="Adapter 模板" width="800px">
      <editor v-model="previewContent" lang="properties" theme="chrome" width="100%" :height="800" @init="editorInit" />
    </el-dialog>
    <el-dialog :visible.sync="dialogTemplatePreviewVisible" title="Adapter 模板" width="800px">
      <editor v-model="previewContent" lang="properties" theme="chrome" width="100%" :height="800" @init="editorInit" />
    </el-dialog>
    <el-dialog
      title="提示"
      :visible.sync="dialogTipVisible"
      width="30%">
      <span>确定要批量新增Adapter？</span>
      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogTipVisible = false">取 消</el-button>
    <el-button type="primary" @click="handleCreate">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
import { batchAddCanalAdapter, getTemplateEngineAdapter, previewTemplateEngineAdapter } from '@/api/canalAdapter'
import { getSchemas, getTables } from '@/api/srcDataSource'
import { getClustersAndClients } from '@/api/canalCluster'

export default {
  components: {
    editor: require('vue2-ace-editor')
  },
  filters: {
  },
  data() {
    return {
      list: [],
      listLoading: true,
      schemas: [],
      form: {
        category: '',
        clusterClientId: '',
        content: ''
      },
      options: [],
      dialogTemplatePreviewVisible: false,
      dialogTemplateVisible: false,
      dialogTipVisible: false,
      previewContent: '',
      listQuery: {
        schema: null
      }
    }
  },
  created() {
    getClustersAndClients().then((res) => {
      this.options = res.data
    })
    getSchemas().then(res => {
      this.schemas = res.data
    })
    this.fetchData()
  },
  methods: {
    editorInit() {
      require('brace/ext/language_tools')
      require('brace/mode/html')
      require('brace/mode/yaml')
      require('brace/mode/properties')
      require('brace/mode/javascript')
      require('brace/mode/less')
      require('brace/theme/chrome')
      require('brace/snippets/javascript')
    },
    queryData() {
      this.fetchData()
    },
    fetchData() {
      this.listLoading = true
      getTables(this.listQuery.schema).then(res => {
        this.list = res.data
      }).finally(() => {
        this.listLoading = false
      })
    },
    handleCreate() {
      const multipleSelection = this.$refs.multipleTable.selection;
      if (multipleSelection.length === 0) {
        this.$message({
          message: '请选择Table',
          type: 'error'
        })
        return;
      }

      const body = []
      multipleSelection.forEach(item => {
        body.push({
          name: item.tableSchema + '::' + item.tableName + '.yml',
          clusterClientId: this.form.clusterClientId,
          category: this.form.category,
          content: this.form.content
        })
      })
      if (this.form.clusterClientId === '') {
        this.$message({
          message: '请选择集群/主机',
          type: 'error'
        })
        return
      }
      if (this.form.category === '') {
        this.$message({
          message: '请输入Adapter种类',
          type: 'error'
        })
        return
      }
      if (this.form.content === '') {
        this.$message({
          message: '请编辑模板',
          type: 'error'
        })
        return
      }
      batchAddCanalAdapter(body).then(res => {
        this.$router.push('/canalClient/canalAdapters')
      })
    },
    handleTemplate() {
      if (this.form.category === '') {
        this.$message({
          message: '请输入Adapter种类',
          type: 'error'
        })
        return
      }

      this.dialogTemplateVisible = true
    },
    onLoadTemplate() {
      const params = {
        category: this.form.category
      }
      getTemplateEngineAdapter(params).then(res => {
        if (res.data === null || res.data === '') {
          this.$message({
            message: '未找到预留模版，请自行编辑',
            type: 'error'
          })
        }
        this.form.content = res.data
      })
    },
    handleSaveTemplate() {
      this.dialogTemplateVisible = false
    },
    previewTemplate(row) {
      this.dialogTemplatePreviewVisible = true
      const body = {
        name: row.tableSchema + '::' + row.tableName + '.yml',
        clusterClientId: this.form.clusterClientId,
        category: this.form.category,
        content: this.form.content
      }
      previewTemplateEngineAdapter(body).then( res => {
        this.previewContent = res.data
      })
    },
    onBack() {
      history.go(-1)
    },
  }
}
</script>
