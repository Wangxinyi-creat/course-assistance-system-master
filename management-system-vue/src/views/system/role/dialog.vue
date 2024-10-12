<template>
  <!--角色分配菜单  :default-checked-keys="checked"-->
  <el-drawer title="分配菜单" :visible.sync="showTree" size="25%">
    <el-tree
        :data="tree"
        :props="defaultProps"
        show-checkbox
        node-key="id"
        ref="tree"
        :default-expanded-keys="[5]"
        class="el-trees">
        <span class="custom-tree-node" slot-scope="{ node, data }">
            <span><i :class="data.elIcon"></i>{{ data.menuName }}</span>
        </span>
    </el-tree>
    <div class="drawer-button">
      <el-button type="primary" @click="submitTree">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </el-drawer>
</template>


<script>
import {Message} from "element-ui";
import {getMenuToRole, getRoleMenu} from "@/api/system/menu";
import {addRoleMenu} from "@/api/system/role";

export default {
  name: "RoleMenuDialog",
  data() {
    return {
      //菜单树
      showTree: false,
      tree: [],
      defaultProps: {
        children: 'children',
        label: 'menuName'
      },
      roleId: null,
      drawer: false,
    }
  },
  created() {
    //获取菜单资源数据
    getMenuToRole().then(response => {
      this.tree = response.data;
      //设置首页为不可选状态
      this.tree.map(item => {
        if (item.menuName === '首页') item['disabled'] = true
      })
    });
  },
  methods: {
    /**角色分配菜单*/
    getRoleMenu(roleId) {
      this.roleId = roleId
      this.showTree = true
      //通过角色获取菜单id 默认选中
      const nodes = []
      getRoleMenu(roleId).then(res => {
        this.$nextTick(() => {
          res.data.forEach(item => {
            //默认添加首页
            if (item.menuName === '首页') {
              nodes.push(item)
            } else {
              const node = this.$refs.tree.getNode(item);
              if (node.isLeaf) { //关键，过滤掉不是叶子节点的
                nodes.push(item)
              }
            }
          })
          this.$refs.tree.setCheckedNodes(nodes, true)
        })
      })
    },
    /**
     * 取消
     */
    cancel() {
      this.showTree = false;
    },
    /**
     * 分配菜单提交
     */
    submitTree() {
      //获取子节点未全选时的父节点id
      const parentArr = this.$refs.tree.getHalfCheckedKeys();
      //获取所有选中节点的id
      const childArr = this.$refs.tree.getCheckedKeys();
      //拼接数组
      const permissionParams = parentArr.concat(childArr);
      //调用接口
      addRoleMenu(this.roleId, permissionParams).then(res => {
        if (res.code === 200) {
          // sessionStorage.removeItem(this.roleId + '角色菜单')
          Message.success(res.message);
        }
      })
      this.showTree = false
    }
  }
}

</script>

<style scoped>
.el-trees {
  height: 520px;
  margin-left: 5px;
  overflow: auto
}

.drawer-button {
  margin-top: 30px;
  text-align: center
}
</style>
