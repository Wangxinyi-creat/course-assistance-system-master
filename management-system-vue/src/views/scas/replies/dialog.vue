<template>
  <div class="system-menu-dialog-container">
    <el-dialog :title="dialog.title"
               :visible.sync="dialog.isShowDialog"
               :close-on-click-modal="false"
               width="769px">
      <el-form ref="repliesDialogFormRef" :model="ruleForm" :rules="rules" size="default" label-width="110px">
        <el-row :gutter="35">
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="所属课程" prop="courseId">
              <el-select v-model="ruleForm.courseId" placeholder="请选择所属课程" style="width: 100%;"
                         :disabled="dialog.type==='reply'">
                <el-option
                    v-for="item in createCourse"
                    :key="item.id"
                    :label="item.courseName"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="提问内容" prop="questionText">
              <el-input v-model="ruleForm.questionText" placeholder="请输入提问内容" clearable
                        type="textarea" :rows="5" :disabled="dialog.type==='reply'"></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20" v-if="dialog.type==='reply'">
            <el-form-item label="回复内容" prop="replyText">
              <el-input v-model="ruleForm.replyText" placeholder="请输入回复内容" clearable
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
  </div>
</template>

<script>
import {Message} from "element-ui";
import {addReplies, replyReplies, updateReplies} from "@/api/scas/replies";

export default {
  name: 'RepliesDialog',
  data() {
    return {
      dialog: {
        isShowDialog: false,
        type: '',
        title: '',
        submitTxt: '',
      },
      createCourse: [],
      ruleForm: {},
      rules: {
        courseId: [{required: true, message: '请选择所属课程', trigger: 'change'}],
        questionText: [{required: true, message: '请输入提问内容', trigger: 'blur'}]
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
        this.dialog.title = '修改提问';
        this.dialog.submitTxt = '修 改';
      } else if (type === 'add'){
        this.dialog.title = '新增提问';
        this.dialog.submitTxt = '新 增';
      }else {
        this.ruleForm = {...row}
        this.dialog.title = '回复提问';
        this.dialog.submitTxt = '回 复';
      }
      this.dialog.type = type
      this.dialog.isShowDialog = true;
    },
    /**
     * 关闭弹框
     */
    onCancel() {
      this.dialog.isShowDialog = false;
      this.$refs.repliesDialogFormRef.resetFields()
    },
    /**
     * 新增/修改  保存
     * @param flag
     */
    onSubmit(flag) {
      this.$refs["repliesDialogFormRef"].validate(valid => {
        if (valid) {
          if (flag === 'edit') {
            updateReplies(this.ruleForm).then(res => {
              if (res.code === 200) {
                Message.success(res.message);
                this.$emit('refresh');
              }
            })
          } else if (flag === 'reply') {
            replyReplies(this.ruleForm).then(res => {
              if (res.code === 200) {
                Message.success(res.message);
                this.$emit('refresh');
              }
            })
          }else {
            addReplies(this.ruleForm).then(res => {
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
