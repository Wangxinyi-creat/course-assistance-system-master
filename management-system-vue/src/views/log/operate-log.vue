<template>
  <div class="system-user-container layout-padding">
    <el-card shadow="hover" class="layout-padding-auto">
      <div class="system-user-search mb15">
        <el-input size="medium" v-model="tableData.param.userName" placeholder="请输入账号名称"
                  style="max-width: 180px"></el-input>
        <el-button size="medium" type="primary" class="ml10" icon="el-icon-search" @click="search()">
          查询
        </el-button>
      </div>

      <el-table :data="tableData.data" v-loading="tableData.loading" border style="width: 100%;min-height:calc(100vh - 320px)">
        <el-table-column type="index" label="序号" align="center" width="60"/>
        <el-table-column prop="methodName" label="操作方法" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="userName" label="操作人" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="status" label="操作状态" align="center" show-overflow-tooltip>
          <template #default="scope">
            <el-tag type="success" v-if="scope.row.status === 1">成功</el-tag>
            <el-tag type="info" v-if="scope.row.status === 0">失败</el-tag>
            <el-tag type="danger" v-if="scope.row.status === 2">异常</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reqIp" label="请求IP" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="reqInterface" label="请求接口" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="reqType" label="请求方式" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="browser" label="操作浏览器类型" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="createTime" label="操作时间" align="center" width="200"></el-table-column>
        <el-table-column prop="expendTime" label="执行时长" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button size="small" type="text" @click="openDrawer(scope.row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
          @size-change="onHandleSizeChange"
          @current-change="onHandleCurrentChange"
          class="mt15"
          :pager-count="5"
          :page-sizes="[10, 20, 30]"
          v-model:current-page="tableData.param.pageNum"
          background
          v-model:page-size="tableData.param.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="tableData.total"
      >
      </el-pagination>
    </el-card>

    <el-drawer
        :with-header="false"
        :visible.sync="drawers.drawer"
        :direction="drawers.direction">
      <el-descriptions :title="drawers.title" direction="vertical" :column="2" border class="descriptions">
        <el-descriptions-item label="操作人" :span="2">{{ drawers.date.userName }}</el-descriptions-item>
        <el-descriptions-item label="操作系统类型" :span="2">{{ drawers.date.os }}</el-descriptions-item>
        <el-descriptions-item label="操作时间">{{ drawers.date.createTime }}</el-descriptions-item>
        <el-descriptions-item label="执行时长">{{ drawers.date.expendTime }}</el-descriptions-item>
        <el-descriptions-item label="请求参数" :span="2">
          <json-viewer :value="drawers.date.reqParam"></json-viewer>
        </el-descriptions-item>
        <el-descriptions-item label="返回参数" :span="2">
          <json-viewer :value="drawers.date.jsonResult"></json-viewer>
        </el-descriptions-item>
      </el-descriptions>
    </el-drawer>

  </div>
</template>

<script>
import {getOperate} from "@/api/log/log";
import JsonViewer from 'vue-json-viewer'

export default {
  name: 'operateLog',
  data() {
    return {
      tableData: {
        data: [],
        total: 0,
        loading: false,
        param: {
          userName: '',
          pageNum: 1,
          pageSize: 10,
        }
      },
      drawers: {
        title: '',
        drawer: false,
        direction: 'rtl',
        date: {}
      }
    };
  },
  components: {
    JsonViewer
  },
  mounted() {
    this.getTableData();
  },
  methods: {
    /**
     * 获取菜单
     */
    getTableData() {
      this.tableData.loading = true;
      getOperate(this.tableData.param).then(res => {
        this.tableData.data = res.data.list;
        this.tableData.total = res.data.total;
        setTimeout(() => {
          this.tableData.loading = false;
        }, 500);
      })
    },
    /**
     * 查询
     */
    search() {
      this.getTableData();
    },
    /**
     * 修改每页所存数据量的值所触发的函数
     * @param val
     */
    onHandleSizeChange(val) {
      // 修改页的大小
      this.tableData.param.pageSize = val;
      // 按新的pageNo和pageSize进行查询
      this.getTableData();
    },
    /**
     * 修改当前页所触发的函数
     * @param val
     */
    onHandleCurrentChange(val) {
      // 更新当前的页
      this.tableData.param.pageNum = val;
      // 按新的pageNo和pageSize进行查询
      this.getTableData();
    },
    /**
     * 打开详情弹框
     * @param row
     */
    openDrawer(row) {
      this.drawers.title = row.methodName + '-详请'
      this.drawers.drawer = true
      this.drawers.date = row
      this.drawers.date.reqParam = eval("(" + row.reqParam + ")");
      this.drawers.date.jsonResult = eval("(" + row.jsonResult + ")");
    }
  }
};
</script>

<style>
.descriptions {
  width: 95%;
  margin: 15px auto;
}

//抽屉从右往左开 隐藏滚动条
.el-drawer.rtl{
  overflow: auto;
}
.el-drawer__container ::-webkit-scrollbar{
  display: none;
}

*{scrollbar-width: none;
}

</style>
