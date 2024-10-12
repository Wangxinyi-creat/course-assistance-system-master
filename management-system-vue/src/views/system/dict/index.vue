<template>
  <div class="system-user-container layout-padding">
    <el-card shadow="hover" class="layout-padding-auto">
      <div class="system-user-search mb15">
        <el-input size="medium" v-model="tableData.param.dictName" placeholder="请输入字典名称"
                  style="max-width: 180px"></el-input>
        <el-button size="medium" type="primary" class="ml10" icon="el-icon-search" @click="getTableData()">
          查询
        </el-button>
        <el-button size="medium" type="success" class="ml10" icon="el-icon-folder-add"
                   @click="openAddOrUpdate('add',1)">
          新增字典
        </el-button>
      </div>

      <el-table :data="tableData.data" v-loading="tableData.loading" style="width: 100%;min-height:calc(100vh - 320px)">
        <el-table-column type="index" label="序号" align="center" width="60"/>
        <el-table-column prop="dictName" label="字典名称" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="dictType" label="字典类型" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="status" label="状态" align="center" show-overflow-tooltip>
          <template #default="scope">
            <el-tag type="success" v-if="scope.row.status">启用</el-tag>
            <el-tag type="danger" v-else>禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="createBy" label="创建人" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="createTime" label="创建时间" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="updateBy" label="更新者" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="updateTime" label="修改时间" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column label="操作" align="center" width="150px">
          <template #default="scope">
            <el-button size="small"
                       type="text"
                       icon="el-icon-edit"
                       @click="openAddOrUpdate('edit', scope.row)"
            >修改
            </el-button>
            <el-button size="small"
                       type="text"
                       icon="el-icon-delete"
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
    <DictDialog ref="dictDialogRef" @refresh="getTableData()"/>
  </div>
</template>

<script>
import {Message, MessageBox} from "element-ui";
import {deleteDict, getDict} from "@/api/system/dict";

export default {
  name: 'powerUser',
  data() {
    return {
      tableData: {
        data: [],
        total: 0,
        loading: false,
        param: {
          dictName: '',
          pageNum: 1,
          pageSize: 10,
        },
      },
    };
  },
  components: {
    DictDialog: () => import('@/views/system/dict/dialog.vue'),
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
      getDict(this.tableData.param).then(res => {
        this.tableData.data = res.data.list;
        this.tableData.total = res.data.total;
        setTimeout(() => {
          this.tableData.loading = false;
        }, 500);
      })
    },
    /**
     * 添加或修改弹框
     * @param flag
     * @param row
     */
    openAddOrUpdate(flag, row) {
      this.$refs.dictDialogRef.openDialog(flag, row)
    },
    /**
     * 删除字典
     * @param row
     */
    onRowDel(row) {
      //提示
      MessageBox.confirm(`此操作将永久删除字典名称为：“${row.dictName}”的数据及其子数据，是否继续?`, '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        //调用删除方法
        deleteDict(row).then(res => {
          this.getTableData();
          Message.success("删除成功");
        })
      }).catch(() => {
      });
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
