<template>
  <div class="account-settings-info-view">
    <a-row :gutter="16">
      <a-col :md="24" :lg="16">
        <a-form @submit="handleSubmit" :form="form" layout="vertical">
          <a-form-item>
            <div>当前手机号: {{ mobile }}</div>
          </a-form-item>
          <a-form-item>
            <a-input placeholder="11 位手机号" v-decorator="['mobile', {rules: [{ required: true, message: '请输入正确的手机号', pattern: /^1[3456789]\d{9}$/ }, { validator: this.handlePhoneCheck } ], validateTrigger: ['change', 'blur'] }]" style="width: 33%">
              <a-select slot="addonBefore" defaultValue="+86">
                <a-select-option value="+86">+86</a-select-option>
                <a-select-option value="+87">+87</a-select-option>
              </a-select>
            </a-input>
          </a-form-item>
          <a-row :gutter="10">
            <a-col class="gutter-row" :span="5">
              <a-form-item>
                <a-input type="text" placeholder="验证码" v-decorator="['captcha', {rules: [{ required: true, message: '请输入验证码' }], validateTrigger: 'blur'}]">
                  <a-icon slot="prefix" type="mail" :style="{ color: 'rgba(0,0,0,.25)' }"/>
                </a-input>
              </a-form-item>
            </a-col>
            <a-col class="gutter-row" :span="8">
              <a-button
                class="getCaptcha"
                :disabled="state.smsSendBtn"
                @click.stop.prevent="getCaptcha"
                v-text="!state.smsSendBtn && '获取验证码'||(state.time+' s')"></a-button>
            </a-col>
          </a-row>
          <a-form-item>
            <a-button htmlType="submit" type="primary">绑定手机</a-button>
          </a-form-item>
        </a-form>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import { getSmsCaptcha, binding } from '@/api/system'
import { mapGetters } from 'vuex'

export default {
  data () {
    return {
      state: {
        time: 60,
        smsSendBtn: false,
        percent: 10,
        progressColor: '#FF0000'
      },
      form: this.$form.createForm(this)
    }
  },
  computed: {
    ...mapGetters(['nickname', 'mobile'])
  },
  methods: {
    // handler
    handleSubmit (e) {
      e.preventDefault()
      this.form.validateFields((err, values) => {
        if (!err) {
          // eslint-disable-next-line no-console
          console.log('Received values of form: ', values)
          binding(values).then(res => {
            if (res.code === 0) {
              this.$message.success('绑定成功')
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
    getCaptcha (e) {
      e.preventDefault()
      const { form: { validateFields }, state, $message, $notification } = this

      validateFields(['mobile'], { force: true },
        (err, values) => {
          if (!err) {
            state.smsSendBtn = true

            const interval = window.setInterval(() => {
              if (state.time-- <= 0) {
                state.time = 60
                state.smsSendBtn = false
                window.clearInterval(interval)
              }
            }, 1000)

            const hide = $message.loading('验证码发送中..', 0)

            getSmsCaptcha({ mobile: values.mobile }).then(res => {
              setTimeout(hide, 2500)
              $notification['success']({
                message: '提示',
                description: '验证码获取成功，您的验证码为：' + res.msg,
                duration: 8
              })
            }).catch(err => {
              setTimeout(hide, 1)
              clearInterval(interval)
              state.time = 60
              state.smsSendBtn = false
              this.requestFailed(err)
            })
          }
        }
      )
    },
    rollback () {
      this.$router.push('/account/settings/base')
    }
  }
}
</script>

<style scoped>

</style>
