<template>
  <div>
    <el-form ref="form" :model="form">
      <div style="padding-left: 10px;padding-top: 20px;">
        <el-form-item>
          {{ form.name }}&nbsp;&nbsp;&nbsp;&nbsp;
          <el-select v-model="form.category" placeholder="请选择 Adapter 种类" class="filter-item">
            <el-option key="hbase" label="HBase" value="hbase" />
            <el-option key="rdb" label="RDB" value="rdb" />
            <el-option key="es6x" label="Elastic Search 6.x" value="es6x" />
            <el-option key="es7x" label="Elastic Search 7.x" value="es7x" />
            <el-option key="kudu" label="Apache Kudu" value="kudu" />
          </el-select>
          <el-select v-model="form.clusterClientId" placeholder="所属主机" class="filter-item">
            <el-option-group v-for="group in options" :key="group.label" :label="group.label">
              <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.value" />
            </el-option-group>
          </el-select>
          <el-button type="primary" @click="onSubmit">修改</el-button>
          <el-button type="warning" @click="onCancel">重置</el-button>
          <el-button type="info" @click="onBack">返回</el-button>
        </el-form-item>
      </div>
      <editor v-model="form.content" lang="properties" theme="chrome" width="100%" :height="800" @init="editorInit" />
    </el-form>
  </div>
</template>

<script>
import { canalAdapterDetail, updateCanalAdapter } from '@/api/canalAdapter'
import { getClustersAndClients } from '@/api/canalCluster'

export default {
  components: {
    editor: require('vue2-ace-editor')
  },
  data() {
    return {
      options: [],
      form: {
        id: null,
        name: '',
        content: '',
        clientId: ''
      }
    }
  },
  created() {
    this.loadCanalConfig()
    getClustersAndClients().then((res) => {
      this.options = res.data
    })
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
    loadCanalConfig() {
      canalAdapterDetail(this.$route.query.id).then(response => {
        const data = response.data
        this.form.id = data.id
        this.form.name = data.name
        this.form.category = data.category
        this.form.content = data.content
        this.form.clusterClientId = data.clusterClientId
      })
    },
    onSubmit() {
      this.$confirm(
        '修改Adapter配置可能会导致重启，是否继续？',
        '确定修改',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(() => {
        updateCanalAdapter(this.form).then(response => {
          if (response.data === 'success') {
            this.$message({
              message: '修改成功',
              type: 'success'
            })
            this.loadCanalConfig()
          } else {
            this.$message({
              message: '修改失败',
              type: 'error'
            })
          }
        })
      })
    },
    onCancel() {
      this.loadCanalConfig()
    },
    onBack() {
      history.go(-1)
    }
  }
}
</script>

<style scoped>
.line{
  text-align: center;
}
</style>

