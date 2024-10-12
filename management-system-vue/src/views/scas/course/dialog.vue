<template>
  <div class="system-menu-dialog-container">
    <el-dialog :title="dialog.title"
               :visible.sync="dialog.isShowDialog"
               :close-on-click-modal="false"
               width="769px">
      <el-form ref="courseDialogFormRef" :model="ruleForm" :rules="rules" size="default" label-width="100px">
        <el-row :gutter="35">
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="课程名称" prop="courseName">
              <el-input v-model="ruleForm.courseName" placeholder="请输入课程名称" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12" class="mb20">
            <el-form-item label="课程类型" prop="courseType">
              <dictSelect v-model="ruleForm.courseType" dictType="courseType" placeholder="请选择课程类型"></dictSelect>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12" class="mb20">
            <el-form-item label="课程状态" prop="status">
              <dictSelect v-model="ruleForm.status" dictType="courseStatus" placeholder="请选择课程状态"></dictSelect>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="课程介绍" prop="courseDescription">
              <el-input v-model="ruleForm.courseDescription" placeholder="请输入课程介绍" clearable type="textarea"
                        :rows="5"></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-row :gutter="10" v-for="(v, k) in classTimeList" :key="k">
              <el-col :xs="24" :sm="12" :md="10" :lg="10" :xl="10" class="mb20">
                <el-form-item>
                  <template #label>
                    <el-button type="primary" circle size="small" @click="onAddRow" v-if="k === 0">
                      <el-icon class="el-icon-plus"></el-icon>
                    </el-button>
                    <el-button type="danger" circle size="small" @click="onDelRow(k)" v-else>
                      <el-icon class="el-icon-delete"></el-icon>
                    </el-button>
                    <span class="ml10">星期</span>
                  </template>
                  <dictSelect v-model="v.dictLabel" dictType="week" placeholder="请选择星期"></dictSelect>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :md="14" :lg="14" :xl="14" class="mb20">
                <el-form-item label="时间">
                  <el-input v-model="v.dictValue" style="width: 100%" placeholder="请输入时间"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
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
import {addCourse, updateCourse} from "@/api/scas/course";

export default {
  components: {
    dictSelect: () => import("@/component/dictSelect/index.vue")
  },
  name: 'CourseDialog',
  data() {
    return {
      dialog: {
        isShowDialog: false,
        type: '',
        title: '',
        submitTxt: '',
      },
      ruleForm: {},
      classTimeList: [{
        dictLabel: '',
        dictValue: ''
      }],
      rules: {
        courseName: [{required: true, message: '请输入课程名称', trigger: 'blur'}],
        courseType: [{required: true, message: '请选择课程类型', trigger: 'change'}],
        status: [{required: true, message: '请选择课程状态', trigger: 'change'}]
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
        this.classTimeList = JSON.parse(row.classTime)
        this.dialog.title = '修改课程';
        this.dialog.submitTxt = '修 改';
      } else {
        this.ruleForm = {
          courseName: '',
          classTime: '',
          courseType: '',
          courseDescription: '',
          status: ''
        }
        this.dialog.title = '新增课程';
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
      this.classTimeList = [{
        dictLabel: '',
        dictValue: ''
      }];
      this.$refs.courseDialogFormRef.resetFields()
    },
    // 新增行
    onAddRow() {
      this.classTimeList.push({
        dictLabel: '',
        dictValue: '',
      });
    },
    // 删除行
    onDelRow(k) {
      this.classTimeList.splice(k, 1);
    },
    /**
     * 新增/修改  保存
     * @param flag
     */
    onSubmit(flag) {
      this.$refs["courseDialogFormRef"].validate(valid => {
        if (valid) {
          this.ruleForm.classTime = JSON.stringify(this.classTimeList)
          if (flag === 'edit') {
            updateCourse(this.ruleForm).then(res => {
              if (res.code === 200) {
                Message.success(res.message);
                this.$emit('refresh');
              }
            })
          } else {
            addCourse(this.ruleForm).then(res => {
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
/* 前面的.el-table可替换为需要设置滚动条的class */
.el-dialog ::-webkit-scrollbar {
  width: 3px !important;
  height: 10px !important;
  border-radius: 5px;
}

.el-dialog ::-webkit-scrollbar-thumb {
  border-radius: 3px;
  -webkit-box-shadow: inset 0 0 3px rgba(0, 0, 0, 0.2);
  /* 滚动条的颜色 */
  background-color: #89898c;
}
</style>
