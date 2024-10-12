<template>
  <div class="system-user-container layout-padding">
    <el-card shadow="hover" class="layout-padding-auto">
      <div class="system-user-search mb15">
        <el-form :model="tableData.param" ref="queryForm" size="small" :inline="true">
          <el-form-item prop="userName">
            <el-input size="medium" v-model="tableData.param.userName" placeholder="请输入账号名称"
                      style="max-width: 180px" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-button size="medium" type="primary" class="ml10" icon="el-icon-search" @click="search()">
              查询
            </el-button>
            <el-button size="medium" type="danger" class="ml10" icon="el-icon-circle-close"
                       @click="revoke()">
              取消授权
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table :data="tableData.data" @selection-change="handleSelectionChange" v-loading="tableData.loading"
                style="width: 100%;min-height:calc(100vh - 320px)">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column type="index" label="序号" align="center" width="60"/>
        <el-table-column prop="userName" label="账户名称" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="nickName" label="用户昵称" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="sex" label="性别" align="center" show-overflow-tooltip>
          <template #default="scope">
            <span>{{ scope.row.sex == 1 ? '男' : '女' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="email" label="邮箱" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="status" label="用户状态" align="center" show-overflow-tooltip>
          <template #default="scope">
            <el-tag v-if="scope.row.status" type="success">启用</el-tag>
            <el-tag v-else type="danger">停用</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createBy" label="创建人" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="createTime" label="创建时间" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="updateBy" label="修改人" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="updateTime" label="修改时间" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column label="操作" align="center" width="150px">
          <template #default="scope">
            <el-button size="small"
                       type="text"
                       icon="el-icon-circle-close"
                       @click="onRowDel(scope.row)">取消授权
            </el-button>
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
  </div>
</template>

<script>
import {Message, MessageBox} from "element-ui";
import {cancelUserRole, deleteUser, getUserByRoleId} from "@/api/system/user";

export default {
  name: 'empower',
  data() {
    return {
      tableData: {
        data: [],
        total: 0,
        loading: false,
        param: {
          userName: '',
          roleId: '',
          pageNum: 1,
          pageSize: 10,
        }
      },
      userNameList: []
    };
  },
  mounted() {
    this.tableData.param.roleId = this.$route.query.id
    this.getTableData();
  },
  methods: {
    /**
     * 获取菜单
     */
    getTableData() {
      this.tableData.loading = true;
      getUserByRoleId(this.tableData.param).then(res => {
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
     * 取消授权
     */
    revoke() {
      if (!this.userNameList.length > 0) {
        Message.info("请选择要取消授权的用户")
        return
      }
      cancelUserRole(this.userNameList).then(res => {
        this.getTableData();
        Message.success(res.message);
      })
    },
    /**
     * 删除用户
     * @param row
     */
    onRowDel(row) {
      //提示
      MessageBox.confirm(`此操作将取消账户名称：“${row.userName}”的权限，是否继续?`, '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        //调用删除方法
        const ids = []
        ids.push(row.userName)
        cancelUserRole(ids).then(res => {
          this.getTableData();
          Message.success("删除成功");
        })
      }).catch(() => {
      });
    },
    handleSelectionChange(val) {
      this.userNameList = []
      val.forEach(item => {
        this.userNameList.push(item.userName)
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
