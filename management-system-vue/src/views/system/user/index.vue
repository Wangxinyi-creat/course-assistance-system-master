<template>
  <div class="system-user-container layout-padding">
    <el-card shadow="hover" class="layout-padding-auto">
      <div class="system-user-search mb15">
        <el-form :model="tableData.param" ref="queryForm" size="small" :inline="true">
          <el-form-item prop="userName">
            <el-input size="medium" v-model="tableData.param.userName" placeholder="请输入账号名称"
                      style="max-width: 180px"></el-input>
          </el-form-item>
          <el-form-item prop="userName">
            <el-cascader :props="{value:'id', label: 'deptName', children: 'children',emitPath: false}"
                         :options="deptList"
                         v-model="tableData.param.deptId"
                         style="max-width: 180px"
                         props.expandTrigger="hover"
                         clearable
                         size="medium"
                         placeholder="请选择所属学院">
            </el-cascader>
          </el-form-item>
          <el-form-item>
            <dictSelect v-model="tableData.param.identity" dictType="Identity" placeholder="请选择身份" size="medium"></dictSelect>
          </el-form-item>
          <el-form-item>
            <el-button size="medium" type="primary" class="ml10" icon="el-icon-search" @click="search()">
              查询
            </el-button>
            <el-button size="medium" type="success" class="ml10" v-hasPermi="['system:user:add']" icon="el-icon-plus"
                       @click="onOpenAddUser('add')">
              新增用户
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table :data="tableData.data" v-loading="tableData.loading" style="width: 100%;min-height:calc(100vh - 320px)">
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
        <el-table-column prop="deptName" label="所属学院" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="identity" label="身份" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="major" label="专业" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="className" label="班级" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="roleName" label="关联角色" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="status" label="用户状态" align="center" show-overflow-tooltip>
          <template #default="scope">
            <el-switch
              v-model="scope.row.status"
              active-color="#13ce66"
              inactive-color="#ff4949"
              v-hasPermi="['system:user:update']"
              @change="changeStatus(scope.row)">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column prop="createBy" label="创建人" align="center" show-overflow-tooltip></el-table-column>
<!--        <el-table-column prop="createTime" label="创建时间" align="center" show-overflow-tooltip></el-table-column>-->
<!--        <el-table-column prop="updateBy" label="修改人" align="center" show-overflow-tooltip></el-table-column>-->
<!--        <el-table-column prop="updateTime" label="修改时间" align="center" show-overflow-tooltip></el-table-column>-->
        <el-table-column label="操作" align="center" width="150px">
          <template #default="scope">
            <el-button size="small"
                       type="text"
                       v-hasPermi="['system:user:update']"
                       icon="el-icon-edit"
                       @click="onOpenEditUser('edit', scope.row)"
            >修改
            </el-button>
            <el-button size="small"
                       type="text"
                       icon="el-icon-delete"
                       v-hasPermi="['system:user:delete']"
                       @click="onRowDel(scope.row)">删除
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
    <UserDialog ref="userDialogRef" @refresh="getTableData()"/>
  </div>
</template>

<script>
import {Message, MessageBox} from "element-ui";
import {changeStatus, deleteUser, getUser} from "@/api/system/user";
import {getRoleToUser} from "@/api/system/role";
import {getDeptList} from "@/api/system/dept";

export default {
  name: 'powerUser',
  data() {
    return {
      tableData: {
        data: [],
        total: 0,
        loading: false,
        param: {
          userName: '',
          deptId: '',
          identity: '',
          pageNum: 1,
          pageSize: 10,
        }
      },
      deptList: [],
      roles: []
    };
  },
  components: {
    UserDialog: () => import('@/views/system/user/dialog.vue'),
    dictSelect: () => import("@/component/dictSelect/index.vue"),
  },
  mounted() {
    this.getTableData();
    this.getRoleAndDept();
  },
  methods: {
    /**
     * 获取菜单
     */
    getTableData() {
      this.tableData.loading = true;
      getUser(this.tableData.param).then(res => {
        this.tableData.data = res.data.list;
        this.tableData.total = res.data.total;
        setTimeout(() => {
          this.tableData.loading = false;
        }, 500);
      })
    },
    /**
     * 获取角色列表/部门列表
     */
    getRoleAndDept() {
      getRoleToUser().then(res => {
        this.roles = res.data
      })
      getDeptList().then(res => {
        this.deptList = res.data
      })
    },
    /**
     * 查询
     */
    search() {
      this.getTableData();
    },
    /**
     * 添加用户
     * @param flag
     */
    onOpenAddUser(flag) {
      const form = {
        userName: '', // 账户名称
        nickName: '', // 用户昵称
        password: '', // 密码
        roleSign: '',//关联角色
        phone: '', // 手机号
        email: '', // 邮箱
        sex: '', // 性别
        headSrc: '', // 头像
        identity: '', // 身份
        major: '', // 专业
        className: '', // 班级
        status: true, // 用户状态
      }
      this.$refs.userDialogRef.deptList = this.deptList
      this.$refs.userDialogRef.openDialog(flag, form, this.roles)
    },
    /**
     * 编辑用户
     * @param flag
     * @param row
     */
    onOpenEditUser(flag, row) {
      this.$refs.userDialogRef.deptList = this.deptList
      this.$refs.userDialogRef.openDialog(flag, row, this.roles)
    },
    /**
     * 删除用户
     * @param row
     */
    onRowDel(row) {
      //提示
      MessageBox.confirm(`此操作将永久删除账户名称：“${row.userName}”，是否继续?`, '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        //调用删除方法
        deleteUser(row.id).then(res => {
          this.getTableData();
          Message.success("删除成功");
        })
      }).catch(() => {
      });
    },
    /**
     * 用户账号修改回调
     */
    changeStatus(row) {
      changeStatus(row.id, row.status).then(res => {
        if (res.code === 200) {
          Message.success(res.message);
          this.getTableData()
        }
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
.el-cascader-panel {
  height: 200px;
}
</style>
