<template>
  <div style="margin: auto;text-align: center">
    <h1 style="font-size: 30px">{{ noticeTable.noticeTitle }}</h1>
    <p style="padding: 10px;margin-left: 55%">{{ noticeTable.createBy }}&nbsp;&nbsp;&nbsp;&nbsp;{{ noticeTable.createTime }}</p>
    <div v-html="noticeTable.noticeContent" style="margin: 0 15%"></div>
  </div>
</template>


<script>
import {getNoticeById} from "@/api/system/notice";

export default {
  name: "checkNotice.vue",
  data() {
    return {
      noticeTable: {}
    }
  },
  mounted() {
    getNoticeById(this.$route.query.id).then(res => {
      this.noticeTable = res.data
    })
  }
}
</script>