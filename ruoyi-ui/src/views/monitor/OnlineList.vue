<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
          <a-col :md="5" :sm="15">
            <a-form-item label="登录账号">
              <a-input placeholder="请输入登录账号" v-model="queryParam.loginName"/>
            </a-form-item>
          </a-col>
          <a-col :md="5" :sm="15">
            <a-form-item label="登录IP地址">
              <a-input placeholder="请输入登录IP地址" v-model="queryParam.ipaddr"/>
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
    <s-table
      size="default"
      ref="table"
      rowKey="tokenId"
      :columns="columns"
      :data="loadData"
    >
      <span slot="serial" slot-scope="text, record, index">
        {{ index + 1 }}
      </span>
      <span slot="action" slot-scope="text, record">
        <a v-if="editEnabel" @click="handleForceLogout(record)">强退</a>
      </span>
    </s-table>
  </a-card>
</template>

<script>
import { STable } from '@/components'
import { getOnlineList, forceLogout } from '@/api/monitor'
import { checkPermission } from '@/utils/permissions'
import pick from 'lodash.pick'

export default {
  name: 'TableList',
  components: {
    STable
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
          title: '#',
          scopedSlots: { customRender: 'serial' }
        },
        {
          title: '会话编号',
          dataIndex: 'tokenId'
        },
        {
          title: '登录账号',
          dataIndex: 'loginName'
        },
        {
          title: '部门名称',
          dataIndex: 'deptName'
        },
        {
          title: '登录IP地址',
          dataIndex: 'ipaddr'
        },
        {
          title: '登录地点',
          dataIndex: 'loginLocation'
        },
        {
          title: '浏览器类型',
          dataIndex: 'browser'
        },
        {
          title: '操作系统',
          dataIndex: 'os'
        },
        {
          title: '登录时间',
          dataIndex: 'loginTime'
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
        return getOnlineList(Object.assign(parameter, this.queryParam))
      },
      editEnabel: checkPermission('system:userOnline:edit')
    }
  },
  filters: {
  },
  created () {
  },
  methods: {
    handleForceLogout (record) {
      const _this = this
      this.$confirm({
        title: '警告',
        content: '是否确认强退名称为"' + record.loginName + '"的数据项?',
        okText: '是',
        okType: 'warning',
        cancelText: '否',
        onOk () {
          forceLogout(pick(record, 'tokenId')).then(res => {
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
