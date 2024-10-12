<template>
  <div class="system-menu-dialog-container">
    <el-dialog :title="dialog.title"
               :visible.sync="dialog.isShowDialog"
               :close-on-click-modal="false"
               width="769px">
      <el-form ref="resourceDialogFormRef" :model="ruleForm" :rules="rules" size="default" label-width="110px">
        <el-row :gutter="35">
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="所属课程" prop="courseId">
              <el-select v-model="ruleForm.courseId" placeholder="请选择所属课程" style="width: 100%;">
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
            <el-form-item label="课程资源名称" prop="resourceName">
              <el-input v-model="ruleForm.resourceName" placeholder="请输入课程资源名称" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="资源链接" prop="resourceUrl">
              <el-button type="success" @click="()=>{this.$refs.uploadRef.open=true}">点击上传</el-button>
              <a :href="ruleForm.resourceUrl" target="_blank"
                 v-if="ruleForm.resourceUrl"
                 style="margin-left: 20px;font-size: 16px;cursor: pointer;color: #409eff" >查看资源</a>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="课程资源描述" prop="resourceDescription">
              <el-input v-model="ruleForm.resourceDescription" placeholder="请输入课程资源描述" clearable
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
import {addResource, updateResource} from "@/api/scas/resource";

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
      createCourse: [],
      ruleForm: {},
      resourceFlag: false,
      rules: {
        courseId: [{required: true, message: '请选择所属课程', trigger: 'change'}],
        resourceName: [{required: true, message: '请输入课程资源名称', trigger: 'blur'}]
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
        this.resourceFlag = true;
        this.dialog.title = '修改课程资源';
        this.dialog.submitTxt = '修 改';
      } else {
        this.resourceFlag = false;
        this.dialog.title = '新增课程资源';
        this.dialog.submitTxt = '新 增';
      }
      this.dialog.type = type
      this.dialog.isShowDialog = true;
    },
    /**
     * 关闭弹框
     */
    onCancel() {
      this.dialog.isShowDialog = false;
      this.$refs.resourceDialogFormRef.resetFields()
    },
    uploadRes(res) {
      this.resourceFlag = true;
      this.ruleForm.resourceUrl = res.data
    },
    /**
     * 新增/修改  保存
     * @param flag
     */
    onSubmit(flag) {
      if (!this.ruleForm.resourceUrl) {
        Message.warning('请上传资源');
        return;
      }
      this.$refs["resourceDialogFormRef"].validate(valid => {
        if (valid) {
          if (flag === 'edit') {
            updateResource(this.ruleForm).then(res => {
              if (res.code === 200) {
                Message.success(res.message);
                this.$emit('refresh');
              }
            })
          } else {
            addResource(this.ruleForm).then(res => {
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
