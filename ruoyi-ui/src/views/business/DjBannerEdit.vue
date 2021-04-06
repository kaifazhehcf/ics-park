<template>
  <a-card :body-style="{padding: '24px 32px'}" :bordered="false">
    <a-form @submit="handleSubmit" :form="form" :label-col="{ span: 4 }" :wrapper-col="{ span: 6 }">
      <a-form-item style="display:none">
        <a-input v-decorator="['id']"/>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="banner名称" extra="不要超过6个字符">
        <a-input style="width: 60%" placeholder="请输入banner名称" v-decorator="['bannerName', {rules: [{ required: true, message: '请输入banner名称' }]}]"/>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="banner简介（可选）" extra="不要超过50个字符">
        <a-textarea placeholder="请输入banner简介" :rows="5" v-decorator="['bannerDesc']"/>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="上传banner图标" extra="尺寸建议：宽750px，高300px; 格式：jpg，png；大小：2MB">
        <a-upload
          v-decorator="['bannerImg', {rules: [{ required: true, message: '请上传banner图标' }]}]"
          list-type="picture-card"
          class="avatar-uploader"
          :show-upload-list="false"
          :action="uploadUrl"
          :headers="headers"
          :before-upload="beforeUpload"
          @change="handleChange"
        >
          <img v-if="imageUrl" :src="imageUrl" style="width: 102px; height: 102px"/>
          <div v-else>
            <a-icon :type="loading ? 'loading' : 'plus'" />
            <div class="ant-upload-text"> 上传 </div>
          </div>
        </a-upload>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="落地页URI">
        <a-input style="width: 60%" placeholder="落地页URI" v-decorator="['url', {rules: [{ required: true, message: '请输入落地页URI' }]}]"/>
      </a-form-item>
      <a-form-item :wrapper-col="{ span: 12, offset: 12 }">
        <a-button htmlType="submit" type="primary">提交</a-button>
        <a-button icon="rollback" style="margin-left: 8px" @click="rollback">关闭</a-button>
      </a-form-item>
    </a-form>
  </a-card>
</template>

<script>

import storage from 'store'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import WangEditor from '@/components/Editor/WangEditor.vue'
import { getDistList } from '@/api/system'
import { getDjBanner, saveDjBanner } from '@/api/business/djBanner'
import pick from 'lodash.pick'

export default {
  name: 'BaseForm',
  components: {
    WangEditor
  },
  data () {
    return {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 6 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      },
      uploadUrl: process.env.VUE_APP_API_BASE_URL + '/dfs/upload',
      headers: {
        token: storage.get(ACCESS_TOKEN)
      },
      loading: false,
      imageUrl: '',
      previewVisible: false,
      previewImage: '',
      fileList: [],
      options: [],
      city: [],
      // form
      form: this.$form.createForm(this)

    }
  },
  created () {
    this.handleInit()
    getDistList(Object.assign({ deep: 0 })).then(res => {
      this.options = res.rows.map(d => {
        return { value: d.id, label: d.name, isLeaf: false }
      })
    })
  },
  methods: {
    handleInit () {
      const { id } = this.$route.query
      if (id) {
        getDjBanner(id).then(record => {
          this.mdl = Object.assign(record)
          this.visible = true
          if (this.mdl.bannerImg) {
            this.imageUrl = process.env.VUE_APP_API_BASE_URL + this.mdl.bannerImg
          }
          this.$nextTick(() => {
            this.form.setFieldsValue(pick(this.mdl, 'id', 'bannerName', 'url', 'bannerDesc', 'bannerImg', 'hits', 'isMarketable', 'isTop'))
          })
        })
      }
    },
    beforeUpload (file) {
      const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png'
      if (!isJpgOrPng) {
        this.$message.error('You can only upload JPG file!')
      }
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isLt2M) {
        this.$message.error('Image must smaller than 2MB!')
      }
      return isJpgOrPng && isLt2M
    },
    handleChange (info) {
      if (info.file.status === 'uploading') {
        this.loading = true
        return
      }
      if (info.file.status === 'done') {
        const { form: { setFieldsValue } } = this
        const result = info.file.response
        console.log('result', result)
        // 设置值
        this.$nextTick(() => {
          setFieldsValue({
            bannerImg: result.fileName
          })
        })
        // Get this url from response in real world.
        getBase64(info.file.originFileObj, imageUrl => {
          this.imageUrl = imageUrl
          this.loading = false
        })
      }
    },
    async handlePreview (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64FileList(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    handleCancel () {
      this.previewVisible = false
    },
    // 上传banner
    handleChangeFileList ({ fileList }) {
      this.fileList = fileList
    },
    // 地区选择
    loadData (selectedOptions) {
      const targetOption = selectedOptions[selectedOptions.length - 1]
      targetOption.loading = true

      getDistList(Object.assign({ pid: targetOption.value })).then(res => {
        targetOption.loading = false
        targetOption.children = res.rows.map(d => {
          return { value: d.id, label: d.name, isLeaf: d.deep === 2 }
        })
        this.options = [...this.options]
      })
    },
    // handler
    handleSubmit (e) {
      e.preventDefault()
      this.form.validateFields((err, values) => {
        if (!err) {
          // eslint-disable-next-line no-console
          console.log('Received values of form: ', values)
          // console.log('city:', this.city)
          // console.log('banner', this.fileList)
          this.confirmLoading = true
          saveDjBanner(values).then(res => {
            if (res.code === 0) {
              this.$message.success('保存成功')
              this.$emit('ok')
              this.visible = false
              this.rollback()
            } else {
              this.$message.error(res.msg)
            }
          }).catch(() => {
            this.$message.error('系统错误，请稍后再试')
          }).finally(() => {
            this.confirmLoading = false
          })
        }
      })
    },
    rollback () {
      this.$router.push('/business/dj/djBanner')
    }
  }
}

// 上传园区小图
function getBase64 (img, callback) {
  const reader = new FileReader()
  reader.addEventListener('load', () => callback(reader.result))
  reader.readAsDataURL(img)
}

// 上传园区banner图
function getBase64FileList (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
</script>

<style>
.avatar-uploader > .ant-upload {
  width: 102px;
  height: 102px;
}
.avatar-banner-uploader > .ant-upload {
  width: 260px;
  height: 104px;
}
.ant-upload-select-picture-card i {
  font-size: 32px;
  color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: #666;
}
</style>
