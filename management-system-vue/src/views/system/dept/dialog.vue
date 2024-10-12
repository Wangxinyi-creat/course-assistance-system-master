<template>
  <div class="system-menu-dialog-container">
    <el-dialog :title="dialog.title"
               :visible.sync="dialog.isShowDialog"
               :close-on-click-modal="false"
               width="769px">
      <el-form ref="deptDialogFormRef" :model="ruleForm" :rules="rules" size="default" label-width="100px">
        <el-row :gutter="35">
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="上级学院">
              <el-cascader
                :options="deptData"
                :props="{ checkStrictly: true, value: 'id', label: 'deptName' ,emitPath: false}"
                placeholder="请选择上级学院"
                clearable
                class="w100"
                v-model="ruleForm.parentId"
              >
                <template #default="{ node, data }">
                  <span>{{ data.deptName }}</span>
                  <span v-if="!node.isLeaf"> ({{ data.children.length }}) </span>
                </template>
              </el-cascader>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="学院名称" prop="deptName">
              <el-input v-model="ruleForm.deptName" placeholder="请输入学院名称" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="负责人" prop="leader">
              <el-input v-model="ruleForm.leader" placeholder="请输入负责人" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="ruleForm.phone" placeholder="请输入联系电话" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="邮箱地址" prop="email">
              <el-input v-model="ruleForm.email" placeholder="请输入邮箱地址" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="学院状态">
              <el-switch
                v-model="ruleForm.status"
                active-color="#13ce66"
                inactive-color="#ff4949"
                active-text="正常"
                inactive-text="停用">
              </el-switch>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="排序">
              <el-input-number v-model="ruleForm.sortId" controls-position="right" placeholder="请输入排序"/>
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
import {addDept, updateDept} from "@/api/system/dept";

export default {
  name: 'DeptDialog',
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
      deptData: [],
      rules: {deptName: [{required: true, message: '请输入学院名称', trigger: 'blur'}]}
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
        if (this.ruleForm.isLink != null) this.isLinkFlag = true;
        this.dialog.title = '修改学院';
        this.dialog.submitTxt = '修 改';
      } else {
        this.ruleForm = {
          menuType: 0
        }
        if (row !== 0) {
          this.ruleForm.parentId = row.id;
        } else {
          this.ruleForm.parentId = 0;
        }
        this.ruleForm.status = true;
        this.dialog.title = '新增学院';
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
      this.$refs.deptDialogFormRef.resetFields()
    },
    /**
     * 新增/修改  保存
     * @param flag
     */
    onSubmit(flag) {
      this.$refs["deptDialogFormRef"].validate(valid => {
        if (valid) {
          if (flag === 'edit') {
            updateDept(this.ruleForm).then(res => {
              if (res.code === 200) {
                Message.success(res.message);
                this.$emit('refresh');
              }
            })
          } else {
            addDept(this.ruleForm).then(res => {
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
