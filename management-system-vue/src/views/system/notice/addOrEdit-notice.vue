<template>
  <div class="tools-warp">
    <el-card shadow="hover">
      <el-form ref="editDialogFormRef" :model="ruleForm" :rules="rules" size="default" label-width="100px">
        <el-form-item label="公告标题" prop="noticeTitle" style="width: 500px">
          <el-input v-model="ruleForm.noticeTitle" placeholder="请输入公告标题" clearable></el-input>
        </el-form-item>
        <el-form-item label="公告类型" prop="noticeType" style="width: 500px">
          <dictSelect v-model="ruleForm.noticeType" dictType="noticeType" placeholder="请选择公告类型"></dictSelect>
        </el-form-item>
        <el-form-item label="是否发布" prop="status">
          <el-radio-group v-model="ruleForm.status">
            <el-radio :label=true>是</el-radio>
            <el-radio :label=false>否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="公告内容" prop="noticeContent">
          <edits :content="ruleForm.noticeContent" @content="getContent"></edits>
        </el-form-item>
        <div class="tc mt15" style="margin-left: 100px">
          <el-button type="primary" @click="saveOrUpdate">完 成</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import {addNotice, updateNotice} from "@/api/system/notice";
import {Message} from "element-ui";

export default {
  components: {
    dictSelect: () => import("@/component/dictSelect/index.vue"),
    edits: () => import('@/component/edit/index.vue')
  },
  name: 'addOrEditNotice',
  data() {
    return {
      flag: '',
      ruleForm: {
        noticeTitle: '',
        noticeType: '',
        status: true,
        noticeContent: ''
      },
      rules: {
        noticeTitle: [{required: true, message: '请输入公告标题', trigger: 'blur'}],
        noticeType: [{required: true, message: '请选择公告类型', trigger: 'change'}],
        noticeContent: [{required: true, message: '请输入公告内容', trigger: 'blur'}]
      }
    }
  },
  mounted() {
    this.flag = this.$route.params.flag
    const row = this.$route.params.row
    if (this.flag === 'edit') {
      this.ruleForm = {...row}
    }
  },
  methods: {
    getContent(val){
      this.ruleForm.noticeContent = val
    },
    saveOrUpdate() {
      this.$refs["editDialogFormRef"].validate(valid => {
        if (valid) {
          if (this.flag === 'add') {
            addNotice(this.ruleForm).then(res => {
              if (res.code === 200) {
                this.bus.$emit('closeCurrentTagsView', '/addOrEdit-notice')
                Message.success(res.message);
                this.$router.push('/notice')
              }
            })
          } else {
            updateNotice(this.ruleForm).then(res => {
              if (res.code === 200) {
                this.bus.$emit('closeCurrentTagsView', '/addOrEdit-notice')
                Message.success(res.message);
                this.$router.push('/notice')
              }
            })
          }
        }
      })
    }
  }
};
</script>
