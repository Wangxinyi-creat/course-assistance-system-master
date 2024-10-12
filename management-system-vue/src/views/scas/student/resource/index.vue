<template>
  <div class="system-user-container layout-padding">
    <el-card shadow="hover" class="layout-padding-auto">
      <div class="system-user-search mb15">
        <el-input v-model="tableData.param.resourceName" clearable placeholder="请输入资源名称" size="medium" style="width: 200px;margin-left: 10px"></el-input>

        <el-button size="medium" type="primary" class="ml10" icon="el-icon-search" @click="getTableData()">
          查询
        </el-button>
      </div>

      <el-table :data="tableData.data" v-loading="tableData.loading" style="width: 100%;min-height:calc(100vh - 320px)">
        <el-table-column type="index" label="序号" align="center" width="60"/>
        <el-table-column prop="resourceName" label="资源名称" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="resourceDescription" label="资源描述" align="center"
                         show-overflow-tooltip></el-table-column>
        <el-table-column prop="resourceUrl" label="资源" align="center" show-overflow-tooltip>
          <template #default="scope">
            <a :href="scope.row.resourceUrl" target="_blank" v-if="scope.row.resourceUrl">查看资源</a>
          </template>
        </el-table-column>
        <el-table-column prop="uploadDate" label="上传日期" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="createBy" label="创建人" align="center" show-overflow-tooltip></el-table-column>
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
  </div>
</template>

<script>
import {getResource} from "@/api/scas/resource";

export default {
  name: 'Resource',
  props: {
    courseId: {
      type: Number,
      default: ''
    }
  },
  data() {
    return {
      tableData: {
        data: [],
        total: 0,
        loading: false,
        param: {
          courseId: '',
          resourceName: '',
          pageNum: 1,
          pageSize: 10,
        },
      }
    };
  },
  mounted() {
    this.tableData.param.courseId = this.courseId
    this.getTableData();
  },
  methods: {
    /**
     * 获取菜单
     */
    getTableData() {
      this.tableData.loading = true;
      getResource(this.tableData.param).then(res => {
        this.tableData.data = res.data.list;
        this.tableData.total = res.data.total;
        setTimeout(() => {
          this.tableData.loading = false;
        }, 500);
      })
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
  }
};
</script>

<style>
.el-tooltip__popper {
  max-width: 50%
}
</style>
