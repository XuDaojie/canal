<template>
  <div>
    <el-form ref="form" :model="form">
      <div class="filter-container" style="padding-left: 10px;padding-top: 20px;">
        <el-input v-model="form.name" placeholder="Adapter 名称" style="width: 200px;" class="filter-item" />
        <el-input v-model="form.category" placeholder="Adapter 种类" style="width: 200px;" class="filter-item" />
        <el-button class="filter-item" type="primary" @click="onSubmit">保存</el-button>
        <el-button class="filter-item" type="success" @click="onLoadTemplate">载入模板</el-button>
        <el-button class="filter-item" type="info" @click="onBack">返回</el-button>
      </div>
      <editor v-model="form.content" lang="properties" theme="chrome" width="100%" :height="800" @init="editorInit" />
    </el-form>
  </div>
</template>

<script>
import { addCanalInstance, getTemplateAdapter } from '@/api/canalAdapter'
import { getClustersAndServers } from '@/api/canalCluster'
import {addCanalAdapter} from "../../api/canalAdapter";

export default {
  components: {
    editor: require('vue2-ace-editor')
  },
  data() {
    return {
      options: [],
      form: {
        name: '',
        content: '',
        clusterServerId: ''
      }
    }
  },
  created() {
    getClustersAndServers().then((res) => {
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
    onSubmit() {
      if (this.form.name === '') {
        this.$message({
          message: '请输入Adapter名称',
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
      if (this.form.content === null || this.form.content === '') {
        this.$message({
          message: '请输入配置内容',
          type: 'error'
        })
        return
      }
      this.$confirm(
        '确定新建',
        '确定新建',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(() => {
        addCanalAdapter(this.form).then(response => {
          if (response.data === 'success') {
            this.$message({
              message: '新建成功',
              type: 'success'
            })
            this.$router.push('/canalClient/canalAdapters')
          } else {
            this.$message({
              message: '新建失败',
              type: 'error'
            })
          }
        })
      })
    },
    onBack() {
      history.go(-1)
    },
    onLoadTemplate() {
      console.log("xx")
      getTemplateAdapter(this.form).then(res => {
        if (res.data === null || res.data === '') {
          this.$message({
            message: '未找到此种类模版',
            type: 'error'
          })
        }
        this.form.content = res.data
      })
    }
  }
}
</script>

<style scoped>
.line{
  text-align: center;
}
</style>

