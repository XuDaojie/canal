<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.name" placeholder="Adapter 名称" style="width: 200px;" class="filter-item" />
      <el-button class="filter-item" type="primary" icon="el-icon-search" plain @click="queryData()">查询</el-button>
      &nbsp;&nbsp;
      <el-button class="filter-item" type="primary" @click="handleCreate()">新建 Adapter</el-button>
      <el-button class="filter-item" type="info" @click="fetchData()">刷新列表</el-button>
    </div>
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column label="Adapter 名称" min-width="200" align="center">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column label="Adapter 种类" min-width="200" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.canalCluster !== null">
            {{ scope.row.category }}
          </span>
        </template>
      </el-table-column>
      <el-table-column class-name="status-col" label="状态" min-width="150" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.runningStatus | statusFilter">{{ scope.row.runningStatus | statusLabel }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="修改时间" min-width="200" align="center">
        <template slot-scope="scope">
          {{ scope.row.modifiedTime }}
        </template>
      </el-table-column>
      <el-table-column align="center" prop="created_at" label="操作" min-width="150">
        <template slot-scope="scope">
          <el-dropdown trigger="click">
            <el-button type="primary" size="mini">
              操作<i class="el-icon-arrow-down el-icon--right" />
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="handleUpdate(scope.row)">修改</el-dropdown-item>
              <el-dropdown-item @click.native="handleDelete(scope.row)">删除</el-dropdown-item>
              <el-dropdown-item @click.native="handleStart(scope.row)">启动</el-dropdown-item>
              <el-dropdown-item @click.native="handleStop(scope.row)">停止</el-dropdown-item>
              <el-dropdown-item @click.native="handleLog(scope.row)">日志</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="count>0" :total="count" :page.sync="listQuery.page" :limit.sync="listQuery.size" @pagination="fetchData()" />
<!--    <el-dialog :visible.sync="dialogFormVisible" :title="textMap[dialogStatus]" width="600px">-->
<!--      <el-form ref="dataForm" :rules="rules" :model="nodeModel" label-position="left" label-width="120px" style="width: 400px; margin-left:30px;">-->
<!--        <el-form-item label="Adapter 种类" prop="clusterId">-->
<!--          <el-select v-if="dialogStatus === 'create'" v-model="nodeModel.clusterId" placeholder="选择Adapter">-->
<!--            <el-option key="" label="单机" value="" />-->
<!--            <el-option v-for="item in canalClusters" :key="item.id" :label="item.name" :value="item.id" />-->
<!--          </el-select>-->
<!--          <el-select v-else v-model="nodeModel.clusterId" placeholder="选择所属集群" disabled="disabled">-->
<!--            <el-option key="" label="单机" value="" />-->
<!--            <el-option v-for="item in canalClusters" :key="item.id" :label="item.name" :value="item.id" />-->
<!--          </el-select>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="Server 名称" prop="name">-->
<!--          <el-input v-model="nodeModel.name" />-->
<!--        </el-form-item>-->
<!--      </el-form>-->
<!--      <div slot="footer" class="dialog-footer">-->
<!--        <el-button @click="dialogFormVisible = false">取消</el-button>-->
<!--        <el-button type="primary" @click="dataOperation()">确定</el-button>-->
<!--      </div>-->
<!--    </el-dialog>-->
  </div>
</template>

<script>
import { getCanalAdapters, deleteCanalInstance, instanceStatus } from '@/api/canalAdapter'
import Pagination from '@/components/Pagination'
import { getClustersAndServers } from '@/api/canalCluster'
import {deleteCanalAdapter} from "../../api/canalAdapter";

export default {
  components: { Pagination },
  filters: {
    statusFilter(status) {
      const statusMap = {
        '1': 'success',
        '0': 'gray'
      }
      return statusMap[status]
    },
    statusLabel(status) {
      const statusMap = {
        '1': '启动',
        '0': '停止'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      list: null,
      listLoading: true,
      dialogFormVisible: false,
      nodeServices: [],
      count: 0,
      options: [],
      listQuery: {
        name: '',
        page: 1,
        size: 20
      },
      currentId: null,
      rules: {
        id: [{ required: true, message: '请选择运行Server', trigger: 'change' }]
      }
    }
  },
  created() {
    getClustersAndServers().then((res) => {
      this.options = res.data
    })
    this.fetchData()
  },
  methods: {
    queryData() {
      this.listQuery.page = 1
      this.fetchData()
    },
    fetchData() {
      this.listLoading = true
      getCanalAdapters(this.listQuery).then(res => {
        this.list = res.data.items
        this.count = res.data.count
      }).finally(() => {
        this.listLoading = false
      })
    },
    handleCreate() {
      this.$router.push('/canalClient/canalAdapter/add')
    },
    handleUpdate(row) {
      this.$router.push('/canalClient/canalAdapter/modify?id=' + row.id)
    },
    handleDelete(row) {
      this.$confirm('删除Instance配置会导致停止', '确定删除Instance信息', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteCanalAdapter(row.id).then((res) => {
          if (res.data === 'success') {
            this.fetchData()
            this.$message({
              message: '删除Adapter信息成功',
              type: 'success'
            })
          } else {
            this.$message({
              message: '删除Adapter信息失败',
              type: 'error'
            })
          }
        })
      })
    },
    handleStart(row) {
      // if (row.runningStatus === '1') {
      //   this.$message({ message: '当前Instance已处于启动状态！', type: 'error' })
      //   return
      // }
      this.$confirm('启动Instance: ' + row.name, '确定启动Instance服务', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        instanceStatus(row.id, 'start').then((res) => {
          if (res.data) {
            this.fetchData()
            this.$message({
              message: '启动成功, 稍后请刷新列表查看状态',
              type: 'success'
            })
          } else {
            this.$message({
              message: '启动Instance出现异常',
              type: 'error'
            })
          }
        })
      })
    },
    handleStop(row) {
      // if (row.runningStatus === '0') {
      //   this.$message({ message: '当前Instance已处于停止状态！', type: 'error' })
      //   return
      // }
      this.$confirm('停止Instance: ' + row.name, '确定停止Instance服务', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        instanceStatus(row.id, 'stop').then((res) => {
          if (res.data) {
            this.fetchData()
            this.$message({
              message: '停止成功, 稍后请刷新列表查看状态',
              type: 'success'
            })
          } else {
            this.$message({
              message: '停止Instance出现异常',
              type: 'error'
            })
          }
        })
      })
    },
    handleLog(row) {
      if (row.nodeId === null) {
        this.$message({ message: '当前Instance不是启动状态，无法查看日志', type: 'warning' })
        return
      }
      this.$router.push('canalInstance/log?id=' + row.id + '&nodeId=' + row.nodeServer.id)
    }
  }
}
</script>
