<template>
  <el-menu
      router
      background-color="transparent"
      :default-active="defaultActive+''"
      :collapse="setIsCollapse"
      :unique-opened="getThemeConfig.isUniqueOpened"
      :collapse-transition="false"
  >
    <template v-for="(item,index) in menuList">

      <el-submenu :index="item.path+''+index" v-if="item.children && item.children.length > 0" :key="index">
        <template slot="title">
          <i :class="item.elIcon ? item.elIcon : ''"></i>
          <span>{{ item.menuName }}</span>
        </template>
        <SubItem :chil="item.children"/>
      </el-submenu>

      <template v-else>
        <el-menu-item :index="item.path" :key="index">
          <i :class="item.elIcon ? item.elIcon : ''"></i>
          <template slot="title" v-if="!item.isLink || (item.isLink && item.isIframe)">
            <span>{{ item.menuName }}</span>
          </template>
          <template slot="title" v-else>
            <a :href="item.isLink" target="_blank">{{ item.menuName }}</a>
          </template>
        </el-menu-item>
      </template>

    </template>
  </el-menu>
</template>

<script>
import SubItem from '@/layout/navMenu/subItem.vue';

export default {
  name: 'navMenuVertical',
  components: {SubItem},
  props: {
    menuList: {
      type: Array,
      default() {
        return [];
      },
    },
  },
  data() {
    return {
      defaultActive: this.$route.path,
    };
  },
  computed: {
    // 获取布局配置信息
    getThemeConfig() {
      return this.$store.state.themeConfig.themeConfig;
    },
    // 设置左侧菜单是否展开/收起
    setIsCollapse() {
      return document.body.clientWidth < 1000 ? false : this.$store.state.themeConfig.themeConfig.isCollapse;
    },
  },
  watch: {
    // 监听路由的变化
    $route: {
      handler(to) {
        this.defaultActive = to.path;
        const clientWidth = document.body.clientWidth;
        if (clientWidth < 1000) this.$store.state.themeConfig.themeConfig.isCollapse = false;
      },
      deep: true,
    },
  },
};
</script>
