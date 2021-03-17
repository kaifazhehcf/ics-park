<template>
  <div class="account-settings-info-view">
    <a-row :gutter="16">
      <a-col :md="24" :lg="16">

        <a-form @submit="handleSubmit" :form="form" layout="vertical">
          <a-form-item style="display:none">
            <a-input v-decorator="['userId']"/>
          </a-form-item>
          <a-form-item label="用户名">
            <a-input placeholder="用户名" v-decorator="['userName', {rules: [{ required: true, message: '请选录入用户名' }]}]"/>
          </a-form-item>
          <a-form-item label="登录名" >
            <a-input placeholder="登录名" v-decorator="['loginName', {rules: [{ required: true, message: '请录入登录名' }]}]" disabled/>
          </a-form-item>
          <a-form-item label="手机" >
            <a-input placeholder="手机" v-decorator="['mobile', {rules: [{ required: true, message: '请选录入手机' }]}]" disabled/>
          </a-form-item>
          <a-form-item label="用户类型">
            <a-select placeholder="类型" v-decorator="['userType', {rules: [{ required: true, message: '请选择用户类型' }]}]" disabled>
              <a-select-option v-for="item in userTypeData" :key="item.value">{{ item.text }}</a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item label="性别">
            <a-radio-group @change="onChange" v-decorator="['sex']" >
              <a-radio :value="'0'">男</a-radio>
              <a-radio :value="'1'">女</a-radio>
              <a-radio :value="'2'">未知</a-radio>
            </a-radio-group>
          </a-form-item>
          <a-form-item label="职位" >
            <a-input placeholder="职位" v-decorator="['post']"/>
          </a-form-item>
          <a-form-item label="电子邮件" :required="false">
            <a-input placeholder="电子邮件" v-decorator="['email', {rules: [{ required: true, message: '请录入电子邮件' }]}]"/>
          </a-form-item>
          <a-form-item>
            <a-button htmlType="submit" type="primary">提交</a-button>
          </a-form-item>
        </a-form>

      </a-col>
      <a-col :md="24" :lg="8" :style="{ minHeight: '180px' }">
        <div class="ant-upload-preview" @click="$refs.modal.edit(1)" >
          <a-icon type="cloud-upload-o" class="upload-icon"/>
          <div class="mask">
            <a-icon type="plus" />
          </div>
          <img :src="option.img"/>
        </div>
      </a-col>

    </a-row>

    <avatar-modal ref="modal" @ok="setAvatar"/>

  </div>
</template>

<script>
import AvatarModal from './AvatarModal'

import { getInfo } from '@/api/login'
import pick from 'lodash.pick'
import { getUserType, updataProfile } from '@/api/system'

export default {
  components: {
    AvatarModal
  },
  data () {
    return {
      // cropper
      preview: {},
      option: {
        img: '',
        info: true,
        size: 1,
        outputType: 'jpeg',
        canScale: false,
        autoCrop: true,
        // 只有自动截图开启 宽度高度才生效
        autoCropWidth: 180,
        autoCropHeight: 180,
        fixedBox: true,
        // 开启宽度和高度比例
        fixed: true,
        fixedNumber: [1, 1]
      },
      mdl: {},
      userTypeData: [],
      parkData: [],
      form: this.$form.createForm(this)
    }
  },
  created () {
    // 用户信息
    getInfo().then(record => {
      this.mdl = Object.assign(record)
      this.option.img = this.mdl.avatar
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.mdl, 'userId', 'userName', 'loginName', 'userType', 'sex', 'post', 'mobile', 'email'))
      })
    })
    // 获取用户类型
    getUserType().then(res => {
      if (res.code === 0) {
        const result = res.data
        result.forEach(r => {
          this.userTypeData.push({ value: r.value, text: r.text })
        })
      }
    })
  },
  methods: {
    setAvatar (url) {
      console.log('url', url)
      this.option.img = url
    },
    onChange (e) {
      this.value = e.target.value
    },
    // handler
    handleSubmit (e) {
      e.preventDefault()
      this.form.validateFields((err, values) => {
        if (!err) {
          // eslint-disable-next-line no-console
          console.log('Received values of form: ', values)
          updataProfile(values).then(res => {
            if (res.code === 0) {
              this.$message.success('保存成功')
              this.$emit('ok')
              this.visible = false
            } else {
              this.$message.success(res.msg)
            }
          }).catch(() => {
            this.$message.error('系统错误，请稍后再试')
          }).finally(() => {
            this.confirmLoading = false
          })
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>

  .avatar-upload-wrapper {
    height: 200px;
    width: 100%;
  }

  .ant-upload-preview {
    position: relative;
    margin: 0 auto;
    width: 100%;
    max-width: 180px;
    border-radius: 50%;
    box-shadow: 0 0 4px #ccc;

    .upload-icon {
      position: absolute;
      top: 0;
      right: 10px;
      font-size: 1.4rem;
      padding: 0.5rem;
      background: rgba(222, 221, 221, 0.7);
      border-radius: 50%;
      border: 1px solid rgba(0, 0, 0, 0.2);
    }
    .mask {
      opacity: 0;
      position: absolute;
      background: rgba(0,0,0,0.4);
      cursor: pointer;
      transition: opacity 0.4s;

      &:hover {
        opacity: 1;
      }

      i {
        font-size: 2rem;
        position: absolute;
        top: 50%;
        left: 50%;
        margin-left: -1rem;
        margin-top: -1rem;
        color: #d6d6d6;
      }
    }

    img, .mask {
      width: 100%;
      max-width: 180px;
      height: 100%;
      border-radius: 50%;
      overflow: hidden;
    }
  }
</style>
