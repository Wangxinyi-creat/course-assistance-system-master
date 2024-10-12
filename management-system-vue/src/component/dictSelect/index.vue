<template>
  <el-select :value="value === null ? '' : value+''" @input="$emit('input', $event)" :placeholder="placeholder" clearable class="w100" :size="size">
    <el-option :label="data.dictLabel" v-for="data in dataList" :key="data.dictValue" :value="data.dictValue">{{ data.dictLabel }}</el-option>
  </el-select>
</template>

<script>
import {getByDictType} from "@/api/system/dict";

export default {
  name: 'dictSelect',
  props: {
    dictType: {
      type: String,
      default: ''
    },
    value: [Number, String],
    placeholder: String,
    size:{
      type: String,
      default: ''
    }
  },
  data() {
    return {
      dataList: []
    }
  },
  created() {
    getByDictType(this.dictType).then(res => {
      this.dataList = res.data
    })
  }
}
</script>
