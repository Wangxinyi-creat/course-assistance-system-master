<template>
  <div class="system-menu-dialog-container">
    <el-dialog :title="dialog.title"
               :visible.sync="dialog.isShowDialog"
               :close-on-click-modal="false"
               width="769px">
      <el-form ref="experimentDialogFormRef" :model="ruleForm" :rules="rules" size="default" label-width="110px">
        <el-row :gutter="35">
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="作业标题" prop="experimentTitle">
              <el-input v-model="ruleForm.experimentTitle" placeholder="请输入作业标题" clearable
                        :disabled="subFlag"></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="作业号" prop="jobNumber">
              <el-input v-model="ruleForm.jobNumber" placeholder="请输入作业号" clearable
                        :disabled="subFlag"></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="作业描述" prop="experimentDescription">
              <el-input v-model="ruleForm.experimentDescription" placeholder="请输入作业描述" clearable
                        :disabled="subFlag"
                        type="textarea" :rows="5"></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="截止日期" prop="deadline">
              <el-date-picker
                  v-model="ruleForm.deadline"
                  type="datetime"
                  style="width: 100%"
                  :disabled="subFlag"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="选择日期时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20" v-if="subFlag">
            <el-form-item label="总结" prop="answer">
              <el-input v-model="ruleForm.answer" placeholder="请输入总结" clearable
                        :disabled="disable"
                        type="textarea" :rows="5"></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20" v-if="subFlag">
            <el-form-item label="实验代码" prop="myCode">
              <el-input v-model="ruleForm.myCode" placeholder="请输入实验代码" clearable
                        :disabled="disable"
                        type="textarea" :rows="5"></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20" v-if="subFlag">
            <el-form-item label="附件上传" prop="uploadUrl">
              <el-button type="success" @click="()=>{this.$refs.uploadRef.open=true}" :disabled="disable">点击上传
              </el-button>
              <a :href="ruleForm.uploadUrl" target="_blank"
                 v-if="ruleForm.uploadUrl"
                 style="margin-left: 20px;font-size: 16px;cursor: pointer;color: #409eff">查看我的作业</a>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20" v-if="disable">
            <el-form-item label="成绩" prop="grade">
              <el-input v-model="ruleForm.grade" placeholder="请输入成绩" clearable type="number"></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20" v-if="disable">
            <el-form-item label="评阅意见" prop="review">
              <el-input v-model="ruleForm.review" placeholder="请输入评阅意见" clearable
                        type="textarea" :rows="5"></el-input>
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

    <upload ref="uploadRef" @uploadRes="uploadRes" v-if="dialog.isShowDialog"></upload>
  </div>
</template>

<script>
import {Message} from "element-ui";
import {addExperiment, updateExperiment} from "@/api/scas/experiment";

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
      subFlag: false,
      disable: false,
      createCourse: [],
      ruleForm: {},
      rules: {
        experimentTitle: [{required: true, message: '请输入作业标题', trigger: 'change'}],
        experimentDescription: [{required: true, message: '请输入作业描述', trigger: 'blur'}],
        deadline: [{required: true, message: '请选择截止日期', trigger: 'change'}]
      }
    };
  },
  methods: {
    /**
     * 打开弹框
     * @param type
     * @param row
     */
    openDialog(type, row) {
      if (type === 'edit') {
        this.ruleForm = {...row}
        this.subFlag = true
        this.disable = false
        this.dialog.title = '修改作业';
        this.dialog.submitTxt = '修 改';
      } else if (type === 'add') {
        this.ruleForm.courseId = row.id
        this.subFlag = false
        this.disable = false
        this.dialog.title = '新增作业';
        this.dialog.submitTxt = '新 增';
      } else {
        this.ruleForm = {...row}
        this.subFlag = true
        this.disable = true
        this.dialog.title = '批改作业';
        this.dialog.submitTxt = '批 改';
      }
      this.dialog.type = type
      this.dialog.isShowDialog = true;
    },
    /**
     * 关闭弹框
     */
    onCancel() {
      this.dialog.isShowDialog = false;
      this.$refs.experimentDialogFormRef.resetFields()
    },
    uploadRes(res) {
      this.ruleForm.uploadUrl = res.data
    },
    /**
     * 新增/修改  保存
     * @param flag
     */
    onSubmit(flag) {
      this.$refs["experimentDialogFormRef"].validate(valid => {
        if (valid) {
          if (flag === 'edit') {
            if (!this.ruleForm.answer) {
              Message.error("请填写总结")
              return;
            }
            if (!this.ruleForm.uploadUrl) {
              Message.error("请上传作业附件")
              return;
            }
            this.ruleForm.result= '正确'
            this.ruleForm.editFlag = 1
            updateExperiment(this.ruleForm).then(res => {
              if (res.code === 200) {
                Message.success(res.message);
                this.$emit('refresh');
              }
            })
          } else if (flag === 'add') {
            addExperiment(this.ruleForm).then(res => {
              if (res.code === 200) {
                Message.success(res.message);
                this.$emit('refresh');
              }
            })
          } else {
            if (!this.ruleForm.grade) {
              Message.error("请填写成绩")
              return;
            }
            this.ruleForm.editFlag = 2
            updateExperiment(this.ruleForm).then(res => {
              if (res.code === 200) {
                Message.success(res.message);
                this.$emit('refresh');
              }
            })
          }
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
