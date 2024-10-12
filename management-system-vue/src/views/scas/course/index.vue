<template>
  <div class="system-user-container layout-padding">
    <el-card shadow="hover" class="layout-padding-auto">
      <div class="system-user-search mb15">
        <el-form :model="tableData.param" ref="queryForm" size="small" :inline="true">
          <el-form-item>
            <el-input size="medium" v-model="tableData.param.courseName" placeholder="请输入课程名称"
                      style="max-width: 180px"></el-input>
          </el-form-item>
          <el-form-item>
            <dictSelect v-model="tableData.param.status" dictType="courseStatus" placeholder="请选择课程状态"
                        size="medium"></dictSelect>
          </el-form-item>
          <el-form-item>
            <el-button size="medium" type="primary" class="ml10" icon="el-icon-search" @click="getTableData()">
              查询
            </el-button>
            <el-button size="medium" type="success" class="ml10" icon="el-icon-folder-add"
                       v-hasPermi="['scas:course:add']"
                       @click="openAddOrUpdate('add',1)">
              新增
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table :data="tableData.data" v-loading="tableData.loading" style="width: 100%;min-height:calc(100vh - 320px)">
        <el-table-column type="index" label="序号" align="center" width="60"/>
        <el-table-column prop="courseName" label="课程名称" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="teacherName" label="教师" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="classTime" label="上课时间" align="center" show-overflow-tooltip>
          <template #default="scope">
            {{ getClassTime(scope.row.classTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="courseType" label="课程类型" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="courseDescription" label="课程介绍" align="center"
                         show-overflow-tooltip></el-table-column>
        <el-table-column prop="status" label="课程状态" align="center" show-overflow-tooltip>
          <template #default="scope">
            <el-tag v-if="scope.row.status === 1" type="success">选课中</el-tag>
            <el-tag v-else-if="scope.row.status === 2">教学中</el-tag>
            <el-tag v-else type="info">已结束</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="300px">
          <template #default="scope">
            <el-button size="small"
                       type="text"
                       icon="el-icon-success"
                       v-if="scope.row.status === 1"
                       v-hasPermi="['scas:mycourses:add']"
                       @click="addMycourses(scope.row)"
            >报名
            </el-button>
            <el-button size="small"
                       type="text"
                       icon="el-icon-search"
                       v-hasPermi="['scas:course:detail']"
                       @click="gotoDetail(scope.row)"
            >查看
            </el-button>
            <el-button size="small"
                       type="text"
                       icon="el-icon-circle-plus"
                       v-if="scope.row.status === 2"
                       v-hasPermi="['scas:assignment:add']"
                       @click="addExperiment(scope.row)"
            >发布作业
            </el-button>
            <el-button size="small"
                       type="text"
                       icon="el-icon-edit"
                       v-hasPermi="['scas:course:update']"
                       @click="openAddOrUpdate('edit', scope.row)"
            >修改
            </el-button>
            <el-button size="small"
                       type="text"
                       icon="el-icon-delete"
                       v-if="scope.row.status === 1"
                       v-hasPermi="['scas:course:delete']"
                       @click="onRowDel(scope.row)"
            >删除
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

    <CourseDialog ref="courseDialog" @refresh="getTableData()"/>
    <assignmentDialog ref="assignmentDialog" @refresh="getTableData()"/>
    <experimentDialog ref="experimentDialogRef" @refresh="getTableData()"/>

  </div>
</template>

<script>
import {Message, MessageBox} from "element-ui";
import {delCourse, getCourse} from "@/api/scas/course";
import {addCourses} from "@/api/scas/my_courses";

export default {
  name: 'Course',
  data() {
    return {
      tableData: {
        data: [],
        total: 0,
        loading: false,
        param: {
          courseName: '',
          status: '',
          pageNum: 1,
          pageSize: 10,
        },
      },
    };
  },
  components: {
    dictSelect: () => import("@/component/dictSelect/index.vue"),
    CourseDialog: () => import('./dialog.vue'),
    experimentDialog: () => import('@/views/scas/teacher/experiment/dialog.vue'),
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
      getCourse(this.tableData.param).then(res => {
        this.tableData.data = res.data.list;
        this.tableData.total = res.data.total;
        setTimeout(() => {
          this.tableData.loading = false;
        }, 500);
      })
    },
    gotoDetail(row) {
      this.$router.push({name: 'courseDetailT', query: {id: row.id}})
    },
    /**
     * 添加或修改弹框
     * @param flag
     * @param row
     */
    openAddOrUpdate(flag, row) {
      this.$refs.courseDialog.openDialog(flag, row);
    },
    addAssignment(row){
      this.$refs.assignmentDialog.openDialog('add',row)
    },
    addExperiment(row) {
      this.$refs.experimentDialogRef.openDialog('add', row)
    },
    getClassTime(item) {
      const data = JSON.parse(item)
      const convertedData = {};

      // 遍历传入的 JSON 形式的数据数组
      data.forEach(entry => {
        // 将 "dictLabel" 作为键， "dictValue" 作为值，添加到转换后的对象中
        convertedData[entry.dictLabel] = entry.dictValue;
      });

      // 将转换后的对象转换为 JSON 格式的字符串
      return JSON.stringify(convertedData)
          // 使用正则表达式替换双引号和逗号
          .replace(/"/g, '') // 去掉双引号
          .replace(/,/g, '； ')// 添加空格;
          .slice(1, -1);
    },
    /**
     * 删除字典
     * @param row
     */
    onRowDel(row) {
      const id = row.id
      //提示
      MessageBox.confirm(`此操作将永久删除课程名称为：“${row.courseName}”的数据及其子数据，是否继续?`, '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        //调用删除方法
        delCourse(id).then(res => {
          this.getTableData();
          Message.success("删除成功");
        })
      }).catch(() => {
      });
    },
    addMycourses(row) {
      MessageBox.confirm(`是否报名课程名称为：“${row.courseName}”的课程?`, '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        //调用删除方法
        const param = {
          courseId: row.id
        }
        addCourses(param).then(res => {
          if (res.code === 200) {
            Message.success("报名成功");
          }
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
