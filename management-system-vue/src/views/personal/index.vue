<template>
  <div class="personal">
    <!-- 用户信息 -->
    <el-row :gutter="15">
      <el-col :md="24" :lg="16" :xl="16" class="mb15">
        <el-card shadow="hover">
          <div slot="header">
            <span>我的工作台</span>
          </div>
          <div class="user-item">
            <div class="user-item-left">
              <el-upload class="h100 personal-user-left-upload"
                         :action="baseURL"
                         :show-file-list="false"
                         :on-success="handleAvatarSuccess"
                         :before-upload="beforeAvatarUpload">
                <img :src="getUserInfos.headSrc"/>
              </el-upload>
            </div>
            <div class="user-item-right overflow">
              <el-row>
                <el-col :span="24" class="right-title mb15 one-text-overflow"
                >{{ currentTime }}，{{ getUserInfos.userName }}，{{ dailyMessage }}
                </el-col>
                <el-col :span="24">
                  <el-col :xs="12" :sm="12" :md="8" class="right-l-v">
                    <div class="right-label">昵称：</div>
                    <div class="right-value">{{ getUserInfos.nickName }}</div>
                  </el-col>
                  <el-col :xs="12" :sm="12" :md="16" class="right-l-v">
                    <div class="right-label">身份：</div>
                    <div class="right-value">
                      {{ getUserInfos.identity }}
                    </div>
                  </el-col>
                </el-col>
                <el-col :span="24" class="mt5" v-if="getUserInfos.identity === '学生'">
                  <el-col :xs="12" :sm="12" :md="8" class="right-l-v">
                    <div class="right-label one-text-overflow">专业：</div>
                    <div class="right-value one-text-overflow">{{ getUserInfos.major }}</div>
                  </el-col>
                  <el-col :xs="12" :sm="12" :md="16" class="right-l-v">
                    <div class="right-label one-text-overflow">班级：</div>
                    <div class="right-value one-text-overflow">{{ getUserInfos.major }}</div>
                  </el-col>
                </el-col>
                <el-col :span="24" class="mt5">
                  <el-col :xs="12" :sm="12" :md="8" class="right-l-v">
                    <div class="right-label one-text-overflow">IP：</div>
                    <div class="right-value one-text-overflow">{{ IP }}</div>
                  </el-col>
                  <el-col :xs="12" :sm="12" :md="16" class="right-l-v">
                    <div class="right-label one-text-overflow">时间：</div>
                    <div class="right-value one-text-overflow">{{ userInfo.time }}</div>
                  </el-col>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-card>
      </el-col>
      <!--消息通知-->
      <el-col :md="24" :lg="8" :xl="8" class="mb15">
        <el-card shadow="hover">
          <div slot="header">
            <span>消息通知</span>
            <el-button class="personal-card-more" type="text" v-if="newsInfoList.length>0"
                       @click="onOpenNotice">更多
            </el-button>
          </div>
          <div class="info">
            <Scroll :data="newsInfoList" class="info-scroll" :class-option="optionSingleHeight">
              <ul class="info-ul" v-if="newsInfoList.length>0">
                <li v-for="(v, k) in newsInfoList" :key="k" class="info-item"
                    @click="onNewsInfoListClick(v)">
                  <div class="info-item-left"
                       v-text="(v.noticeType===1?'[通知]':'[公告]')+v.noticeTitle"></div>
                  <div class="info-item-right" v-text="v.createTime"></div>
                </li>
              </ul>
              <el-empty description="暂无消息通知" v-else :image-size="50"></el-empty>
            </Scroll>
          </div>
        </el-card>
      </el-col>
      <!-- 更新信息 -->
      <el-col :span="24">
        <el-card shadow="hover" class="personal-edit" header="更新信息">
          <div class="personal-edit-title">
            <span>基本信息</span>
            <el-button class="personal-card-more" type="text" @click="replace()" v-if="!replaceFlag">
              更新个人信息
            </el-button>
            <el-button class="personal-card-more" type="text" @click="replace()" v-else>取 消</el-button>
          </div>
          <el-form :model="personalForm" size="small" label-width="40px" class="mt35 mb35"
                   :disabled='!replaceFlag'>
            <el-row :gutter="35">
              <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="4">
                <el-form-item label="昵称">
                  <el-input v-model="personalForm.nickName" placeholder="请输入昵称"
                            clearable></el-input>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="4">
                <el-form-item label="邮箱">
                  <el-input v-model="personalForm.email" placeholder="请输入邮箱"
                            clearable></el-input>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="4">
                <el-form-item label="手机">
                  <el-input v-model="personalForm.phone" placeholder="请输入手机"
                            clearable></el-input>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="4">
                <el-form-item label="性别">
                  <el-select v-model="personalForm.sex" placeholder="请选择性别" clearable
                             class="w100">
                    <el-option label="男" :value="1"></el-option>
                    <el-option label="女" :value="2"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="4" v-if="replaceFlag">
                <el-form-item>
                  <el-button type="primary" icon="el-icon-edit" @click="replaceSave">提交更新
                  </el-button>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <div class="personal-edit-title">账号安全</div>
          <div class="personal-edit-safe-box">
            <div class="personal-edit-safe-item">
              <div class="personal-edit-safe-item-left">
                <div class="personal-edit-safe-item-left-label">账户密码</div>
              </div>
              <div class="personal-edit-safe-item-right">
                <el-button text type="primary" size="small" @click="dialogVisible = true">立即修改
                </el-button>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!--修改密码-->
    <el-dialog
        title="修改密码"
        :close-on-click-modal="false"
        :visible.sync="dialogVisible"
        :before-close="reset"
        width="35%">
      <el-form ref="userDialogFormRef" :model="modifyPwdForm" :rules="rules" size="default" label-width="100px">
        <el-form-item label="旧密码" prop="oldPassword">
          <el-input v-model="modifyPwdForm.oldPassword"
                    placeholder="请输入旧密码"
                    show-password
                    clearable
                    maxlength="16"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword1">
          <el-input v-model="modifyPwdForm.newPassword1"
                    placeholder="请输入新密码"
                    show-password
                    clearable
                    maxlength="16"></el-input>
        </el-form-item>
        <el-form-item label="确认新密码" prop="newPassword2">
          <el-input v-model="modifyPwdForm.newPassword2"
                    placeholder="请重新输入新密码"
                    show-password
                    clearable
                    maxlength="16"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
				<span class="dialog-footer">
					<el-button @click="reset()" size="small">取 消</el-button>
					<el-button type="primary" @click="onSubmit" size="small">确定</el-button>
				</span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import Scroll from 'vue-seamless-scroll';
