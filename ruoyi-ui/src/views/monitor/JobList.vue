<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
          <a-col :md="5" :sm="15">
            <a-form-item label="任务名称">
              <a-input placeholder="请输入任务名称" v-model="queryParam.jobName"/>
            </a-form-item>
          </a-col>
          <a-col :md="5" :sm="15">
            <a-form-item label="任务组名">
              <a-select placeholder="请选择任务组名" v-model="queryParam.jobGroup">
                <a-select-option v-for="item in jobGroupOptions" :key="item" :value="item.value">
                  {{ item.text }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="5" :sm="15">
            <a-form-item label="任务状态">
              <a-select placeholder="请选择任务状态" v-model="queryParam.status">
                <a-select-option v-for="item in statusOptions" :key="item" :value="item.value">
                  {{ item.text }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="24">
            <span class="table-page-search-submitButtons">
              <a-button type="primary" @click="$refs.table.refresh(true)">查询</a-button>
              <a-button style="margin-left: 8px" @click="() => queryParam = {}">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <div class="table-operator">
      <a-button v-if="addEnable" type="primary" icon="plus" @click="$refs.modal.add()">新建</a-button>
      <a-dropdown v-if="removeEnable&&selectedRowKeys.length > 0">
        <a-button type="danger" icon="delete" @click="delByIds(selectedRowKeys)">删除</a-button>
      </a-dropdown>
    </div>
    <s-table
      size="default"
      ref="table"
      rowKey="jobId"
      :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
      :columns="columns"
      :data="loadData"
      v-if="jobGroupMap"
    >
      <span slot="jobGroup" slot-scope="text">
        {{ text | jobGroupFilter }}
      </span>
      <span slot="status" slot-scope="text, record">
        <a-switch :checked="record.status=='0'" @change="onChangeStatus(record)"/>
      </span>
      <span slot="action" slot-scope="text, record">
        <a v-if="editEnabel" @click="handleRun(record)">执行一次</a>
        <a-divider type="vertical" />
        <a v-if="editEnabel" @click="handleEdit(record)">编辑</a>
        <a-divider type="vertical" />
        <a v-if="removeEnable" @click="delByIds([record.jobId])">删除</a>
      </span>
    </s-table>
    <sysJob-modal ref="modal" @ok="handleOk"/>
  </a-card>
</template>

<script>
import { STable } from '@/components'
import { getJobList, delJob, changStatus, runJob } from '@/api/monitor'
import SysJobModal from './modules/JobModal.vue'
import { checkPermission } from '@/utils/permissions'
import { getDictArray } from '@/utils/dict'
import pick from 'lodash.pick'

// 任务组名字典翻译
const jobGroupMap = {}

export default {
  name: 'TableList',
  components: {
    STable,
    SysJobModal
  },
  data () {
    return {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      },
      form: this.$form.createForm(this),
      mdl: {},
      // 高级搜索 展开/关闭
      advanced: false,
      // 查询参数
      queryParam: {},
      // 表头
      columns: [
        {
          title: '任务编号',
          dataIndex: 'jobId'
        },
        {
          title: '任务名称',
          dataIndex: 'jobName'
        },
        {
          title: '任务组名',
          dataIndex: 'jobGroup',
          scopedSlots: { customRender: 'jobGroup' }
        },
        {
          title: '调用目标字符串',
          dataIndex: 'invokeTarget'
        },
        {
          title: 'cron执行表达式',
          dataIndex: 'cronExpression'
        },
        {
          title: '计划执行错误策略',
          dataIndex: 'misfirePolicy'
        },
        {
          title: '是否并发执行',
          dataIndex: 'concurrent'
        },
        {
          title: '状态',
          dataIndex: 'status',
          scopedSlots: { customRender: 'status' }
        },
        {
          title: '操作',
          width: '200px',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' }
        }
      ],
      // 加载数据方法 必须为 Promise 对象
      loadData: parameter => {
        return getJobList(Object.assign(parameter, this.queryParam))
      },
      selectedRowKeys: [],
      selectedRows: [],
      // 任务组名字典
      jobGroupOptions: [],
      // 状态字典
      statusOptions: [],
      jobGroupMap,
      addEnable: checkPermission('system:job:add'),
      editEnabel: checkPermission('system:job:edit'),
      removeEnable: checkPermission('system:job:remove')
    }
  },
  filters: {
    jobGroupFilter (jobGroup) {
      return jobGroupMap[jobGroup].text
    }
  },
  async created () {
    // 任务组
    const jobGroupData = await getDictArray('sys_job_group')
    // 任务组名字典翻译
    jobGroupData.map(d => {
      jobGroupMap[d.dictValue] = { text: d.dictLabel }
    })
    // 下拉选项
    jobGroupData.map(d => {
      this.jobGroupOptions.push({ text: d.dictLabel, value: d.dictValue })
    })
    // 状态
    const statusData = await getDictArray('sys_job_status')
    statusData.map(d => {
      this.statusOptions.push({ text: d.dictLabel, value: d.dictValue })
    })
  },
  methods: {
    onSelectChange (selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
    },
    handleAdd () {
      this.$refs.modal.add()
    },
    handleEdit (record) {
      this.$refs.modal.edit(record)
    },
    handleOk () {
      this.$refs.table.refresh(true)
      console.log('handleSaveOk')
    },
    delByIds (ids) {
      delJob({ ids: ids.join(',') }).then(res => {
        if (res.code === 0) {
          this.$message.success('删除成功')
          this.handleOk()
        } else {
          this.$message.error(res.msg)
        }
        this.selectedRowKeys = []
      })
    },
    onChangeStatus (record) {
      const _this = this
      const text = record.status === '1' ? '启用' : '停用'
      this.$confirm({
        title: '警告',
        content: '确认要"' + text + '""' + record.jobName + '"任务吗?',
        okText: '是',
        okType: 'warning',
        cancelText: '否',
        onOk () {
          record.status = record.status === '0' ? '1' : '0'
          changStatus(pick(record, 'jobId', 'status')).then(res => {
            if (res.code === 0) {
              _this.$message.success('操作成功')
            } else {
              _this.$message.error(res.msg)
            }
          })
        },
        onCancel () {}
      })
    },
    /* 立即执行一次 */
    handleRun (record) {
      const _this = this
      this.$confirm({
        title: '警告',
        content: '确认要立即执行一次"' + record.jobName + '"任务吗?',
        okText: '是',
        okType: 'warning',
        cancelText: '否',
        onOk () {
          runJob(pick(record, 'jobId', 'jobGroup')).then(res => {
            if (res.code === 0) {
              _this.$message.success('操作成功')
            } else {
              _this.$message.error(res.msg)
            }
          })
        },
        onCancel () {}
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
