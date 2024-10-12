<template>
  <div class="system-menu-dialog-container">
    <el-dialog :title="dialog.title"
               :visible.sync="dialog.isShowDialog"
               :close-on-click-modal="false"
               width="769px">
      <el-form ref="menuDialogFormRef" :model="ruleForm" size="default" label-width="100px">
        <el-row :gutter="35">
          <!--公共字段-->
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="上级菜单">
              <el-cascader
                :options="menuData"
                :props="{ checkStrictly: true, value: 'id', label: 'menuName' ,emitPath: false}"
                placeholder="请选择上级菜单"
                clearable
                class="w100"
                v-model="ruleForm.parentId"
              >
                <template #default="{ node, data }">
                  <span>{{ data.menuName }}</span>
                  <span v-if="!node.isLeaf"> ({{ data.children.length }}) </span>
                </template>
              </el-cascader>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" class="mb20">
            <el-form-item label="菜单类型">
              <el-radio-group v-model="ruleForm.menuType">
                <el-radio :label=0>目录</el-radio>
                <el-radio :label=1>菜单</el-radio>
                <el-radio :label=2>按钮</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
            <el-form-item label="菜单名称">
              <el-input v-model="ruleForm.menuName" placeholder="请输入菜单名称" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="24" :xl="24" class="mb20" v-if="ruleForm.menuType!=2">
            <el-form-item label="菜单图标">
              <e-icon-picker v-model="ruleForm.elIcon" size="small"/>
            </el-form-item>
          </el-col>

          <!--菜单特有字段-->
          <template v-if="ruleForm.menuType === 1">
            <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
              <el-form-item label="路由名称">
                <el-input v-model="ruleForm.name" placeholder="路由中的 name 值 例:powerUser" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
              <el-form-item label="路由路径">
                <el-input v-model="ruleForm.path" placeholder="路由中的 path 值 例:/powerUser" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="24" class="mb20">
              <el-form-item label="组件路径">
                <el-input v-model="ruleForm.component" placeholder="文件路径 例: system/user/index"
                          clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
              <el-form-item label="是否外链">
                <el-radio-group v-model="isLinkFlag">
                  <el-radio :label="true">是</el-radio>
                  <el-radio :label="false">否</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <template v-if="isLinkFlag">
              <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
                <el-form-item label="是否内嵌">
                  <el-radio-group v-model="ruleForm.isIframe">
                    <el-radio :label="true">是</el-radio>
                    <el-radio :label="false">否</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="24" class="mb20">
                <el-form-item label="链接地址">
                  <el-input
                    v-model="ruleForm.isLink"
                    placeholder="外链/内嵌时链接地址（http:xxx.com）"
                    clearable>
                  </el-input>
                </el-form-item>
              </el-col>
            </template>
            <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
              <el-form-item label="是否固定">
                <el-radio-group v-model="ruleForm.isAffix">
                  <el-radio :label="true">固定</el-radio>
                  <el-radio :label="false">不固定</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </template>

          <!--目录、菜单特有字段-->
          <template v-if="ruleForm.menuType !== 2">
            <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
              <el-form-item label="是否侧边隐藏">
                <el-radio-group v-model="ruleForm.isHide">
                  <el-radio :label="true">隐藏</el-radio>
                  <el-radio :label="false">不隐藏</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </template>

          <!--按钮特有字段-->
          <template v-if="ruleForm.menuType === 2">
            <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
              <el-form-item label="权限标识">
                <el-input v-model="ruleForm.permission" placeholder="请输入按钮权限标识" clearable></el-input>
              </el-form-item>
            </el-col>
          </template>

          <!--用户是否可见   公共字段-->
          <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
            <el-form-item label="用户是否可见">
              <el-radio-group v-model="ruleForm.showFlag">
                <el-radio :label="true">是</el-radio>
                <el-radio :label="false">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>

          <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
            <el-form-item label="排序">
              <el-input-number v-model="ruleForm.sortId" controls-position="right" placeholder="请输入排序"
                               class="w100"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
				<span class="dialog-footer">
					<el-button @click="onCancel" size="default">取 消</el-button>
					<el-button type="primary" @click="onSubmit(dialog.type)" size="default">{{ dialog.submitTxt }}</el-button>
				</span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import {Message} from "element-ui";
import {addMenu, updateMenu} from "@/api/system/menu";

export default {
  name: 'menuDialog',
  data() {
    return {
      dialog: {
        isShowDialog: false,
        type: '',
        title: '',
        submitTxt: '',
      },
      isLinkFlag: false,
      ruleForm: {},
      menuData: []
    };
  },
  methods: {
    /**
     * 打开弹框
     * @param type
     * @param row
     */
    openDialog(type, row) {
      if (type === 'edit') {
        this.ruleForm = {...row}
        if (this.ruleForm.isLink != null) this.isLinkFlag = true;
        this.dialog.title = '修改菜单';
        this.dialog.submitTxt = '修 改';
      } else {
        this.ruleForm = {
          menuType: 0
        }
        if (row !== 0) {
          this.ruleForm.parentId = row.id;
        } else {
          this.ruleForm.parentId = 0;
        }
        this.ruleForm.isHide = false
        this.ruleForm.isAffix = false
        this.ruleForm.showFlag = true
        this.dialog.title = '新增菜单';
        this.dialog.submitTxt = '新 增';
      }
      this.dialog.type = type
      this.dialog.isShowDialog = true;
    },
    /**
     * 关闭弹框
     */
    onCancel() {
      this.dialog.isShowDialog = false;
    },
    /**
     * 新增/修改  保存
     * @param flag
     */
    onSubmit(flag) {
      this.$refs["menuDialogFormRef"].validate(valid => {
        if (valid) {
          if (flag === 'edit') {
            updateMenu(this.ruleForm).then(res => {
              if (res.code === 200) {
                Message.success(res.message);
                this.$emit('refresh');
              }
            })
          } else {
            addMenu(this.ruleForm).then(res => {
              if (res.code === 200) {
                Message.success(res.message);
                this.$emit('refresh');
              }
            })
          }
          this.onCancel();
        }
      })
    }
  }
};
</script>
