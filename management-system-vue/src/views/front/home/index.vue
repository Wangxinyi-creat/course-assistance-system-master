<template>
  <div>
    <div class="heads">
      <div class="carousel"></div>
      <el-carousel :interval="5000" height="500px">
        <el-carousel-item v-for="(item,index) in carouselImgList" :key="index">
          <a :href="item.link" target="_blank">
            <el-image :src="item.carouselUrl" style="width: 100%;height: 100%"></el-image>
          </a>
        </el-carousel-item>
      </el-carousel>
    </div>
  </div>
</template>

<script>
import {carouselImgList} from './home'

export default {
  name: 'frontHome',
  data() {
    return {
      carouselImgList,
    }
  },
  created() {

  },
  methods: {}
}
</script>

<style scoped>
.heads {
  border-radius: 6px;
  max-width: 1200px;
  margin: auto;
  width: 100%;
}

.carousel {
  margin-bottom: 20px;
}
</style>
