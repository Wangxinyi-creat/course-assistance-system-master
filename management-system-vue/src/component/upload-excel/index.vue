<template>
  <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
    <el-upload
      class="upload-demo"
      drag
      :auto-upload="false"
      action="#"
      :limit="1"
      :file-list="fileList"
      :on-change="handleChanges"
      :on-exceed="handleExceed"
      :on-remove="handleRemove"
      accept=".xls,.xlsx"
      multiple>
      <i class="el-icon-upload"></i>
      <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
      <div class="el-upload__tip" slot="tip">只能上传1个excel文件,重新上传请先删除</div>
      <el-button type="text" slot="tip" @click="exportTemplate">点击下载导入模板</el-button>
    </el-upload>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="handleImportExcel" :disabled="uploading">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "UploadExcel",
  data() {
    return {
      open: false,
      title: '导入',
      fileList: [],  //文件列表
      file: {},
      uploading: false,
      baseURL: process.env.VUE_APP_APIURL
    }
  },
  props: {
    url: String,//上传地址
    fileName: String//模板文件名
  },
  methods: {
    //下载模板  将模板文件放置在public/excel文件夹下
    exportTemplate() {
      const url = this.baseURL + "excel/" + this.fileName
      window.open(url)
    },
    /**
     * 文件状态改变触发
     * @param file  当前文件
     * @param fileList 当前文件列表
     */
    handleChanges(file, fileList) {
      this.uploading = false;
      this.file = file;
    },
    /**
     * 文件超出个数限制
     */
    handleExceed() {
      this.$message.warning("仅允许上传一个文件！");
    },
    /**
     * 移除文件
     */
    handleRemove() {
      this.uploading = true;
    },
    /**
     * 上传
     */
    handleImportExcel() {
      if (!this.file.size) {
        this.$message.warning("请选择上传的文件");
        return;
      }
      this.uploading = true
      let formData = new FormData();
      formData.append("file", this.file.raw);
      request.post(this.url, formData).then(res => {
        this.uploading = false
        this.$message.success("导入成功")
        this.open = false
        this.$emit('refresh')
      }).catch(() => {
        this.uploading = false
      })
    },
    cancel() {
      this.open = false
    }
  }

}
</script>

<style scoped>
.el-input {
  width: 90%;
}

.el-form .el-select {
  width: 90%;
}

.upload-demo {
  text-align: center;
  justify-content: center;
}
</style>
