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

      <el-table :data="tableData.data"
                v-loading="tableData.loading"
                border
                :row-style="{height: '60px'}"
                :cell-style="{padding: '3px'}"
                style="width: 100%;min-height:calc(100vh - 320px)">
        <el-table-column type="index" label="序号" align="center" width="60"/>
        <el-table-column prop="userName" label="登录账号" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="ipaddr" label="登录IP地址" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="loginLocation" label="登录地点" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="browser" label="浏览器类型" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="os" label="操作系统" align="center" show-overflow-tooltip width="350"></el-table-column>
        <el-table-column prop="loginTime" label="访问时间" align="center" show-overflow-tooltip></el-table-column>
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
import {getLogin} from "@/api/log/log";

export default {
  name: 'LoginLog',
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
        },
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
      getLogin(this.tableData.param).then(res => {
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
  }
};
</script>
