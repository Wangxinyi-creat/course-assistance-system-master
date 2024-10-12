<template>
  <div class="system-user-container layout-padding">
    <el-card shadow="hover" class="layout-padding-auto">
      <el-empty description="暂无选课，快去选课吧" v-if="tableData.total ===0"></el-empty>
      <el-row :gutter="20" v-else>
        <el-col :span="6"
                v-for="(item,index) in tableData.data"
                :key="index"
                style="cursor: pointer">
          <el-card class="box-card" shadow="hover">
            <div slot="header" class="clearfix">
              <span>课程名称：{{ item.courseName }}</span>
              <div style="float: right;">
                <el-button type="text"
                           style="padding: 3px 0"
                           icon="el-icon-delete"
                           @click="onRowDel(item)"
                           v-if="item.courseStatus === 1">退课
                </el-button>
                <el-button type="text"
                           style="padding: 3px 0"
                           icon="el-icon-info"
                           @click="pingjia(item)"
                           v-if="item.courseStatus !== 1 && item.courseStatus !==2 ">评价
                </el-button>
              </div>
            </div>
            <div class="card-item" @click="gotoDetail(item)">
              <p>课程类型：{{ item.courseType }}</p>
              <p>任课教师：{{ item.teacherName }}</p>
              <p>选课日期：{{ item.enrollmentDate }}</p>
              <p>课程状态：
                <el-tag v-if="item.courseStatus === 1" type="success">选课中</el-tag>
                <el-tag v-else-if="item.courseStatus === 2">教学中</el-tag>
                <el-tag v-else type="info">已结束</el-tag>
              </p>
            </div>
          </el-card>
        </el-col>
      </el-row>

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


      <reviews ref="reviewsRef"/>
    </el-card>
  </div>
</template>

<script>
import {Message, MessageBox} from "element-ui";
import {delCourses, getCourses} from "@/api/scas/my_courses";

export default {
  name: 'Courses',
  data() {
    return {
      tableData: {
        data: [],
        total: 0,
        loading: false,
        param: {
          pageNum: 1,
          pageSize: 10,
        },
      },
      createCourse: []
    };
  },
  components: {
    reviews: () => import('@/views/scas/student/pingjia.vue'),
    detail: () => import('./detail.vue')
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
      getCourses(this.tableData.param).then(res => {
        this.tableData.data = res.data.list;
        this.tableData.total = res.data.total;
        setTimeout(() => {
          this.tableData.loading = false;
        }, 500);
      })
    },
    gotoDetail(row) {
      this.$router.push({name: 'courseDetail', query: {id: row.courseId}})
    },
    pingjia(row) {
      this.$refs.reviewsRef.openDialog(row)
    },
    /**
     * 删除字典
     * @param row
     */
    onRowDel(row) {
      const id = row.id
      //提示
      MessageBox.confirm(`此操作将退出，已报名课程：“${row.courseName}”，是否继续?`, '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        //调用删除方法
        delCourses(id).then(res => {
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
.card-item {
  padding: 10px;

  p {
    padding: 5px 0;
    font-size: 16px;
  }
}
</style>
