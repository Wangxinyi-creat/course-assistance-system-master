<template>
  <div class="system-user-container layout-padding">
    <el-card shadow="hover" class="layout-padding-auto">
      <div class="system-user-search mb15">
        <el-select v-model="tableData.param.courseId" placeholder="请选择所属课程" clearable size="medium">
          <el-option
              v-for="item in createCourse"
              :key="item.id"
              :label="item.courseName"
              :value="item.id">
          </el-option>
        </el-select>
        <el-input v-model="tableData.param.userName" clearable placeholder="请输入学号" size="medium"
                  v-if="user.identity === '管理员'"
                  style="width: 200px;margin-left: 10px"></el-input>
        <el-button size="medium" type="primary" class="ml10" icon="el-icon-search" @click="getTableData()">
          查询
        </el-button>
      </div>

      <el-table :data="tableData.data" v-loading="tableData.loading" style="width: 100%;min-height:calc(100vh - 320px)">
        <el-table-column type="index" label="序号" align="center" width="60"/>
        <el-table-column prop="courseName" label="课程" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="userName" label="用户" align="center" show-overflow-tooltip
                         v-if="user.identity === '管理员'"></el-table-column>
        <el-table-column prop="reviewText" label="评价内容" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="reviewDate" label="评价日期" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="rating" label="评分" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column label="操作" align="center" width="150px" v-if="!(user.identity === '老师')">
          <template #default="scope">
            <el-button size="small"
                       type="text"
                       v-hasPermi="['scas:reviews:delete']"
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
import {delReviews, getReviews} from "@/api/scas/reviews";
import {getCreate} from "@/api/scas/course";

export default {
  name: 'Reviews',
  data() {
    return {
      tableData: {
        data: [],
        total: 0,
        loading: false,
        param: {
          courseId: '',
          userName: '',
          pageNum: 1,
          pageSize: 10,
        },
      },
      createCourse: [],
      user: this.$store.state.userInfos.userInfos.user
    };
  },
  mounted() {
    this.getTableData();
    this.getCreateCourse();
  },
  methods: {
    /**
     * 获取菜单
     */
    getTableData() {
      this.tableData.loading = true;
      getReviews(this.tableData.param).then(res => {
        this.tableData.data = res.data.list;
        this.tableData.total = res.data.total;
        setTimeout(() => {
          this.tableData.loading = false;
        }, 500);
      })
    },
    getCreateCourse() {
      getCreate(1).then(res => {
        this.createCourse = res.data;
      })
    },
    /**
     * 添加或修改弹框
     * @param flag
     * @param row
     */
    openAddOrUpdate(flag, row) {
    },
    /**
     * 删除字典
     * @param row
     */
    onRowDel(row) {
      const id = row.id
      //提示
      MessageBox.confirm(`此操作将永久删除此课程评价，是否继续?`, '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        //调用删除方法
        delReviews(id).then(res => {
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

<style>
.el-tooltip__popper {
  max-width: 50%
}
</style>
