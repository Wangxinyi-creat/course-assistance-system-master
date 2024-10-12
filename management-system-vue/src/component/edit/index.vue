<template>
  <div>
    <div style="border: 1px solid #ccc">
      <Toolbar
        style="border-bottom: 1px solid #ccc"
        :editor="editor"
        :defaultConfig="toolbarConfig"
        :mode="mode"
      />
      <Editor
        style="height: 500px; overflow-y: hidden"
        v-model="html"
        :defaultConfig="editorConfig"
        :mode="mode"
        @onCreated="onCreated"
        @onChange="onChange"
      />
    </div>
  </div>
</template>

<script>
import {Message} from "element-ui";
import {Editor, Toolbar} from '@wangeditor/editor-for-vue'

export default {
  components: {Editor, Toolbar},
  props: {
    content: {//回显接口的数据
      type: String,
      default: ''
    }
  },
  data() {
    return {
      editor: null,
      flag: false,
      html: '<p><br/></p>',
      toolbarConfig: {
        excludeKeys: ['fullScreen']
      },
      editorConfig: {
        placeholder: '请输入内容...',
        MENU_CONF: {
          uploadImage: {
            // server:放自己公司后端提供的图片上传接口
            server: process.env.VUE_APP_APIUPLOAD,
            // fieldName:自己公司和后端共同约定好的接收图片接口参数名
            fieldName: 'file',
            // 请求头
            // headers: { Authorization: 'Bearer ' + getToken() },
            // 插入图片
            customInsert: (res, insertFn) => {
              if (res.code === 200) {
                insertFn(res.data)
              } else {
                Message.error(res.message)
              }
            }
          },
          uploadVideo: {
            server: process.env.VUE_APP_APIUPLOAD,
            fieldName: 'file',
            customInsert: (res, insertFn) => {
              if (res.code === 200) {
                insertFn(res.data)
              } else {
                Message.error(res.message)
              }
            }
          }
        }
      },
      mode: 'default'
    }
  },
  methods: {
    onCreated(editor) {
      this.editor = Object.seal(editor) // 一定要用 Object.seal() ，否则会报错
    },
    onChange(editor) {
      if (this.flag) this.$emit('content', editor.getHtml())
    }
  },
  mounted() {
    // 模拟 ajax 请求，异步渲染编辑器
    setTimeout(() => {
      this.content ? this.html = this.content : ''
      this.flag = true
    }, 500)
  },
  beforeDestroy() {
    const editor = this.editor
    if (editor == null) return
    editor.destroy() // 组件销毁时，及时销毁编辑器
  }
}
</script>

<style src="@wangeditor/editor/dist/css/style.css"></style>
