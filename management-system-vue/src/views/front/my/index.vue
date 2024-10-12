<template>
  <div class="container-main">
    <!--个人信息-->
    <el-card style="border-radius: 12px;margin-top: 10px">
      <div style="padding: 20px">
        <el-descriptions title="用户信息">
          <el-descriptions-item label="用户账户">{{ user.userName }}</el-descriptions-item>
          <el-descriptions-item label="用户昵称">{{ user.nickName }}</el-descriptions-item>
          <el-descriptions-item label="性别">{{ user.sex === 1 ? '男' : '女' }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ user.phone }}</el-descriptions-item>
          <el-descriptions-item label="邮箱">{{ user.email }}</el-descriptions-item>
          <el-descriptions-item label="身份证号码">{{ user.idCard }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>
    <!--可扩展-->
    <!--    <el-card class="card1">-->
    <!--      <div class="card1-block" @click="goToMyView">-->
    <!--        <p class="p-style">-->
    <!--          <i class="el-icon-view" style="font-size: 30px"/>-->
    <!--          <span style="display: block">正在学习</span>-->
    <!--        </p>-->
    <!--      </div>-->
    <!--      <div class="card1-block" @click="goToMyCollect">-->
    <!--        <p class="p-style">-->
    <!--          <i class="el-icon-star-on" style="font-size: 30px"></i>-->
    <!--          <span style="display: block">已经完成</span>-->
    <!--        </p>-->
    <!--      </div>-->
    <!--    </el-card>-->
    <!--更多服务-->
    <el-card class="card2">
      <div slot="header" class="clearfix">
        <span>更多服务</span>
      </div>
      <div class="card2-content">
        <p style="margin:10px 0 10px 10px" @click="edit1()">
          <i class="el-icon-edit" style="margin-right: 6px"/>修改信息
        </p>
        <p style="margin:20px 0 10px 10px" @click="edit2()">
          <i class="el-icon-edit-outline" style="margin-right: 6px"/>修改密码
        </p>
      </div>
    </el-card>
    <div style="width: 200px;margin: 50px auto">
      <el-button round class="button-style" style="width: 200px;height: 50px" @click="logout">退出登录</el-button>
    </div>

    <!--修改弹框-->
    <el-dialog title="修改个人基本信息"
               :visible.sync="isShowDialog"
               :close-on-click-modal="false"
               width="769px">
      <el-form ref="DialogFormRef1" :model="ruleForm" :rules="rules" size="default" label-width="90px">
        <el-row :gutter="35">
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="账户名称" prop="userName">
              <el-input v-model="ruleForm.userName" placeholder="请输入账户名称" clearable
                        maxlength="20"></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="用户昵称" prop="nickName">
              <el-input v-model="ruleForm.nickName" placeholder="请输入用户昵称" clearable
                        maxlength="20"></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="性别" prop="sex">
              <dictSelect v-model="ruleForm.sex" dictType="sex"
                          placeholder="请选择性别"></dictSelect>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="ruleForm.phone" placeholder="请输入手机号" clearable
                        maxlength="11"></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="ruleForm.email" placeholder="请输入" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="身份证号码" prop="idCard">
              <el-input v-model="ruleForm.idCard" placeholder="请输入" clearable></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
				<span class="dialog-footer">
					<el-button @click="onCancel" size="default">取 消</el-button>
					<el-button type="primary" @click="onSubmit()" size="default">提交</el-button>
				</span>
      </template>
    </el-dialog>

    <el-dialog title="修改密码"
               :visible.sync="isShowDialog2"
               :close-on-click-modal="false"
               width="769px">
      <el-form ref="DialogFormRef2" :model="ruleForm2" :rules="rules2" size="default" label-width="90px">
        <el-row :gutter="35">
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="旧密码" prop="oldPassword">
              <el-input v-model="ruleForm2.oldPassword"
                        placeholder="请输入用户密码"
                        type="password"
                        maxlength="16"
                        clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="新密码" prop="newPassword1">
              <el-input v-model="ruleForm2.newPassword1"
                        placeholder="请输入新密码"
                        type="password"
                        maxlength="16"
                        clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="新密码" prop="newPassword2">
              <el-input v-model="ruleForm2.newPassword2"
                        placeholder="请再输入新密码"
                        type="password"
                        maxlength="16"
                        clearable></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
				<span class="dialog-footer">
					<el-button @click="onCancel" size="default">取 消</el-button>
					<el-button type="primary" @click="onSubmit2()" size="default">提交</el-button>
				</span>
      </template>
    </el-dialog>
  </div>
</template>


<script>
import {Session} from "@/utils/storage";
import {updatePersonal, updatePwd,} from "@/api/system/user";
import {signOut} from "@/api/login";

export default {
  components: {
    dictSelect: () => import("@/component/dictSelect/index.vue")
  },
  name: 'my',
  data() {
    return {
      circleUrl: "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
      isShowDialog: false,
      isShowDialog2: false,
      ruleForm: {},
      ruleForm2: {},
      rules: {
        userName: [{required: true, message: '请输入账号名称', trigger: 'blur'}],
        password: [{required: true, message: '请输入密码', trigger: 'blur'}],
        sex: [{required: true, message: '请选择性别', trigger: 'change'}],
        phone: [{required: true, message: '请输入手机号', trigger: 'blur'}]
      },
      rules2: {
        oldPassword: [{required: true, message: '请输入密码', trigger: 'blur'}],
        newPassword1: [{required: true, message: '请输入密码', trigger: 'blur'}],
        newPassword2: [{required: true, message: '请输入密码', trigger: 'blur'}]
      },
      user: {},
      authBtnList: [],
    };
  },
  created() {
    if (!Session.get('userInfo')) {
      this.$router.push('/login');
      return
    }
    this.user = Session.get('userInfo').user;
    this.authBtnList = Session.get('userInfo').authBtnList;
  },
  methods: {
    goToMyView() {
      this.$router.push("/front-my-view")
    },
    goToMyCollect() {
      this.$router.push("/front-my-collect")
    },
    logout() {
      setTimeout(() => {
        this.$msgbox({
          closeOnClickModal: false,
          closeOnPressEscape: false,
          title: '提示',
          message: '此操作将退出登录, 是否继续?',
          showCancelButton: true,
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          beforeClose: (action, instance, done) => {
            if (action === 'confirm') {
              signOut().then(res => {
                instance.confirmButtonLoading = true;
                instance.confirmButtonText = '退出中';
                setTimeout(() => {
                  done();
                  setTimeout(() => {
                    instance.confirmButtonLoading = false;
                  }, 300);
                }, 700);
              })
            } else {
              done();
            }
          },
        }).then(() => {
          // 清除缓存/token等
          Session.clear();
          // 使用 reload 时，不需要调用 resetRoute() 重置路由
          window.location.reload();
        })
            .catch(() => {
            });
      }, 150);
      setTimeout(() => {
        this.$router.push('/');
      }, 2000)
    },
    edit1() {
      this.isShowDialog = true
      this.ruleForm = {...this.user}
    },
    edit2() {
      this.isShowDialog2 = true
      this.ruleForm = {...this.user}
    },
    /**
     * 关闭弹框
     */
    onCancel() {
      this.isShowDialog = false;
      this.isShowDialog2 = false;
      this.$refs.DialogFormRef1.resetFields()
      this.$refs.DialogFormRef2.resetFields()
    },
    onSubmit() {
      this.$refs["DialogFormRef1"].validate(valid => {
        if (valid) {
          updatePersonal(this.ruleForm).then(res => {
            if (res.code === 200) {
              //更新vuex数据
              this.loadVueX()
              this.$notify.success({
                title: '提示',
                message: '修改成功'
              });
              this.onCancel();
            }
          })
        }
      })
    },
    /**
     * 更新vuex数据
     */
    loadVueX() {
      const userInfos = {
        user: this.ruleForm,
        time: new Date().getTime(),
        authBtnList: this.authBtnList,
      };
      this.$store.dispatch('userInfos/setUserInfos', userInfos);
      this.user = this.ruleForm;
      Session.set('userInfo', userInfos);
    },
    onSubmit2() {
      this.$refs["DialogFormRef2"].validate(valid => {
        if (valid) {
          updatePwd(this.getUserInfos.id, this.ruleForm2).then(res => {
            if (res.code === 200) {
              this.$notify.success({
                title: '提示',
                message: '修改成功'
              });
              this.dialogVisible = false
            }
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.container-main {
  border-radius: 6px;
  max-width: 1200px;
  margin: auto;
  width: 100%;
  min-height: 100%;
}

.card1 {
  height: 80px;
  border-radius: 12px;
  margin-top: 20px;
}

.card1-block {
  cursor: pointer;
  width: 50%;
  height: 80px;
  display: inline-block;

p {
  text-align: center;
  font-size: 15px;
}

.p-style {
  padding: 15px
}

}

.card1-block:hover {
  background-color: #2EB976;
}

.card2 {
  height: 180px;
  border-radius: 12px;
  margin-top: 20px;

.card2-content {
  padding: 20px;

p {
  cursor: pointer;
  font-size: 16px;
}

p:hover {
  color: #409eff;
}

}
}
</style>

<style scoped>
::v-deep .el-card__body {
  padding: 0;
}

::v-deep .el-card__header {
  background-color: #bcc9c0;
}
</style>
