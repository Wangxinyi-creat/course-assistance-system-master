<template>
  <div class="system-user-dialog-container">
    <el-dialog :title="dialog.title"
               :visible.sync="dialog.isShowDialog"
               :close-on-click-modal="false"
               width="769px">
      <el-form ref="userDialogFormRef" :model="ruleForm.user" :rules="rules" size="default" label-width="90px">
        <el-row :gutter="35">
          <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
            <el-form-item label="账户名称" prop="userName">
              <el-input v-model="ruleForm.user.userName" placeholder="请输入账户名称/学工号" clearable
                        maxlength="20"></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
            <el-form-item label="用户昵称" prop="nickName">
              <el-input v-model="ruleForm.user.nickName" placeholder="请输入用户昵称" clearable
                        maxlength="20"></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
            <el-form-item label="性别" prop="sex">
              <dictSelect v-model="ruleForm.user.sex" dictType="sex"
                          placeholder="请选择性别"></dictSelect>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
            <el-form-item label="用户密码" prop="password">
              <el-input v-model="ruleForm.user.password"
                        placeholder="请输入用户密码"
                        type="password"
                        maxlength="16"
                        clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="ruleForm.user.phone" placeholder="请输入手机号" clearable
                        maxlength="11"></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="ruleForm.user.email"  placeholder="请输入"
                        clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
            <el-form-item label="所属学院" prop="deptId">
              <el-cascader ref="ownArea"
                           :props="{value:'id', label: 'deptName', children: 'children',emitPath: false}"
                           :options="deptList"
                           v-model="ruleForm.user.deptId"
                           style="width: 100%"
                           props.expandTrigger="hover"
                           clearable
                           placeholder="请选择所属学院">
              </el-cascader>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
            <el-form-item label="身份" prop="identity">
              <el-select v-model="ruleForm.user.identity" placeholder="请选择身份" style="width: 100%">
                <el-option label="学生" value="学生"></el-option>
                <el-option label="老师" value="老师"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20" v-if="ruleForm.user.identity === '学生'">
            <el-form-item label="专业" prop="major">
              <el-input v-model="ruleForm.user.major" placeholder="请输入专业" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20" v-if="ruleForm.user.identity === '学生'">
            <el-form-item label="班级" prop="className">
              <el-input v-model="ruleForm.user.className" placeholder="请输入班级" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
            <el-form-item label="头像" prop="headSrc">
              <upload-img v-model="ruleForm.user.headSrc"/>
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
import {register} from "@/api/system/user";
import {Message} from "element-ui";

export default {
  components: {
    dictSelect: () => import("@/component/dictSelect/index.vue"),
    uploadImg: () => import("@/component/upload-img/index.vue")
  },
  name: 'userDialog',
  data() {
    return {
      dialog: {
        isShowDialog: false,
        type: '',
        title: '',
        submitTxt: '',
      },
      ruleForm: {
        user: {
          userName: '', // 账户名称
          nickName: '', // 用户昵称
          password: '', // 密码
          phone: '', // 手机号
          email: '', // 邮箱
          sex: '', // 性别
          headSrc: '', // 头像
          identity: '', // 身份
          major: '', // 专业
          className: '', // 班级
          status: true, // 用户状态
        },
        roleSign: [],//关联角色
      },
      deptList: [],
      roles: [],
      rules: {
        userName: [{required: true, message: '请输入账号名称', trigger: 'blur'}],
        password: [{required: true, message: '请输入密码', trigger: 'blur'}],
        sex: [{required: true, message: '请选择性别', trigger: 'change'}],
        phone: [{required: true, message: '请输入手机号', trigger: 'blur'}],
        identity: [{required: true, message: '请选择身份', trigger: 'change'}],
      }
    };
  },
  methods: {
    /**
     * 打开弹框
     */
    openDialog() {
      this.ruleForm = {
        user: {
          userName: '', // 账户名称
          nickName: '', // 用户昵称
          password: '', // 密码
          phone: '', // 手机号
          email: '', // 邮箱
          sex: '', // 性别
          headSrc: '', // 头像
          identity: '', // 身份
          major: '', // 专业
          className: '', // 班级
          status: true, // 用户状态
        },
        roleSign: [],//关联角色
      }
      this.dialog.title = '注册用户';
      this.dialog.submitTxt = '注 册';
      this.dialog.isShowDialog = true;
    },
    /**
     * 关闭弹框
     */
    onCancel() {
      this.dialog.isShowDialog = false;
      this.$refs.userDialogFormRef.resetFields()
    },
    /**
     * 新增/修改  保存
     */
    onSubmit() {
      this.$refs["userDialogFormRef"].validate(valid => {
        if (valid) {
          if (this.ruleForm.user.identity === '老师'){
            this.ruleForm.roleSign = [2]
          }else {
            this.ruleForm.roleSign = [3]
          }
          register(this.ruleForm).then(res => {
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
.el-cascader-panel {
  height: 200px;
}
</style>

