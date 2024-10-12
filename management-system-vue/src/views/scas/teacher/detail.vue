<template>
  <div>
    <el-card shadow="hover" class="layout-padding-auto">
      <i class="el-icon-d-arrow-left" style="cursor: pointer;font-size: 20px" @click="goBack()">返回</i>
      <p style="text-align: center;font-size: 26px">课程：{{ course.courseName }}</p>
      <el-tabs v-model="activeName" class="tabs-style">
        <el-tab-pane label="基本信息" name="first">
          <el-descriptions :column="1">
            <el-descriptions-item label="课程名称">{{ course.courseName }}</el-descriptions-item>
            <el-descriptions-item label="教师">{{ course.teacherName }}</el-descriptions-item>
            <el-descriptions-item label="上课时间"> {{ getClassTime(course.classTime) }}</el-descriptions-item>
            <el-descriptions-item label="课程类型">{{ course.courseType }}</el-descriptions-item>
            <el-descriptions-item label="课程状态">
              <el-tag v-if="course.status === 1" type="success">选课中</el-tag>
              <el-tag v-else-if="course.status === 2">教学中</el-tag>
              <el-tag v-else type="info">已结束</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="课程介绍">{{ course.courseDescription }}</el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>
        <el-tab-pane label="学习资源管理" name="second">
          <resource :course-id="courseId"></resource>
        </el-tab-pane>
        <el-tab-pane label="学生管理" name="third">
          <my-student :course-id="courseId"></my-student>
        </el-tab-pane>
        <el-tab-pane label="作业管理" name="five">
          <experiment :course-id="courseId"></experiment>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import {getDetail} from "@/api/scas/course";

export default {
  name: 'CoursesDetail.vue',
  data() {
    return {
      courseId: '',
      course: {},
      activeName: 'first'
    };
  },
  components: {
    experiment: () => import('./experiment/index.vue'),
    resource: () => import('./resource/index.vue'),
    myStudent: () => import('./myStudent/index.vue')
  },
  created() {
    this.courseId = this.$route.query.id
    this.getDetail()
  },
  methods: {
    getDetail() {
      getDetail(this.courseId).then(res => {
        this.course = res.data
      })
    },
    goBack() {
      this.$router.go(-1)
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
  }
};
</script>

<style>
.tabs-style {
  padding: 20px;
}

</style>
