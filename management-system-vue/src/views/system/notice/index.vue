<template>
  <div class="system-user-container layout-padding">
    <el-card shadow="hover" class="layout-padding-auto">
      <div class="system-user-search mb15">
        <el-input size="medium" v-model="tableData.param.noticeTitle" placeholder="请输入通知公告名称" clearable
                  style="max-width: 200px"></el-input>
        <el-button size="medium" type="primary" class="ml10" icon="el-icon-search" @click="getTableData()">
          查询
        </el-button>
        <el-button size="medium" type="success" class="ml10" icon="el-icon-plus"
                   v-hasPermi="['system:notice:add']"
                   @click="openAddOrUpdate('add',1)">
          新增
        </el-button>
      </div>

      <el-table :data="tableData.data" v-loading="tableData.loading" style="width: 100%;min-height:calc(100vh - 320px)">
        <el-table-column type="index" label="序号" align="center" width="60"/>
        <el-table-column prop="noticeTitle" label="标题" align="center" width="400"
                         show-overflow-tooltip></el-table-column>
        <el-table-column prop="noticeType" label="类型" align="center" show-overflow-tooltip>
          <template #default="scope">
            {{ scope.row.noticeType === '1' ? '通知' : '公告' }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" align="center">
          <template #default="scope">
            <el-switch
                @change="changeStatus(scope.row)"
                style="display: block"
                v-model="scope.row.status"
                active-color="#13ce66"
                inactive-color="#ff4949"
                active-text="发布中"
                inactive-text="编辑">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column prop="createBy" label="创建人" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="createTime" label="创建时间" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="updateBy" label="修改人" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="updateTime" label="修改时间" align="center" show-overflow-tooltip></el-table-column>
        <!--        <el-table-column prop="remark" label="备注" align="center" show-overflow-tooltip></el-table-column>-->
        <el-table-column label="操作" align="center" width="200px">
          <template #default="scope">
            <el-button size="small"
                       type="text"
                       icon="el-icon-view"
                       @click="openCheck(scope.row)"
            >查看
            </el-button>
            <el-button size="small"
                       type="text"
                       icon="el-icon-edit"
                       v-hasPermi="['system:notice:update']"
                       @click="openAddOrUpdate('edit', scope.row)"
            >修改
            </el-button>
            <el-button size="small"
                       type="text"
                       icon="el-icon-delete"
                       v-hasPermi="['system:notice:delete']"
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
  </div>
</template>

<script>
import {Message, MessageBox} from "element-ui";
import {changeStatus, delNotice, getNotice} from "@/api/system/notice";
import {CheckNoticeRouter, addOrEditNoticeRouter} from "@/utils/addRouter";
import store from "@/store";

export default {
  name: 'Notice',
  data() {
    return {
      tableData: {
        data: [],
        total: 0,
        loading: false,
        param: {
          noticeTitle: '',
          status: '',
          pageNum: 1,
          pageSize: 10,
        },
      },
    };
  },
  mounted() {
    this.getTableData();
  },
  methods: {
    /**
     * 获取通知公告
     */
    getTableData() {
      //查询权限，无权限仅查询已发布状态
      const permissions = store.state.userInfos.userInfos.authBtnList
      if (!permissions.includes("system:notice:update")) this.tableData.param.status = 1;
      this.tableData.loading = true;
      //查询
      getNotice(this.tableData.param).then(res => {
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
      //添加路由
      addOrEditNoticeRouter()//添加、修改通知公告路由
      this.$router.push({name: 'addOrEdit-notice', params: {flag: flag, row: row}})
    },
    /**
     * 删除通知公告
     * @param row
     */
    onRowDel(row) {
      //提示
      MessageBox.confirm(`此操作将永久删除通知公告标题为：“${row.noticeTitle}”的数据，是否继续?`, '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        //调用删除方法
        delNotice(row.noticeId).then(res => {
          this.getTableData();
          Message.success("删除成功");
        })
      }).catch(() => {
      });
    },
    /**
     * 查看通知公告详情
     * @param row
     */
    openCheck(row) {
      CheckNoticeRouter()//查询公告详情路由
      this.$router.push({name: 'checkNotice', query: {id: row.noticeId}})
    },
    /**
     * 通知公告修改回调
     */
    changeStatus(row) {
      changeStatus(row.noticeId, row.status).then(res => {
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
