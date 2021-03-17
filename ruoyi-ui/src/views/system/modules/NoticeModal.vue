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
        <a-input v-decorator="['noticeId']"/>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="公告标题">
        <a-input placeholder="公告标题" v-decorator="['noticeTitle', {rules: [{required: true, message: '请输入公告标题'}]}]"/>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="公告类型">
        <a-select placeholder="请选择公告类型" v-decorator="['noticeType', {initialValue:'', rules: [{ required: true, message: '请选择公告类型' }]}]">
          <a-select-option v-for="item in noticeTypeOptions" :key="item" :value="item.value">
            {{ item.text }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="公告状态">
        <a-radio-group placeholder="请选择公告状态" :options="statusOptions" v-decorator="['status', {initialValue:'0'}]" />
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="公告内容">
        <QuillEditor v-decorator="['noticeContent']"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script>
import { saveNotice } from '@/api/system'
import QuillEditor from '@/components/Editor/QuillEditor'
import pick from 'lodash.pick'
import { getDictArray } from '@/utils/dict'
export default {
  name: 'NoticeModal',
  props: {
  },
  components: {
    QuillEditor
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
      noticeTypeOptions: [],
      statusOptions: [],
      confirmLoading: false,
      mdl: {},
      form: this.$form.createForm(this)
    }
  },
  beforeCreate () {
  },
  async created () {
    const [noticeTypeData, statusData] = await Promise.all([getDictArray('sys_notice_type'), getDictArray('sys_notice_status')])
    // 通知类型
    noticeTypeData.map(d => {
      this.noticeTypeOptions.push({ text: d.dictLabel, value: d.dictValue })
    })
    // 通知状态
    statusData.map(d => {
      this.statusOptions.push({ label: d.dictLabel, value: d.dictValue })
    })
  },
  methods: {
    add () {
      this.form.resetFields()
      this.edit({ noticeId: 0 })
    },
    edit (record) {
      this.mdl = Object.assign(record)
      this.visible = true
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.mdl, 'noticeId', 'noticeTitle', 'noticeType', 'noticeContent', 'status', 'remark'))
      })
    },
    handleSubmit (e) {
      e.preventDefault()
      this.form.validateFields((err, values) => {
        if (!err) {
          console.log('Received values of form: ', values)
          this.confirmLoading = true
          saveNotice(values).then(res => {
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
  watch: {
    /*
      'selectedRows': function (selectedRows) {
        this.needTotalList = this.needTotalList.map(item => {
          return {
            ...item,
            total: selectedRows.reduce( (sum, val) => {
              return sum + val[item.dataIndex]
            }, 0)
          }
        })
      }
      */
  }
}
</script>
