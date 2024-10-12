<template>
  <div class="system-menu-dialog-container">
    <el-dialog :title="dialog.title"
               :visible.sync="dialog.isShowDialog"
               :close-on-click-modal="false"
               width="769px">
      <el-form ref="reviewsDialogFormRef" :model="ruleForm" :rules="rules" size="default" label-width="100px">
        <el-row :gutter="35">
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="课程" prop="courseName">
              <el-input v-model="ruleForm.courseName" placeholder="请输入课程" clearable disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="评分" prop="rating">
              <el-input v-model="ruleForm.rating" placeholder="请输入评分" clearable type="number"></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="评价内容" prop="reviewText">
              <el-input v-model="ruleForm.reviewText" placeholder="请输入评价内容" clearable type="textarea" :rows="5"></el-input>
            </el-form-item>
          </el-col>
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
import {addReviews} from "@/api/scas/reviews";

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
        reviewText: [{required: true, message: '请输入评价内容', trigger: 'blur'}],
        rating: [{required: true, message: '请输入评分', trigger: 'blur'}]
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
      this.dialog.title = '评价';
      this.dialog.submitTxt = '评 价';
      this.dialog.isShowDialog = true;
    },
    /**
     * 关闭弹框
     */
    onCancel() {
      this.dialog.isShowDialog = false;
      this.$refs.reviewsDialogFormRef.resetFields()
    },
    /**
     * 新增/修改  保存
     * @param flag
     */
    onSubmit(flag) {
      this.$refs["reviewsDialogFormRef"].validate(valid => {
        if (valid) {
          const param = {
            courseId: this.ruleForm.courseId,
            reviewText: this.ruleForm.reviewText,
            rating: this.ruleForm.rating
          }
          addReviews(param).then(res => {
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
