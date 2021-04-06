<template>
  <a-modal
    title="操作"
    style="top: 20px;"
    :width="800"
    v-model="visible"
    :confirmLoading="confirmLoading"
    @ok="handleSubmit"
  >
    <a-form :form="form">
      <a-form-item style="display:none">
        <a-input v-decorator="['id']"/>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="费项名称">
        <a-input placeholder="费项名称" v-decorator="['name', {rules: [{required: true, message: '请输入费项名称'}]}]"/>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="费项类型">
        <a-select v-decorator="['type', {initialValue:'',rules: [{ required: true, message: '请选择' }]}]">
          <a-select-option value="0">系统费项</a-select-option>
          <a-select-option value="1">周期性费项</a-select-option>
          <a-select-option value="2">一次性费项</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="计量单位">
        <a-select v-decorator="['unitsCode', {initialValue:'',rules: [{ required: true, message: '请选择' }]}]">
          <a-select-option value="0">度</a-select-option>
          <a-select-option value="1">平方米</a-select-option>
          <a-select-option value="2">吨</a-select-option>
          <a-select-option value="3">立方米</a-select-option>
          <a-select-option value="4">千克</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="税率%">
        <a-input placeholder="税率%" v-decorator="['taxFee', {rules: [{required: true, message: '请输入税率%'}]}]"/>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="备注">
        <a-input placeholder="备注" v-decorator="['memo']"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script>
import { saveExpenseSettings } from '@/api/business/expenseSettings'
import pick from 'lodash.pick'
export default {
  name: 'ExpenseSettingsModal',
  props: {
  },
  components: {
  },
  data () {
    return {
      visible: false,
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      },
      confirmLoading: false,
      mdl: {},
      form: this.$form.createForm(this)
    }
  },
  beforeCreate () {
  },
  created () {
  },
  methods: {
    add () {
      this.form.resetFields()
      this.edit({ id: 0 })
    },
    edit (record) {
      this.mdl = Object.assign(record)
      this.visible = true
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.mdl, 'id', 'name', 'type', 'unitsCode', 'taxFee', 'memo'))
      })
    },
    handleSubmit (e) {
      e.preventDefault()
      this.form.validateFields((err, values) => {
        if (!err) {
          this.confirmLoading = true
          saveExpenseSettings(values).then(res => {
            if (res.code === 0) {
              this.$message.success('保存成功')
              this.$emit('ok')
              this.visible = false
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
    }
  },
  watch: {}
}
</script>
