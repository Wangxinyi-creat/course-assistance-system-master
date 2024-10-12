<template>
  <div class="system-dic-dialog-container">
    <el-dialog :title="dialog.title"
               :visible.sync="dialog.isShowDialog"
               :close-on-click-modal="false"
               width="769px">
      <el-form ref="dicDialogFormRef" :model="ruleForm" size="default" label-width="90px">
        <el-row :gutter="35">
          <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
            <el-form-item label="字典名称">
              <el-input v-model="ruleForm.dictType.dictName" placeholder="请输入字典名称" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
            <el-form-item label="字典类型">
              <el-input v-model="ruleForm.dictType.dictType" placeholder="请输入字典类型"
                        clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="字典状态">
              <el-switch v-model="ruleForm.dictType.status" inline-prompt active-text="启"
                         inactive-text="禁"></el-switch>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-row :gutter="35" v-for="(v, k) in ruleForm.dictDataList" :key="k">
              <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
                <el-form-item>
                  <template #label>
                    <el-button type="primary" circle size="small" @click="onAddRow" v-if="k === 0">
                      <el-icon class="el-icon-plus"></el-icon>
                    </el-button>
                    <el-button type="danger" circle size="small" @click="onDelRow(k)" v-else>
                      <el-icon class="el-icon-delete"></el-icon>
                    </el-button>
                    <span class="ml10">标签</span>
                  </template>
                  <el-input v-model="v.dictLabel" style="width: 100%" placeholder="请输入字典标签"></el-input>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
                <el-form-item label="属性值">
                  <el-input v-model="v.dictValue" style="width: 100%" placeholder="请输入属性值"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="字典描述">
              <el-input v-model="ruleForm.dictType.remark" type="textarea" placeholder="请输入字典描述"
                        maxlength="150"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
				<span class="dialog-footer">
					<el-button @click="onCancel" size="default">取 消</el-button>
					<el-button type="primary" @click="onSubmit" size="default">{{ dialog.submitTxt }}</el-button>
				</span>
      </template>
    </el-dialog>
  </div>
</template>


<script>
import {addDict, updateDict} from "@/api/system/dict";
import {Message} from "element-ui";

export default {
  name: "DictDialog",
  data() {
    return {
      dialog: {
        isShowDialog: false,
        type: '',
        title: '',
        submitTxt: '',
      },
      ruleForm: {
        dictType: {
          dictName: '', // 字典名称
          dictType: '',//字典类型
          status: true, // 字典状态
          remark: '', // 字典描述
        },
        dictDataList: [
          {
            id: 'number',
            dictLabel: '',
            dictValue: ''
          }
        ]
      },
    }

  },
  methods: {
    /**
     * 打开弹框
     * @param type
     * @param row
     */
    openDialog(type, row) {
      if (type === 'edit') {
        this.ruleForm.dictType = {...row}
        this.ruleForm.dictType.oldDictType = row.dictType
        this.ruleForm.dictDataList = row.dictDataList
        this.dialog.title = '修改字典';
        this.dialog.submitTxt = '修 改';
      } else {
        this.ruleForm = {
          dictType: {
            dictName: '',
            dictType: '',
            status: true,
            remark: '',
          },
          dictDataList: [
            {
              id: 'number',
              dictLabel: '',
              dictValue: ''
            }
          ]
        }
        this.dialog.title = '新增字典';
        this.dialog.submitTxt = '新 增';
      }
      this.dialog.type = type
      this.dialog.isShowDialog = true;
    },
    // 取消
    onCancel() {
      this.dialog.isShowDialog = false;
    },
    // 提交
    onSubmit() {
      if (this.ruleForm.dictType.dictName === '') {
        Message.error("请输入字典名称")
        return
      }
      if (this.ruleForm.dictType.dictType === '') {
        Message.error("请输入字典类型")
        return
      }
      if (this.dialog.type === 'edit') {
        updateDict(this.ruleForm).then(res => {
          if (res.code === 200) {
            Message.success(res.message);
            this.$emit('refresh');
          }
        })
      } else {
        addDict(this.ruleForm).then(res => {
          if (res.code === 200) {
            Message.success(res.message);
            this.$emit('refresh');
          }
        })
      }
      this.onCancel()
    },
    // 新增行
    onAddRow() {
      this.ruleForm.dictDataList.push({
        id: Math.random(),
        dictLabel: '',
        dictValue: '',
      });
    },
    // 删除行
    onDelRow(k) {
      this.ruleForm.dictDataList.splice(k, 1);
    },
  }
}
</script>

<style>
/* 弹出框滚动条 */
/* 设置滚动条的样式 */
/**解决了滚动条之间发生错位的现象 */
.el-dialog ::-webkit-scrollbar {
  width: 5px !important;
  height: 10px !important;
  border-radius: 5px;
}

.el-dialog ::-webkit-scrollbar-thumb {
  border-radius: 5px;
  -webkit-box-shadow: inset 0 0 3px rgba(0, 0, 0, 0.2);
  /* 滚动条的颜色 */
  background-color: #e4e4e4;
}
</style>