import {Session} from '@/utils/storage';
import {formatAxis, formatDate} from '@/utils/style/formatTime';
import {dailyMessage} from './mock';
import {updatePersonal, updatePwd} from "@/api/system/user";
import {Message} from "element-ui";
import {getNotice} from "@/api/system/notice";
import {CheckNoticeRouter} from "@/utils/addRouter";
import axios from "axios";

export default {
  name: 'personal',
  components: {Scroll},
  data() {
    return {
      baseURL: process.env.VUE_APP_APIUPLOAD,
      newsInfoList: [],
      userInfo: {},
      dailyMessage: {},
      replaceFlag: false,
      personalForm: {},
      dialogVisible: false,
      modifyPwdForm: {},
      IP: '',
      rules: {
        oldPassword: [{required: true, message: '请输入旧密码', trigger: 'blur'}],
        newPassword1: [{required: true, message: '请输入新密码', trigger: 'blur'}],
        newPassword2: [{required: true, message: '请输入确认新密码', trigger: 'blur'}],
      }
    };
  },
  created() {
    this.personalForm = {...this.$store.state.userInfos.userInfos.user}
    this.getNotice();// 获取消息通知
    this.initUserInfo();// 初始化登录信息
    this.initDailyMessage();// 随机语录
    //获取ip
    axios.get('http://api.ipify.org?format=json').then(response => {
      this.IP = response.data.ip
    })
  },
  computed: {
    currentTime() {
      return formatAxis(new Date());
    },
    optionSingleHeight() {
      return {
        singleHeight: 28,
        limitMoveNum: 8,
        waitTime: 2000,
      };
    },
    getUserInfos() {
      return this.$store.state.userInfos.userInfos.user;
    },
  },
  methods: {
    // 随机语录
    initDailyMessage() {
      this.dailyMessage = dailyMessage[Math.floor(Math.random() * dailyMessage.length)];
    },
    // 初始化登录信息
    initUserInfo() {
      if (!Session.get('userInfo')) return false;
      this.userInfo = Session.get('userInfo');
      this.userInfo.time = formatDate(new Date(this.userInfo.time), 'YYYY-mm-dd HH:MM:SS');
    },
    replace() {
      this.replaceFlag = !this.replaceFlag
      this.personalForm = {...this.$store.state.userInfos.userInfos.user}
    },
    getNotice() {
      const param = {
        status: 1,
        pageNum: 1,
        pageSize: 10,
      }
      getNotice(param).then(res => {
        this.newsInfoList = res.data.list;
      })
    },
    /**
     * 提交个人信息修改
     */
    replaceSave() {
      updatePersonal(this.personalForm).then(res => {
        if (res.code === 200) {
          //更新vuex数据
          this.loadVueX()
          this.$notify.success({
            title: '提示',
            message: '修改成功'
          });
        }
      })
      this.replaceFlag = false
    },
    /**
     * 更新vuex数据
     */
    loadVueX() {
      const userInfos = {
        user: this.personalForm,
        time: new Date().getTime(),
        authBtnList: this.$store.state.userInfos.userInfos.authBtnList,
      };
      this.$store.dispatch('userInfos/setUserInfos', userInfos);
      Session.set('userInfo', userInfos);
    },
    /**
     * 图片上传成功回调
     * @param res
     * @param file
     */
    handleAvatarSuccess(res, file) {
      this.personalForm.headSrc = res.data;
      this.replaceSave()
    },
    /**
     * 图片上传前调用
     * @param file
     * @returns {boolean}
     */
    beforeAvatarUpload(file) {
      const isPNG = file.type === 'image/png';
      const isJPG = file.type === 'image/jpeg';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG && !isPNG) {
        Message.error('上传头像图片只能是 JPG 或 PNG 格式!');
      }
      if (!isLt2M) {
        Message.error('上传头像图片大小不能超过 2MB!');
      }
      const isIMG = isJPG || isPNG
      return isIMG && isLt2M;
    },
    onSubmit() {
      updatePwd(this.getUserInfos.id, this.modifyPwdForm).then(res => {
        if (res.code === 200) {
          this.$notify.success({
            title: '提示',
            message: '修改成功'
          });
          this.dialogVisible = false
        }
      })
    },
    // 消息通知当前项点击
    onNewsInfoListClick(v) {
      CheckNoticeRouter()//查询公告详情路由
      this.$router.push({name: 'checkNotice', query: {id: v.noticeId}})
    },
    // 跳转到通知公告
    onOpenNotice() {
      this.$router.push('/notice');
    },
    reset() {
      this.dialogVisible = false
      this.$refs.userDialogFormRef.resetFields()
      this.modifyPwdForm = {}
    }
  },
};
</script>

<style scoped lang="scss">
@import './index.scss';
</style>
