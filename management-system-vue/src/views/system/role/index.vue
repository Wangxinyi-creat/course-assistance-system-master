<template>
  <div class="system-user-container layout-padding">
    <el-card shadow="hover" class="layout-padding-auto">
      <div class="system-user-search mb15">
        <el-input size="medium" v-model="tableData.param.roleName" placeholder="请输入角色名称"
                  style="max-width: 180px"></el-input>
        <el-button size="medium" type="primary" class="ml10" icon="el-icon-search" @click="search()">
          查询
        </el-button>
        <el-button size="medium" type="success" class="ml10" icon="el-icon-plus" v-hasPermi="['system:role:add']"
                   @click="onOpenDialogRole('add',null)">
          新增角色
        </el-button>
      </div>
      <el-table :data="tableData.data" v-loading="tableData.loading" border style="width: 100%;min-height:calc(100vh - 320px)">
        <el-table-column type="index" label="序号" align="center" width="60"/>
        <el-table-column prop="roleName" label="角色名称" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="dataScope" label="数据范围" align="center" show-overflow-tooltip>
          <template #default="scope">
            <span>{{ scope.row.dataScope === 1 ? "全部数据权限" : "自定义数据权限" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="是否显示" align="center" show-overflow-tooltip>
          <template #default="scope">
            <el-switch
                v-model="scope.row.showFlag"
                active-color="#13ce66"
                inactive-color="#ff4949"
                @change="changeStatus(scope.row)">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column prop="remarks" label="备注" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="createBy" label="创建人" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="createTime" label="创建时间" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column label="操作" align="center" width="300px">
          <template #default="scope">
            <el-button v-if="scope.row.dataScope!==1"
                       size="small"
                       type="text"
                       icon="el-icon-menu"
                       v-hasPermi="['system:role:update']"
                       @click="onOpenRoleMenu(scope.row)">分配菜单
            </el-button>
            <el-button size="small" type="text" v-hasPermi="['system:role:update']" @click="empower(scope.row)" icon="el-icon-view">已授权用户</el-button>
            <el-button size="small" type="text" v-hasPermi="['system:role:update']" @click="onOpenDialogRole('edit', scope.row)" icon="el-icon-edit">修改</el-button>
            <el-button size="small" type="text" v-hasPermi="['system:role:delete']" @click="onRowDel(scope.row)" icon="el-icon-delete">删除</el-button>
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

    <el-dialog :title="dialog.title"
               :visible.sync="dialog.isShowDialog"
               :close-on-click-modal="false"
               width="769px">
      <el-form ref="roleDialogFormRef" :model="ruleForm" :rules="rules" size="default" label-width="90px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="ruleForm.roleName" placeholder="请输入角色名称(不可重复)" clearable
                    maxlength="20"></el-input>
        </el-form-item>
        <el-form-item label="数据范围" prop="dataScope">
          <el-select v-model="ruleForm.dataScope" placeholder="请选择" clearable class="w100">
            <el-option label="全部数据权限" :value=1></el-option>
            <el-option label="自定义数据权限" :value=2></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input type="textarea"
                    :autosize="{ minRows: 3, maxRows: 6}"
                    v-model="ruleForm.remarks"
                    placeholder="请输入备注" maxlength="200" show-word-limit clearable></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
				<span class="dialog-footer">
					<el-button @click="onCancel" size="default">取 消</el-button>
					<el-button type="primary" @click="onSubmit(dialog.type)" size="default">{{ dialog.submitTxt }}</el-button>
				</span>
      </template>
    </el-dialog>

    <!--分配菜单抽屉-->
    <RoleMenuDialog ref="RoleMenuDialogRef" @refresh="getTableData()"/>
  </div>
</template>

<script>
import {Message, MessageBox} from "element-ui";
import {addRole, changeStatus, deleteRole, getRole, updateRole} from "@/api/system/role";
import {addEmpowerRouter} from "@/utils/addRouter";

export default {
  name: 'powerRole',
  data() {
    return {
      tableData: {
        data: [],
        total: 0,
        loading: false,
        param: {
          roleName: '',
          pageNum: 1,
          pageSize: 10,
        },
      },
      dialog: {
        isShowDialog: false,
        type: '',
        title: '',
        submitTxt: '',
      },
      ruleForm: {
        roleName: '', // 角色名称
        dataScope: '', // 数据范围
        remarks: '', // 备注
      },
      rules: {roleName: [{required: true, message: '请输入角色名称', trigger: 'blur'}]}
    };
  },
  components: {
    RoleMenuDialog: () => import('@/views/system/role/dialog.vue'),
  },
  created() {
    this.getTableData();
  },
  methods: {
    /**
     * 获取菜单
     */
    getTableData() {
      this.tableData.loading = true;
      getRole(this.tableData.param).then(res => {
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
     * 打开新增，编辑对话框
     * @param flag
     * @param row
     */
    onOpenDialogRole(flag, row) {
      if (flag === 'edit') {
        this.ruleForm = {...row};
        this.dialog.title = '修改用户';
        this.dialog.submitTxt = '修 改';
      } else {
        this.ruleForm = {};
        this.dialog.title = '新增用户';
        this.dialog.submitTxt = '新 增';
      }
      this.dialog.type = flag
      this.dialog.isShowDialog = true;
    },
    /**
     * 删除用户
     * @param row
     */
    onRowDel(row) {
      //提示
      MessageBox.confirm(`此操作将永久删除角色名称：“${row.roleName}”，是否继续?`, '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        //调用删除方法
        deleteRole(row.id).then(res => {
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
      changeStatus(row.id, row.showFlag).then(res => {
        if (res.code === 200) {
          Message.success(res.message);
          this.getTableData()
        }
      })
    },
    /**
     * 关闭弹框
     */
    onCancel() {
      this.dialog.isShowDialog = false;
      this.$refs.roleDialogFormRef.resetFields()
    },
    /**
     * 打开分配菜单界面
     * @param row
     */
    onOpenRoleMenu(row) {
      this.$refs.RoleMenuDialogRef.getRoleMenu(row.id);
    },
    /**
     * 增加/修改 角色
     * @param type
     */
    onSubmit(type) {
      this.$refs["roleDialogFormRef"].validate(valid => {
        if (valid) {
          if (type === 'edit') {
            updateRole(this.ruleForm).then(res => {
              if (res.code === 200) {
                Message.success(res.message);
                this.getTableData()
              }
            })
          } else {
            addRole(this.ruleForm).then(res => {
              if (res.code === 200) {
                Message.success(res.message);
                this.getTableData()
              }
            })
          }
          this.onCancel();
        }
      })
    },
    /**
     * 跳转查询已授权用户
     * @param row
     */
    empower(row){
      //添加路由
      addEmpowerRouter()
      //跳转
      this.$router.push({name: 'empower', query: {id: row.id}})
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
