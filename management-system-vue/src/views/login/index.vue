<template>
  <div class="login">
    <div class="login-weaper">
      <div class="login-left">
        <div class="login-time">{{ time.txt }}</div>
        <div class="login-left-box">
          <div>
<!--            <div class="logo">{{ getThemeConfig.globalViceTitle }}</div>-->
            <h2 class="title">{{ getThemeConfig.globalViceDes }}</h2>
            <div class="msg">
              <div class="msg-author">
                <span>{{ quotations.name }}</span>
                <span>{{ quotations.comeFrom }}</span>
              </div>
              <div class="msg-txt">{{ quotations.content }}</div>
            </div>
          </div>
        </div>
      </div>
      <div class="login-right">
        <div class="login-main">
          <h4 class="login-title">{{ getThemeConfig.globalTitle }}</h4>
          <el-form class="el-form login-form" :rules="rules" ref="loginForm" :model="ruleForm">
            <el-form-item style="margin-left: 0px" prop="userName">
              <el-input
                type="text"
                :placeholder="'请输入用户名'"
                prefix-icon="el-icon-user"
                v-model="ruleForm.userName"
                clearable
                autocomplete="off"
                @keyup.enter.native="submitForm"
              >
              </el-input>
            </el-form-item>
            <el-form-item style="margin-left: 0px" prop="password">
              <el-input
                type="password"
                :placeholder="'请输入密码'"
                prefix-icon="el-icon-lock"
                v-model="ruleForm.password"
                autocomplete="off"
                :show-password="true"
                @keyup.enter.native="submitForm"
              >
              </el-input>
            </el-form-item>
            <el-form-item style="margin-left: 0px" prop="code">
              <div class="el-row" span="24">
                <div class="el-col el-col-16">
                  <el-input
                    type="text"
                    maxlength="5"
                    :placeholder="'请输入验证码'"
                    prefix-icon="el-icon-position"
                    v-model="ruleForm.code"
                    clearable
                    autocomplete="off"
                    @keyup.enter.native="submitForm"
                  ></el-input>
                </div>
                <div class="el-col el-col-8">
                  <div class="login-code">
                    <img :src="captchaPath" @click="getCaptcha()" class="login-code-img">
                  </div>
                </div>
              </div>
              <span class="register" @click="registerHandle()">没有账号，点击注册</span>
            </el-form-item>
            <el-form-item style="margin: 15px 0 0">
              <el-button type="primary" class="login-submit" @click="submitForm" :loading="submit.loading">
                <span>登 录</span>
              </el-button>
            </el-form-item>
          </el-form>
          <!--          <div class="login-menu">-->
          <!--            <a href="javascript:;">第三方登录</a>-->
          <!--            <a href="javascript:;">友情链接</a>-->
          <!--          </div>-->
        </div>
      </div>
    </div>
    <div class="vue-particles">
      <vue-particles color="#dedede" shapeType="star" linesColor="#dedede" hoverMode="grab" clickMode="push"
                     style="height: 100%"></vue-particles>
    </div>

    <UserDialog ref="userDialogRef"/>
  </div>
</template>

<script>
import {Session} from '@/utils/storage';
import {formatDate, formatAxis} from '@/utils/style/formatTime';
import {PrevLoading} from '@/utils/style/loading.js';
import {quotationsList} from './mock';
import {getCaptcha, signIn} from "@/api/login";
import {getDeptList} from "@/api/system/dept";


export default {
  name: 'login',
  data() {
    return {
      quotationsList,
      quotations: {},
      isView: false,
      submit: {
        loading: false,
      },
      ruleForm: {
        userName: '',
        password: '',
        code: ''
      },
      imgText: '',
      time: {
        txt: '',
        fun: null,
      },
      deptList: [],
      captchaPath: '',
      rules: {
        userName: [{required: true, message: '请输入账号名称', trigger: 'blur'}],
        password: [{required: true, message: '请输入密码', trigger: 'blur'}],
        code: [{required: true, message: '请输入验证码', trigger: 'blur'}],
      }
    };
  },
  components: {
    UserDialog: () => import('./dialog.vue')
  },
  computed: {
    // 获取当前时间
    currentTime() {
      return formatAxis(new Date());
    },
    // 获取布局配置信息
    getThemeConfig() {
      return this.$store.state.themeConfig.themeConfig;
    },
  },
  created() {
    this.initTime();
    this.getCaptcha()
    getDeptList().then(res => {
      this.deptList = res.data
    })
  },
  mounted() {
    this.initRandomQuotations();
  },
  methods: {
    registerHandle(){
      this.$refs.userDialogRef.deptList = this.deptList
      this.$refs.userDialogRef.openDialog()
    },
    // 随机语录
    initRandomQuotations() {
      this.quotations = this.quotationsList[Math.floor(Math.random() * this.quotationsList.length)];
    },
    // 初始化左上角时间显示
    initTime() {
      this.time.txt = formatDate(new Date(), 'YYYY-mm-dd HH:MM:SS WWW QQQQ');
      this.time.fun = setInterval(() => {
        this.time.txt = formatDate(new Date(), 'YYYY-mm-dd HH:MM:SS WWW QQQQ');
      }, 1000);
    },
    //获取验证码
    getCaptcha() {
      getCaptcha().then(res => {
        this.captchaPath = res.data.imgBase64
        this.imgText = res.data.imgText
      })
    },
    // 登录按钮点击
    submitForm() {
      if (this.imgText.toLowerCase() !== this.ruleForm.code.toLowerCase()) {
        this.$message.error('验证码错误')
        this.getCaptcha()
        return
      }
      this.$refs["loginForm"].validate(valid => {
        console.log(valid)
        if (valid) {
          this.submit.loading = true;
          signIn(this.ruleForm).then(async (res) => {
            const userInfos = {
              user: res.data.user,
              time: new Date().getTime(),
              authBtnList: res.data.permissions,
            };
            // 存储 token 到浏览器缓存
            Session.set('token', res.data.token);
            // 存储用户信息到浏览器缓存
            Session.set('userInfo', userInfos);
            // 存储用户信息到vuex
            await this.$store.dispatch('userInfos/setUserInfos', userInfos);
            PrevLoading.start();
            if (res.data.permissions.length === 1) {
              await this.$router.push('/');
            } else {
              await this.$router.push('/admin');
            }
            setTimeout(() => {
              this.$message.success(`${this.currentTime}，${'欢迎回来！'}`);
            }, 300);
          }).catch(() => {
            this.getCaptcha()
            this.submit.loading = false;
          });
        }
      })
    },
  },
  destroyed() {
    clearInterval(this.time.fun);
  },
};
</script>

