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
        <a-input v-decorator="['jobId']"/>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="任务名称">
        <a-input placeholder="请输入任务名称" v-decorator="['jobName', {rules: [{ required: true, message: '请选择任务组名' }]}]"/>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="任务分组">
        <a-select placeholder="请选择任务组名" v-decorator="['jobGroup', {initialValue:'DEFAULT', rules: [{ required: true, message: '请选择任务组名' }]}]">
          <a-select-option v-for="item in jobGroupOptions" :key="item" :value="item.value">
            {{ item.text }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="调用方法">
        <a-input placeholder="请输入调用目标字符串" v-decorator="['invokeTarget', {rules: [{ required: true, message: '请输入调用目标字符串' }]}]">
          <a-tooltip slot="suffix">
            <template slot="title">
              Bean调用示例：ryTask.ryParams('ry')
              <br />Class类调用示例：com.ruoyi.quartz.task.RyTask.ryParams('ry')
              <br />参数说明：支持字符串，布尔类型，长整型，浮点型，整型
            </template>
            <a-icon type="info-circle" style="color: rgba(0,0,0,.45)" />
          </a-tooltip>
        </a-input>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="cron表达式">
        <a-input placeholder="请输入cron执行表达式" v-decorator="['cronExpression', {rules: [{ required: true, message: '请输入cron执行表达式' }]}]"/>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="错误策略">
        <a-radio-group v-decorator="['misfirePolicy']">
          <a-radio-button value="1"> 立即执行 </a-radio-button>
          <a-radio-button value="2"> 执行一次 </a-radio-button>
          <a-radio-button value="3"> 放弃执行 </a-radio-button>
        </a-radio-group>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="是否并发">
        <a-radio-group v-decorator="['concurrent']">
          <a-radio-button value="0"> 允许 </a-radio-button>
          <a-radio-button value="1"> 禁止 </a-radio-button>
        </a-radio-group>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="状态">
        <a-radio-group v-decorator="['status', {initialValue: '0'}]">
          <a-radio :value="'0'">是</a-radio>
          <a-radio :value="'1'">否</a-radio>
        </a-radio-group>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script>
import { saveJob } from '@/api/monitor'
import { getDictArray } from '@/utils/dict'
import pick from 'lodash.pick'
export default {
  name: 'SysJobModal',
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
      // 任务组名字典
      jobGroupOptions: [],
      confirmLoading: false,
      mdl: {},
      form: this.$form.createForm(this)
    }
  },
  beforeCreate () {
  },
  async created () {
    // 任务组
    const jobGroupData = await getDictArray('sys_job_group')
    // 下拉选项
    jobGroupData.map(d => {
      this.jobGroupOptions.push({ text: d.dictLabel, value: d.dictValue })
    })
  },
  methods: {
    add () {
      this.form.resetFields()
      this.edit({ jobId: 0 })
    },
    edit (record) {
      this.mdl = Object.assign(record)
      this.visible = true
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.mdl, 'jobId', 'jobName', 'jobGroup', 'invokeTarget', 'cronExpression', 'misfirePolicy', 'concurrent', 'status'))
      })
    },
    handleSubmit (e) {
      e.preventDefault()
      this.form.validateFields((err, values) => {
        if (!err) {
          console.log('Received values of form: ', values)
          this.confirmLoading = true
          saveJob(values).then(res => {
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
