import { axios } from '@/utils/request'

const api = {
  dashboard: '/dashboard/analysis'
}

export function getBillData (parameter) {
  return axios({
    url: api.dashboard + '/bill_data',
    method: 'get',
    params: parameter
  })
}

export function getBillRentData (parameter) {
  return axios({
    url: api.dashboard + '/bill_rent_data',
    method: 'get',
    params: parameter
  })
}

export function getBillManagementData (parameter) {
  return axios({
    url: api.dashboard + '/bill_management_data',
    method: 'get',
    params: parameter
  })
}

export function getBillPwData (parameter) {
  return axios({
    url: api.dashboard + '/bill_pw_data',
    method: 'get',
    params: parameter
  })
}

export function getBillYearData (parameter) {
  return axios({
    url: api.dashboard + '/bill_year_data',
    method: 'get',
    params: parameter
  })
}

export function getRankList (parameter) {
  return axios({
    url: api.dashboard + '/rank_list',
    method: 'get',
    params: parameter
  })
}

export function getCountApprovingContracts (parameter) {
  return axios({
    url: api.dashboard + '/count_approving_contracts',
    method: 'get',
    params: parameter
  })
}

export function getCountNoPayBills (parameter) {
  return axios({
    url: api.dashboard + '/count_no_pay_bills',
    method: 'get',
    params: parameter
  })
}

export function getCountRentArea (parameter) {
  return axios({
    url: api.dashboard + '/count_rent_area',
    method: 'get',
    params: parameter
  })
}

export function getCountSaleData (parameter) {
  return axios({
    url: api.dashboard + '/product_sale_data',
    method: 'get',
    params: parameter
  })
}
