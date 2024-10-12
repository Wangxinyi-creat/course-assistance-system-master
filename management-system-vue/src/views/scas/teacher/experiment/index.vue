<template>
  <div class="system-user-container layout-padding">
    <el-card shadow="hover" class="layout-padding-auto">
      <div class="system-user-search mb15">
        <el-input v-model="tableData.param.major" clearable placeholder="请输入专业" size="medium"
                  style="width: 200px;margin-left: 10px"></el-input>
        <el-input v-model="tableData.param.userName" clearable placeholder="请输入学号" size="medium"
                  style="width: 200px;margin-left: 10px"></el-input>
        <el-input v-model="tableData.param.jobNumber" clearable placeholder="请输入作业号" size="medium"
                  style="width: 200px;margin-left: 10px"></el-input>
        <el-button size="medium" type="primary" class="ml10" icon="el-icon-search" @click="getTableData()">
          查询
        </el-button>
      </div>

      <el-table :data="tableData.data" v-loading="tableData.loading" style="width: 100%;min-height:calc(100vh - 320px)">
<!--        <el-table-column type="index" label="序号" align="center" width="60"/>-->
<!--        <el-table-column prop="courseName" label="课程" align="center" show-overflow-tooltip></el-table-column>-->
        <el-table-column prop="userName" label="学号" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="jobNumber" label="作业号" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="experimentTitle" label="作业标题" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="experimentDescription" label="作业描述" align="center"
                         show-overflow-tooltip></el-table-column>
        <el-table-column prop="answer" label="总结" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="myCode" label="实验代码" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="result" label="代码运行结果" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="uploadUrl" label="作业附件" align="center" show-overflow-tooltip>
          <template #default="scope">
            <!--            <a :href="scope.row.uploadUrl" target="_blank" v-if="scope.row.uploadUrl">查看附件</a>-->
            <button @click="previewPdf(scope.row.uploadUrl)" v-if="scope.row.uploadUrl">查看附件</button>
          </template>
        </el-table-column>
        <el-table-column prop="deadline" label="截止日期" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="uploadDate" label="上传日期" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="grade" label="成绩" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="review" label="评阅意见" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column label="操作" align="center" width="180px">
          <template #default="scope">
            <el-button size="small"
                       type="text"
                       icon="el-icon-edit"
                       @click="updateHandle(scope.row)"
            >修改
            </el-button>
            <el-button size="small"
                       type="text"
                       icon="el-icon-circle-plus"
                       v-if="!scope.row.grade && scope.row.uploadUrl"
                       @click="openAddOrUpdate('sy', scope.row)"
            >审阅
            </el-button>
            <el-button size="small"
                       type="text"
                       icon="el-icon-delete"
                       @click="onRowDel(scope.row)">删除
            </el-button>
<!--            <el-button size="small"-->
<!--                       type="text"-->
<!--                       icon="el-icon-picture-outline-round"-->
<!--                       v-if="!scope.row.grade && scope.row.uploadUrl"-->
<!--            >查重-->
<!--            </el-button>-->
            <el-button size="small"
                       type="text"
                       icon="el-icon-download"
                       @click="openUrl(scope.row.id)"
                       v-if="!scope.row.grade && scope.row.uploadUrl"
            >一键合成文档
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
          @size-change="onHandleSizeChange"
          @current-change="onHandleCurrentChange"
          class="mt15"
          :pager-count="5"
          :page-sizes="[10, 20, 30]"
          v-model:current-page="tableData.param.pageNum"
          background
          v-model:page-size="tableData.param.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="tableData.total"
      >
      </el-pagination>
    </el-card>

    <experimentDialog ref="experimentDialogRef" @refresh="getTableData()"/>
    <update-dialog ref="updateDialogRef" @refresh="getTableData()"/>
  </div>
</template>

<script>
import {Message, MessageBox} from "element-ui";

import {delExperiment, getExperiment, getDetail} from "@/api/scas/experiment";

export default {
  name: 'experiment',
  props:{
    courseId:{
      type: Number,
      default: ''
    }
  },
  data() {
    return {
      tableData: {
        data: [],
        total: 0,
        loading: false,
        param: {
          major:'',
          courseId: '',
          userName: '',
          jobNumber:'',
          pageNum: 1,
          pageSize: 10,
        },
      },
    };
  },
  components: {
    experimentDialog: () => import('./dialog.vue'),
    updateDialog: () => import('./updateDialog.vue')
  },
  mounted() {
    this.tableData.param.courseId = this.courseId
    this.getTableData();
  },
  methods: {
    previewPdf(url) {
      console.log(url)
      window.open(`static/web/viewer.html?file=${encodeURIComponent(url)}`)
      // window.open(`static/pdf/web/viewer.html?file=${url}`)
    },
    /**
     * 获取菜单
     */
    getTableData() {
      this.tableData.loading = true;
      getExperiment(this.tableData.param).then(res => {
        this.tableData.data = res.data.list;
        this.tableData.total = res.data.total;
        setTimeout(() => {
          this.tableData.loading = false;
        }, 500);
      })
    },
    openUrl(id){
      getDetail(id).then(res => {
        console.log("res", res);
        // 文件以pdf形式进行预览
        let blob = new Blob([res.data], {
          type: "application/pdf;chartset=UTF-8",
        });
        let fileURL = URL.createObjectURL(blob);
        window.open(fileURL);
      })
    },
    /**
     * 添加或修改弹框
     * @param flag
     * @param row
     */
    openAddOrUpdate(flag, row) {
      this.$refs.experimentDialogRef.openDialog(flag, row)
    },
    updateHandle(row) {
      this.$refs.updateDialogRef.openDialog(row)
    },
    /**
     * 删除字典
     * @param row
     */
    onRowDel(row) {
      const id = row.id
      //提示
      MessageBox.confirm(`此操作将永久删除此项作业，是否继续?`, '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        //调用删除方法
        delExperiment(id).then(res => {
          this.getTableData();
          Message.success("删除成功");
        })
      }).catch(() => {
      });
    },
    /**
     * 修改每页所存数据量的值所触发的函数
     * @param val
     */
    onHandleSizeChange(val) {
      // 修改页的大小
      this.tableData.param.pageSize = val;
      // 按新的pageNo和pageSize进行查询
      this.getTableData();
    },
    /**
     * 修改当前页所触发的函数
     * @param val
     */
    onHandleCurrentChange(val) {
      // 更新当前的页
      this.tableData.param.pageNum = val;
      // 按新的pageNo和pageSize进行查询
      this.getTableData();
    },
  }
};
</script>

<style>
.el-tooltip__popper {
  max-width: 50%
}
</style>