<style scoped lang="scss">
.register {
  float: right;
  font-size: 14px;
  color: #409eff;
  cursor: pointer;
}

.register:hover {
  color: #1e6b5f;
}
.login {
  height: 100%;
  width: 100%;
  overflow: hidden;
  display: flex;
  position: relative;

  .vue-particles {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: radial-gradient(ellipse at top left, rgba(105, 155, 200, 1) 0%, rgba(181, 197, 216, 1) 57%);
  }

  .login-weaper {
    margin: auto;
    height: 500px;
    display: flex;
    box-sizing: border-box;
    position: relative;
    z-index: 1;
    border: none;
    box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);

    .login-left {
      width: 491px;
      padding: 20px;
      font-size: 16px;
      color: var(--prev-color-text-white);
      position: relative;
      background-color: var(--prev-color-primary);
      display: flex;
      flex-direction: column;
      border-top-left-radius: 4px;
      border-bottom-left-radius: 4px;

      .login-time {
        width: 100%;
        color: var(--prev-color-text-white);
        opacity: 0.9;
        font-size: 14px;
        overflow: hidden;
      }

      .login-left-box {
        flex: 1;
        overflow: hidden;
        position: relative;
        z-index: 1;
        display: flex;
        align-items: center;
        padding: 80px 45px;

        .logo {
          font-size: 22px;
          margin-bottom: 15px;
        }

        .title {
          color: var(--prev-color-text-white);
          font-weight: 300;
          letter-spacing: 2px;
          font-size: 26px;
        }

        .msg {
          color: var(--prev-color-text-white);
          font-size: 13px;
          margin-top: 35px;

          .msg-author {
            opacity: 0.6;

            span:last-of-type {
              margin-left: 15px;
            }
          }

          .msg-txt {
            margin-top: 15px;
            line-height: 22px;
          }
        }
      }
    }

    .login-right {
      width: 491px;
      padding: 20px;
      position: relative;
      align-items: center;
      display: flex;
      background-color: var(--prev-bg-white);
      border-top-right-radius: 4px;
      border-bottom-right-radius: 4px;

      .login-main {
        margin: 0 auto;
        width: 70%;

        .login-title {
          color: var(--prev-color-text-primary);
          margin-bottom: 40px;
          font-weight: 500;
          font-size: 22px;
          text-align: center;
          letter-spacing: 4px;
        }

        .login-form {
          margin: 10px 0;

          i {
            color: var(--prev-color-text-primary);
          }

          .el-form-item {
            margin-bottom: 20px !important;

            .login-code {
              display: flex;
              align-items: center;
              justify-content: space-around;
              margin: 0 0 0 10px;
              user-select: none;

              .login-code-img {
                margin-top: 2px;
                width: 100px;
                height: 38px;
                border: 1px solid var(--prev-border-color-base);
                color: var(--prev-color-text-primary);
                font-size: 14px;
                font-weight: 700;
                letter-spacing: 5px;
                line-height: 38px;
                text-indent: 5px;
                text-align: center;
                cursor: pointer;
                transition: all ease 0.2s;
                border-radius: 4px;

                &:hover {
                  border-color: var(--prev-border-color-hover);
                  transition: all ease 0.2s;
                }
              }
            }

            .login-submit {
              width: 100%;
              letter-spacing: 2px;
            }
          }
        }

        .login-menu {
          margin-top: 30px;
          width: 100%;
          text-align: left;

          a {
            color: var(--prev-color-text-secondary);
            font-size: 12px;
            margin: 0 8px;
            text-decoration: none;

            &:hover {
              color: var(--prev-color-primary);
              text-decoration: underline;
            }
          }
        }
      }
    }
  }
}
</style>
