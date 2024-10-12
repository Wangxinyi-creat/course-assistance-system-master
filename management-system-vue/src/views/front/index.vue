<template>
  <el-container style="width: 100%">
    <el-header class="header header-style">
      <p style="font-size: 30px; color: #00f1e5; margin: 0;">鸽子管理系统</p>
      <div>
        <el-menu default-active="/frontHome" router class="el-menu-demo container-main" mode="horizontal">
          <el-menu-item index="/frontHome" class="menu-item">首页</el-menu-item>
          <el-menu-item index="/front-my" class="menu-item" v-if="isLogin">我的</el-menu-item>
          <el-menu-item index="/login" class="menu-item" v-else>登录</el-menu-item>
        </el-menu>
      </div>
    </el-header>
    <el-main style="margin: 10px 0">
      <router-view/>
    </el-main>
  </el-container>
</template>


<script>
import {Session} from '@/utils/storage';

export default {
  name: 'frontLayput',
  data() {
    return {
      isLogin: false,
      user: {},
      circleUrl: "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
    };
  },
  created() {
    this.user = Session.get('userInfo');
    if (this.user) this.isLogin = true;
  }
}
</script>

<style scoped>
.header {
  max-width: 1200px;
  margin: auto;
  width: 100%;
  min-height: 100%;
}

.header-style {
  margin-top: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

::v-deep .el-main {
  padding: 0;
}

::v-deep .container-main {
  background-color: transparent;
}

::v-deep .el-cascader-panel {
  height: 300px;
}

::v-deep .el-header {
  padding: 10px 20px;
}

.menu-item {
  font-size: 26px;
}

.el-menu--horizontal > .el-menu-item.is-active, .el-menu--horizontal > .el-submenu.is-active .el-submenu__title {
  border-bottom-color: transparent !important;
}

.footer {
  margin: auto;
  font-size: 16px;
}
</style>
