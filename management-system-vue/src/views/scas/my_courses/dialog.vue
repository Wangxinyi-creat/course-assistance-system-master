<template>
  <div class="system-menu-dialog-container">
    <el-dialog :title="dialog.title"
               :visible.sync="dialog.isShowDialog"
               :close-on-click-modal="false"
               width="769px">
      <el-form ref="myCoursesDialogFormRef" :model="ruleForm" :rules="rules" size="default" label-width="100px">
        <el-row :gutter="35">
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="学号" prop="userName">
              <el-input v-model="ruleForm.userName" placeholder="请输入学号" clearable disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="课程" prop="courseName">
              <el-input v-model="ruleForm.courseName" placeholder="请输入课程" clearable disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="选课日期" prop="enrollmentDate">
              <el-input v-model="ruleForm.enrollmentDate" placeholder="请输入选课日期" clearable disabled></el-input>
            </el-form-item>
          </el-col>

          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="平时成绩" prop="ordinaryGrade">
              <el-input v-model="ruleForm.ordinaryGrade" placeholder="请输入平时成绩" clearable type="number"></el-input>
            </el-form-item>
          </el-col>
<!--          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">-->
<!--            <el-form-item label="考试成绩" prop="examGrade">-->
<!--              <el-input v-model="ruleForm.examGrade" placeholder="请输入考试成绩" clearable type="number"></el-input>-->
<!--            </el-form-item>-->
<!--          </el-col>-->
<!--          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">-->
<!--            <el-form-item label="总分" prop="grade">-->
<!--              <el-input v-model="ruleForm.grade" placeholder="请输入总分" clearable type="number"></el-input>-->
<!--            </el-form-item>-->
<!--          </el-col>-->
        </el-row>
      </el-form>
      <template #footer>
				<span class="dialog-footer">
					<el-button @click="onCancel" size="default">取 消</el-button>
					<el-button type="primary" @click="onSubmit(dialog.type)" size="default">{{ dialog.submitTxt }}</el-button>
				</span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import {Message} from "element-ui";
import {updateCourses} from "@/api/scas/my_courses";

export default {
  name: 'MyCoursesDialog',
  data() {
    return {
      dialog: {
        isShowDialog: false,
        type: '',
        title: '',
        submitTxt: '',
      },
      isLinkFlag: false,
      ruleForm: {},
      rules: {
        ordinaryGrade: [{required: true, message: '请输入平时成绩', trigger: 'blur'}]
        // examGrade: [{required: true, message: '请输入考试成绩', trigger: 'blur'}],
        // grade: [{required: true, message: '请输入总分', trigger: 'blur'}]
      }
    };
  },
  methods: {
    /**
     * 打开弹框
     * @param row
     */
    openDialog(row) {
      this.ruleForm = {...row}
      this.dialog.title = '评分';
      this.dialog.submitTxt = '评 分';
      this.dialog.isShowDialog = true;
    },
    /**
     * 关闭弹框
     */
    onCancel() {
      this.dialog.isShowDialog = false;
      this.$refs.myCoursesDialogFormRef.resetFields()
    },
    /**
     * 新增/修改  保存
     * @param flag
     */
    onSubmit(flag) {
      this.$refs["myCoursesDialogFormRef"].validate(valid => {
        if (valid) {
          updateCourses(this.ruleForm).then(res => {
            if (res.code === 200) {
              Message.success(res.message);
              this.$emit('refresh');
            }
          })
          this.onCancel();
        }
      })
    }
  }
};
</script>

<style>
/* type=number样式解决 */
input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none;
}
input[type="number"]{
  -moz-appearance: textfield;
}
</style>
