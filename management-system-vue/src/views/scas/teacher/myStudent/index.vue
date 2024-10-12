<template>
  <div class="system-user-container layout-padding">
    <el-card shadow="hover" class="layout-padding-auto">
      <div class="system-user-search mb15">
        <el-input v-model="tableData.param.major" clearable placeholder="请输入专业" size="medium"
                  style="width: 200px;margin-left: 10px"></el-input>
        <el-input v-model="tableData.param.userName" clearable placeholder="请输入学号" size="medium"
                  style="width: 200px;margin-left: 10px"></el-input>
        <el-button size="medium" type="primary" class="ml10" icon="el-icon-search" @click="getTableData()">
          查询
        </el-button>
      </div>

      <el-table :data="tableData.data" v-loading="tableData.loading" style="width: 100%;min-height:calc(100vh - 320px)">
        <el-table-column type="index" label="序号" align="center" width="60"/>
        <el-table-column prop="userName" label="学号" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="courseName" label="课程" align="center" show-overflow-tooltip></el-table-column>
        <!--        <el-table-column prop="courseId" label="课程ID" align="center" show-overflow-tooltip></el-table-column>-->
        <el-table-column prop="enrollmentDate" label="选课日期" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="ordinaryGrade" label="平时成绩" align="center" show-overflow-tooltip>
          <template slot-scope="scope">
            <span v-if="scope.row.ordinaryGrade">{{scope.row.ordinaryGrade}}</span>
            <el-tag v-else type="info">暂未结课</el-tag>
          </template>
        </el-table-column>
<!--        <el-table-column prop="examGrade" label="考试成绩" align="center" show-overflow-tooltip>-->
<!--          <template slot-scope="scope">-->
<!--            <span v-if="scope.row.examGrade">{{scope.row.examGrade}}</span>-->
<!--            <el-tag v-else type="info">暂未结课</el-tag>-->
<!--          </template>-->
<!--        </el-table-column>-->
<!--        <el-table-column prop="grade" label="总分" align="center" show-overflow-tooltip>-->
<!--          <template slot-scope="scope">-->
<!--            <span v-if="scope.row.grade">{{scope.row.grade}}</span>-->
<!--            <el-tag v-else type="info">暂未结课</el-tag>-->
<!--          </template>-->
<!--        </el-table-column>-->
        <el-table-column label="操作" align="center" width="200px">
          <template #default="scope">
            <el-button size="small"
                       type="text"
                       icon="el-icon-help"
                       @click="openUpdate(scope.row)">评分
            </el-button>
            <el-button size="small"
                       type="text"
                       icon="el-icon-delete"
                       v-if="scope.row.courseStatus === 1"
                       @click="onRowDel(scope.row)">退课
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

    <MyCoursesDialog ref="MyCoursesDialogRef" @refresh="getTableData()"/>
  </div>
</template>

<script>
import {Message, MessageBox} from "element-ui";
import {delCourses, getCourses} from "@/api/scas/my_courses";

export default {
  name: 'Courses',
  props:{
    courseId:{
      type: Number,
      default: ''
    }
  },
  data() {
    return {
      tableData: {
        data: [],
        total: 0,
        loading: false,
        param: {
          major: '',
          courseId: '',
          userName: '',
          pageNum: 1,
          pageSize: 10,
        },
      }
    };
  },
  components: {
    MyCoursesDialog: () => import('./dialog.vue')
  },
  mounted() {
    this.tableData.param.courseId = this.courseId
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
    /**
     * 修改弹框
     * @param row
     */
    openUpdate(row) {
      this.$refs.MyCoursesDialogRef.openDialog(row);
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
