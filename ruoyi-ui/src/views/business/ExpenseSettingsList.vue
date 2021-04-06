<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
          <a-col :md="5" :sm="15">
            <a-form-item label="费项名称">
              <a-input placeholder="请输入费项名称" v-model="queryParam.name"/>
            </a-form-item>
          </a-col>
          <a-col :md="5" :sm="15">
            <a-form-item label="费项类型">
              <a-tree-select
                v-model="queryParam.type"
                :dropdownStyle="{ maxHeight: '400px', overflow: 'auto' }"
                :treeData="menus"
                placeholder="请选择"
                treeDefaultExpandAll
              ></a-tree-select>
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
      rowKey="id"
      showPagination="true"
      :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
      :columns="columns"
      :data="loadData"
    >
      <span slot="type" slot-scope="text">
        {{ text | typeFilter }}
      </span>
      <span slot="unitsCode" slot-scope="text">
        {{ text | unitsCodeFilter }}
      </span>
      <span slot="isEnabled" slot-scope="text, record">
        <a-switch :checked="record.isEnabled==true" @change="onChangeStatus(record)"/>
      </span>
      <span slot="action" slot-scope="text, record">
        <a v-if="editEnabel" @click="handleEdit(record)">编辑</a>
        <a-divider type="vertical" />
        <a v-if="removeEnable" @click="delByIds([record.id])">删除</a>
      </span>
    </s-table>
    <expenseSettings-modal ref="modal" @ok="handleOk"/>
  </a-card>
</template>

<script>
import { STable } from '@/components'
import { getExpenseSettingsList, delExpenseSettings, enabled } from '@/api/business/expenseSettings'
import ExpenseSettingsModal from './modules/ExpenseSettingsModal.vue'
import { checkPermission } from '@/utils/permissions'
import pick from 'lodash.pick'

export default {
  name: 'TableList',
  components: {
    STable,
    ExpenseSettingsModal
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
      // 下拉框赋值
      menus: [
        { key: 0, value: '0', title: '系统费项' },
        { key: 1, value: '1', title: '周期性费项' },
        { key: 2, value: '2', title: '一次性费项' }],
      // 高级搜索 展开/关闭
      advanced: false,
      // 查询参数
      queryParam: {},
      // 表头
      columns: [
        {
          title: '费项名称',
          dataIndex: 'name'
        },
        {
          title: '说明',
          dataIndex: 'memo'
        },
        {
          title: '费项类型',
          dataIndex: 'type',
          scopedSlots: { customRender: 'type' }
        },
        {
          title: '单位',
          dataIndex: 'unitsCode',
          scopedSlots: { customRender: 'unitsCode' }
        },
        {
          title: '税率%',
          dataIndex: 'taxFee'
        },
        {
          title: '状态',
          dataIndex: 'isEnabled',
          scopedSlots: { customRender: 'isEnabled' }
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
        return getExpenseSettingsList(Object.assign(parameter, this.queryParam))
      },
      selectedRowKeys: [],
      selectedRows: [],
      addEnable: checkPermission('business:expenseSettings:add'),
      editEnabel: checkPermission('business:expenseSettings:edit'),
      removeEnable: checkPermission('business:expenseSettings:remove')
    }
  },
  filters: {
    isEnabledFilter (isEnabled) {
      const enabledMap = {
        true: '启动',
        false: '停用'
      }
      return enabledMap[isEnabled]
    },
    typeFilter (type) {
      const typeMap = {
        0: '系统费项',
        1: '周期性费项',
        2: '一次性费项'
      }
      return typeMap[type]
    },
    unitsCodeFilter (unitsCode) {
      const unitsCodeMap = {
        0: '度',
        1: '平方米',
        2: '吨',
        3: '立方米',
        4: '千克'
      }
      return unitsCodeMap[unitsCode]
    }
  },
  created () {
  },
  methods: {
    onChangeStatus (record) {
      const _this = this
      console.log('record : ', record)
      const text = record.isEnabled === false ? '启动' : '停用'
      this.$confirm({
        title: '警告',
        content: '确认要' + text + '【' + record.name + '】' + '吗?',
        okText: '是',
        okType: 'warning',
        cancelText: '否',
        onOk () {
          record.isEnabled = !record.isEnabled
          enabled(pick(record, 'id', 'isEnabled')).then(res => {
            if (res.code === 0) {
              _this.$message.success('操作成功')
              _this.$refs.table.refresh(true)
            } else {
              _this.$message.error(res.msg)
            }
          })
        },
        onCancel () {}
      })
    },
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
    },
    delByIds (ids) {
      delExpenseSettings({ ids: ids.join(',') }).then(res => {
        if (res.code === 0) {
          this.$message.success('删除成功')
          this.handleOk()
        } else {
          this.$message.error(res.msg)
        }
        this.selectedRowKeys = []
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
