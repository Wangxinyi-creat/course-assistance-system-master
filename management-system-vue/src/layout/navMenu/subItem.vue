<template>
  <div>
    <template v-for="(item,index) in chil">

      <el-submenu :index="item.path" :key="index" v-if="item.children && item.children.length > 0">
        <template slot="title">
          <i :class="item.elIcon"></i>
          <span>{{ item.menuName }}</span>
        </template>
        <sub-item :chil="item.children"/>
      </el-submenu>

      <template v-else>
        <el-menu-item :index="item.path" :key="index">
          <template v-if="!item.isLink || (item.isLink && item.isIframe)">
            <i :class="item.elIcon ? item.elIcon : ''"></i>
            <span>{{ item.menuName }}</span>
          </template>
          <template v-else>
            <a :href="item.isLink" target="_blank">
              <i :class="item.elIcon ? item.elIcon : ''"></i>
              {{ item.menuName }}
            </a>
          </template>
        </el-menu-item>
      </template>

    </template>
  </div>
</template>

<script>
export default {
  name: 'subItem',
  props: {
    chil: {
      type: Array,
      default() {
        return [];
      },
    },
  },
};
</script>
