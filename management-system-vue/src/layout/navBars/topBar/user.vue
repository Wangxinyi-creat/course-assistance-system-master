<template>
  <div class="layout-navbars-breadcrumb-user" :style="{ flex: layoutUserFlexNum }">
    <!--大小调整-->
    <el-dropdown :show-timeout="70" :hide-timeout="50" trigger="click" @command="onComponentSizeChange">
      <div class="layout-navbars-breadcrumb-user-icon">
        <i class="iconfont icon-ziti" :title="'组件大小'"></i>
      </div>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item command="" :disabled="disabledSize === ''">默认</el-dropdown-item>
          <el-dropdown-item command="medium" :disabled="disabledSize === 'medium'">中等</el-dropdown-item>
          <el-dropdown-item command="small" :disabled="disabledSize === 'small'">小型</el-dropdown-item>
          <el-dropdown-item command="mini" :disabled="disabledSize === 'mini'">超小</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
    <!--菜单搜索-->
    <div class="layout-navbars-breadcrumb-user-icon" @click="onSearchClick">
      <i class="el-icon-search" :title="'菜单搜索'"></i>
    </div>
    <!--布局配置-->
    <div class="layout-navbars-breadcrumb-user-icon" @click="onLayoutSetingClick">
      <i class="icon-skin iconfont" :title="'布局配置'"></i>
    </div>
    <!-- 全屏-->
    <div class="layout-navbars-breadcrumb-user-icon mr10" @click="onScreenfullClick">
      <i
          class="iconfont"
          :title="isScreenfull ? '关全屏' : '开全屏'"
          :class="!isScreenfull ? 'icon-fullscreen' : 'icon-tuichuquanping'"
      ></i>
    </div>
    <!-- 个人中心-->
    <el-dropdown :show-timeout="70" :hide-timeout="50" @command="onDropdownCommand">
			<span class="layout-navbars-breadcrumb-user-link">
				<img :src="getUserInfos.headSrc" class="layout-navbars-breadcrumb-user-link-photo mr5"/>
				{{ getUserInfos.userName === '' ? 'test' : getUserInfos.userName }}
				<i class="el-icon-arrow-down el-icon--right"></i>
			</span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item command="/home">首页</el-dropdown-item>
        <el-dropdown-item command="/personal">个人中心</el-dropdown-item>
        <!--        <el-dropdown-item command="/404">404</el-dropdown-item>-->
        <!--        <el-dropdown-item command="/401">401</el-dropdown-item>-->
        <el-dropdown-item divided command="logOut">退出登录</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
    <Search ref="searchRef"/>
  </div>
</template>

<script>
import screenfull from 'screenfull';
import {Session, Local} from '@/utils/storage.js';
import Search from '@/layout/navBars/topBar/search.vue';
import {signOut} from "../../../api/login";

export default {
  name: 'layoutBreadcrumbUser',
  components: {Search},
  data() {
    return {
      isScreenfull: false,
      disabledI18n: 'zh-cn',
      disabledSize: '',
    };
  },
  computed: {
    // 获取用户信息
    getUserInfos() {
      return this.$store.state.userInfos.userInfos.user;
    },
    // 设置弹性盒子布局 flex
    layoutUserFlexNum() {
      let {layout, isClassicSplitMenu} = this.$store.state.themeConfig.themeConfig;
      let num = '';
      if (layout === 'defaults' || (layout === 'classic' && !isClassicSplitMenu) || layout === 'columns') num = 1;
      else num = null;
      return num;
    },
  },
  mounted() {
    if (Local.get('themeConfigPrev')) {
      this.initComponentSize();
    }
  },
  methods: {
    // 搜索点击
    onSearchClick() {
      this.$refs.searchRef.openSearch();
    },
    // 布局配置点击
    onLayoutSetingClick() {
      this.bus.$emit('openSetingsDrawer');
    },
    // 全屏点击
    onScreenfullClick() {
      if (!screenfull.isEnabled) {
        this.$message.warning('暂不不支持全屏');
        return false;
      }
      screenfull.toggle();
      screenfull.on('change', () => {
        if (screenfull.isFullscreen) this.isScreenfull = true;
        else this.isScreenfull = false;
      });
      // 监听菜单 horizontal.vue 滚动条高度更新
      this.bus.$emit('updateElScrollBar');
    },
    // 组件大小改变
    onComponentSizeChange(size) {
      Local.remove('themeConfigPrev');
      this.$store.state.themeConfig.themeConfig.globalComponentSize = size;
      Local.set('themeConfigPrev', this.$store.state.themeConfig.themeConfig);
      this.$ELEMENT.size = size;
      this.initComponentSize();
      window.location.reload();
    },
    // 初始化全局组件大小
    initComponentSize() {
      switch (Local.get('themeConfigPrev').globalComponentSize) {
        case '':
          this.disabledSize = '';
          break;
        case 'medium':
          this.disabledSize = 'medium';
          break;
        case 'small':
          this.disabledSize = 'small';
          break;
        case 'mini':
          this.disabledSize = 'mini';
          break;
      }
    },
    // `dropdown 下拉菜单` 当前项点击
    onDropdownCommand(path) {
      if (path === 'logOut') {
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
          })
              .then(() => {
                // 清除缓存/token等
                Session.clear();
                // 使用 reload 时，不需要调用 resetRoute() 重置路由
                window.location.reload();
              })
              .catch(() => {
              });
        }, 150);
      } else {
        this.$router.push(path);
      }
    },
  },
};
</script>

<style scoped lang="scss">
.layout-navbars-breadcrumb-user {
  display: flex;
  align-items: center;
  justify-content: flex-end;

  &-link {
    height: 100%;
    display: flex;
    align-items: center;
    white-space: nowrap;

    &-photo {
      width: 25px;
      height: 25px;
      border-radius: 100%;
    }
  }

  &-icon {
    padding: 0 10px;
    cursor: pointer;
    color: var(--prev-bg-topBarColor);
    height: 50px;
    line-height: 50px;
    display: flex;
    align-items: center;

    &:hover {
      background: var(--prev-color-hover);

      i {
        display: inline-block;
        animation: logoAnimation 0.3s ease-in-out;
      }
    }
  }

  & ::v-deep .el-dropdown {
    color: var(--prev-bg-topBarColor);
  }

  & ::v-deep .el-badge {
    height: 40px;
    line-height: 40px;
    display: flex;
    align-items: center;
  }

  & ::v-deep .el-badge__content.is-fixed {
    top: 12px;
  }
}
</style>
