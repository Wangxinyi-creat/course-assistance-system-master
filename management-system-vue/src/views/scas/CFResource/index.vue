<template>
  <div class="system-user-container layout-padding">
    <el-card shadow="hover" class="layout-padding-auto">
      <el-table :data="tableData.data" v-loading="tableData.loading" style="width: 100%;min-height:calc(100vh - 320px)">
        <el-table-column type="index" label="序号" align="center" width="60"/>
        <el-table-column prop="courseName" label="课程" align="center" show-overflow-tooltip></el-table-column>
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
    </el-card>
  </div>
</template>

<script>
import {getRecommendResource} from "@/api/scas/CFResource";

export default {
  name: 'Resource',
  data() {
    return {
      tableData: {
        data: [],
        loading: false,
      }
    };
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
      getRecommendResource().then(res => {
        this.tableData.data = res.data;
        setTimeout(() => {
          this.tableData.loading = false;
        }, 500);
      })
    }
  }
};
</script>

<style>
.el-tooltip__popper {
  max-width: 50%
}
</style>
