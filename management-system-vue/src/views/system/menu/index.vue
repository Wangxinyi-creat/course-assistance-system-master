<template>
  <div class="system-menu-container layout-pd">
    <el-card shadow="hover">
      <div class="system-menu-search mb15">
        <el-input size="medium" v-model="tableData.param.menuName" placeholder="请输入菜单名称"
                  style="max-width: 180px"></el-input>
        <el-button size="medium" type="primary" class="ml10" icon="el-icon-search" @click="getTableData()">查询
        </el-button>
        <el-button size="medium" type="success" class="ml10" icon="el-icon-plus" @click="onOpenAddOrEditMenu('add',0)">
          新增菜单
        </el-button>
      </div>
      <el-table
        :data="tableData.data"
        v-loading="tableData.loading"
        style="width: 100%"
        :row-style="menuTypeStyle"
        border
        row-key="id"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      >
        <el-table-column label="菜单名称" align="center" show-overflow-tooltip>
          <template slot-scope="scope">
            <i :class="scope.row.elIcon"></i>
            <span class="ml10">{{ scope.row.menuName }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="路由名称" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="path" label="路由路径" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="component" label="组件路径" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column label="菜单类型" align="center" show-overflow-tooltip>
          <template #default="scope">
            <el-tag type="info" size="small" v-if="scope.row.menuType === 0">目录</el-tag>
            <el-tag type="success" size="small" v-if="scope.row.menuType === 1">菜单</el-tag>
            <el-tag type="warning" size="small" v-if="scope.row.menuType === 2">按钮</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="是否隐藏" align="center" show-overflow-tooltip>
          <template slot-scope="scope" v-if="scope.row.menuType !== 2">
            <el-tag type="success" v-if="scope.row.isHide">是</el-tag>
            <el-tag type="danger" v-else>否</el-tag>
<!--            <span class="ml10"-->
<!--                  :style="scope.row.isHide?'color:#01b79d':'color:#ec5656'">{{ scope.row.isHide ? "" : "" }}</span>-->
          </template>
        </el-table-column>
        <el-table-column label="是否固定" align="center" show-overflow-tooltip>
          <template slot-scope="scope" v-if="scope.row.menuType === 1">
            <el-tag type="success" v-if="scope.row.isAffix">是</el-tag>
            <el-tag type="danger" v-else>否</el-tag>
<!--            <span class="ml10"-->
<!--                  :style="scope.row.isAffix?'color:#01b79d':'color:#ec5656'">{{-->
<!--                scope.row.isAffix ? "是" : "否"-->
<!--              }}</span>-->
          </template>
        </el-table-column>
        <el-table-column label="用户是否可见" align="center" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-tag type="success" v-if="scope.row.showFlag">是</el-tag>
            <el-tag type="danger" v-else>否</el-tag>
<!--            <span class="ml10"-->
<!--                  :style="scope.row.showFlag?'color:#01b79d':'color:#ec5656'">{{-->
<!--                scope.row.showFlag ? "是" : "否"-->
<!--              }}</span>-->
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180px" fixed="right">
          <template #default="scope">
            <el-button size="small" type="text" v-if="scope.row.menuType!=2"
                       @click="onOpenAddOrEditMenu('add',scope.row)"
                       icon="el-icon-plus">新增
            </el-button>
            <el-button size="small" type="text" @click="onOpenAddOrEditMenu('edit', scope.row)" icon="el-icon-edit">
              修改
            </el-button>
            <el-button size="small" type="text" @click="onTabelRowDel(scope.row)" icon="el-icon-delete">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <MenuDialog ref="menuDialogRef" @refresh="getTableData()"/>
  </div>
</template>

<script>
import {Message, MessageBox} from "element-ui";
import {deleteMenu, getMenuList} from "@/api/system/menu";
import {deepCopy, removeMenuType2} from "@/utils/utils";

export default {
  name: 'powerMenu',
  data() {
    return {
      tableData: {
        data: [],
        loading: true,
        param: {
          menuName: '',
        },
      },
    };
  },
  components: {
    MenuDialog: () => import('@/views/system/menu/dialog.vue'),
  },
  created() {
    this.getTableData()
  },
  methods: {
    /**
     * 获取菜单
     */
    getTableData() {
      this.tableData.loading = true;
      getMenuList(this.tableData.param).then(res => {
        this.tableData.data = res.data;
        setTimeout(() => {
          this.tableData.loading = false;
        }, 500);
      })
    },
    /**
     * 添加用户/编辑用户
     * @param flag
     * @param row
     */
    onOpenAddOrEditMenu(flag, row) {
      //上级菜单数据获取
      const menuDataS = removeMenuType2(deepCopy(this.tableData.data))
      menuDataS.unshift({
        id: 0,
        menuName: "无"
      })
      this.$refs.menuDialogRef.menuData = menuDataS
      this.$refs.menuDialogRef.openDialog(flag, row)
    },
    /**
     * 删除
     * @param row
     */
    onTabelRowDel(row) {
      //提示
      MessageBox.confirm(`此操作将永久删除菜单名称为：“${row.menuName}”的数据，是否继续?`, '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        //调用删除方法
        deleteMenu(row.id).then(res => {
          this.getTableData();
          Message.success("删除成功");
        })
      }).catch(() => {
      });
    },
    /**
     * 菜单类型样式
     * @param row
     * @param index
     * @returns {{}}
     */
    menuTypeStyle({row, index}) {
      let style = {};
      if (row.menuType === 1) {
        style = {
          'color': '#39ab50',
        }
      } else if (row.menuType === 2) {
        style = {
          'color': '#e6a23c',
          'background': '#fdf6ec',
        }
      }
      return style;
    }
  }
};
</script>
