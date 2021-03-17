<template>
  <div :class="prefixCls">
    <div ref="editor" class="editor-wrapper"></div>
  </div>
</template>

<script>
import WEditor from 'wangeditor'
import storage from 'store'
import { ACCESS_TOKEN } from '@/store/mutation-types'

export default {
  name: 'WangEditor',
  props: {
    prefixCls: {
      type: String,
      default: 'ant-editor-wang'
    },
    // eslint-disable-next-line
    value: {
      type: String
    }
  },
  data () {
    return {
      editor: null,
      editorContent: null,
      uploadUrl: process.env.VUE_APP_API_BASE_URL + '/dfs/upload_file_list',
      headers: {
        token: storage.get(ACCESS_TOKEN)
      }
    }
  },
  watch: {
    value (val) {
      this.editorContent = val
      this.editor.txt.html(val)
    }
  },
  mounted () {
    this.initEditor()
  },
  methods: {
    initEditor () {
      this.editor = new WEditor(this.$refs.editor)
      this.editor.customConfig.showLinkImg = false
      this.editor.customConfig.uploadImgServer = this.uploadUrl
      this.editor.customConfig.uploadFileName = 'files'
      this.editor.customConfig.debug = true
      this.editor.customConfig.uploadImgHeaders = {
        token: storage.get(ACCESS_TOKEN) // 设置请求头
      }
      this.editor.customConfig.uploadImgHooks = {
        // 图片上传并返回结果，但图片插入错误时触发
        fail: function (xhr, editor, result) {
          console.log(result)
        },
        success: function (xhr, editor, result) {
          // 图片上传并返回结果，图片插入成功之后触发
          console.log(result, 'success')
        }
      }
      this.editor.customConfig.onchange = (html) => {
        this.editorContent = html
        this.$emit('change', this.editorContent)
      }
      this.editor.create()
    }
  }
}
</script>
editor
<style lang="less" scoped>
.ant-editor-wang {
  .editor-wrapper {
    text-align: left;
  }
}
</style>
