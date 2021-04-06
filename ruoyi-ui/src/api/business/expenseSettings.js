import { axios } from '@/utils/request'

const api = {
  expenseSettings: '/business/expenseSettings'
}

export function getExpenseSettingsList (parameter) {
  return axios({
    url: api.expenseSettings + '/list',
    method: 'get',
    params: parameter
  })
}

export function saveExpenseSettings (parameter) {
  return axios({
    url: api.expenseSettings + (parameter.id > 0 ? '/update' : '/save'),
    method: 'post',
    data: parameter,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function delExpenseSettings (parameter) {
  return axios({
    url: api.expenseSettings + '/remove',
    method: 'post',
    params: parameter
  })
}

export function enabled (parameter) {
  return axios({
    url: api.expenseSettings + '/enabled',
    method: 'post',
    data: parameter,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export const expenseSettingsExport = api.expenseSettings + '/export'
