<template>
  <div class="system-menu-container layout-pd">
    <el-card shadow="hover">
      <div class="system-menu-search mb15">
        <el-input size="medium" v-model="tableData.param.deptName" placeholder="请输入学院名称" clearable
                  style="max-width: 180px"></el-input>
        <el-button size="medium" type="primary" class="ml10" icon="el-icon-search" @click="getTableData()">查询
        </el-button>
        <el-button size="medium" type="success" class="ml10" icon="el-icon-plus" v-hasPermi="['system:dept:add']"
                   @click="onOpenAddOrEditDept('add',0)">
          新增学院
        </el-button>
      </div>
      <el-table
        :data="tableData.data"
        v-loading="tableData.loading"
        style="width: 100%"
        border
        row-key="id"
        :default-expand-all="true"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      >
        <el-table-column prop="deptName" label="学院名称" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="leader" label="负责人" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="phone" label="联系电话" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="email" label="邮箱" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="status" label="学院状态" align="center" show-overflow-tooltip>
          <template #default="scope">
            <el-tag v-if="scope.row.status" type="success">正常</el-tag>
            <el-tag v-else type="danger">停用</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createBy" label="创建者" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="createTime" label="创建时间" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="updateBy" label="更新者" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="updateTime" label="更新时间" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column label="操作" width="200px" align="center">
          <template #default="scope">
            <el-button size="small" type="text"
                       v-if="scope.row.menuType!=2"
                       v-hasPermi="['system:dept:add']"
                       icon="el-icon-plus"
                       @click="onOpenAddOrEditDept('add',scope.row)">新增
            </el-button>
            <el-button size="small" type="text" @click="onOpenAddOrEditDept('edit', scope.row)"
                       v-hasPermi="['system:dept:update']" icon="el-icon-edit">修改
            </el-button>
            <el-button size="small" type="text" @click="onTabelRowDel(scope.row)" v-hasPermi="['system:dept:delete']"
                       icon="el-icon-delete">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <MenuDialog ref="deptDialogRef" @refresh="getTableData()"/>
  </div>
</template>

<script>
import {Message, MessageBox} from "element-ui";
import {deepCopy} from "@/utils/utils";
import {deleteDept, getDeptList} from "@/api/system/dept";

export default {
  name: 'powerMenu',
  data() {
    return {
      tableData: {
        data: [],
        loading: true,
        param: {
          deptName: '',
        },
      },
    };
  },
  components: {
    MenuDialog: () => import('@/views/system/dept/dialog.vue'),
  },
  created() {
    this.getTableData()
  },
  methods: {
    /**
     * 获取学院
     */
    getTableData() {
      this.tableData.loading = true;
      getDeptList(this.tableData.param).then(res => {
        this.tableData.data = res.data;
        setTimeout(() => {
          this.tableData.loading = false;
        }, 500);
      })
    },
    /**
     * 添加/修改 学院
     * @param flag
     * @param row
     */
    onOpenAddOrEditDept(flag, row) {
      //上级学院数据获取
      const deptDataS = deepCopy(this.tableData.data)
      deptDataS.unshift({
        id: 0,
        deptName: "无"
      })
      this.$refs.deptDialogRef.deptData = deptDataS
      this.$refs.deptDialogRef.openDialog(flag, row)
    },
    /**
     * 删除
     * @param row
     */
    onTabelRowDel(row) {
      //提示
      MessageBox.confirm(`此操作将永久删除学院名称为：“${row.deptName}”的数据，是否继续?`, '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        //调用删除方法
        deleteDept(row.id).then(res => {
          if (res.code === 200) {
            Message.success("删除成功");
          }
          this.getTableData();
        })
      }).catch(() => {
      });
    },
  }
};
</script>
