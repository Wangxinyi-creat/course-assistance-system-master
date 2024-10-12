<template>
  <div class="system-menu-dialog-container">
    <el-dialog :title="dialog.title"
               :visible.sync="dialog.isShowDialog"
               :close-on-click-modal="false"
               width="769px">
      <el-form ref="assignmentDialogFormRef" :model="ruleForm" :rules="rules" size="default" label-width="110px">
        <el-row :gutter="35">
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="作业标题" prop="experimentTitle">
              <el-input v-model="ruleForm.experimentTitle" placeholder="请输入作业标题" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="作业号" prop="jobNumber">
              <el-input v-model="ruleForm.jobNumber" placeholder="请输入作业号" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="作业描述" prop="experimentDescription">
              <el-input v-model="ruleForm.experimentDescription" placeholder="请输入作业描述" clearable
                        type="textarea" :rows="5"></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="截止日期" prop="deadline">
              <el-date-picker
                  v-model="ruleForm.deadline"
                  type="datetime"
                  style="width: 100%"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="选择日期时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
				<span class="dialog-footer">
					<el-button @click="onCancel" size="default">取 消</el-button>
					<el-button type="primary" @click="onSubmit()" size="default">{{ dialog.submitTxt }}</el-button>
				</span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import {Message} from "element-ui";
import {updateHandle} from "@/api/scas/experiment";

export default {
  components: {
    upload: () => import('@/component/upload/index')
  },
  name: 'ResourceDialog',
  data() {
    return {
      dialog: {
        isShowDialog: false,
        type: '',
        title: '',
        submitTxt: '',
      },
      ruleForm: {},
      rules: {
        experimentTitle: [{required: true, message: '请输入作业标题', trigger: 'change'}],
        jobNumber: [{required: true, message: '请输入作业号', trigger: 'change'}],
        experimentDescription: [{required: true, message: '请输入作业描述', trigger: 'blur'}],
        deadline: [{required: true, message: '请选择截止日期', trigger: 'change'}]
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
      this.subFlag = true
      this.disable = false
      this.dialog.title = '修改作业';
      this.dialog.submitTxt = '修 改';
      this.dialog.isShowDialog = true;
    },
    /**
     * 关闭弹框
     */
    onCancel() {
      this.dialog.isShowDialog = false;
      this.$refs.assignmentDialogFormRef.resetFields()
    },
    /**
     * 新增/修改  保存
     */
    onSubmit() {
      this.$refs["assignmentDialogFormRef"].validate(valid => {
        if (valid) {
          updateHandle(this.ruleForm).then(res => {
            if (res.code === 200) {
              Message.success("操作成功");
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

input[type="number"] {
  -moz-appearance: textfield;
}
</style>
