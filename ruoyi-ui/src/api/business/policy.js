import { axios } from '@/utils/request'

const api = {
  policy: '/business/policy'
}

export function getPolicyList (parameter) {
  return axios({
    url: api.policy + '/list',
    method: 'get',
    params: parameter
  })
}

export function savePolicy (parameter) {
  return axios({
    url: api.policy + (parameter.id > 0 ? '/update' : '/save'),
    method: 'post',
    data: parameter,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function delPolicy (parameter) {
  return axios({
    url: api.policy + '/remove',
    method: 'post',
    params: parameter
  })
}

export function getPolicy (parameter) {
  return axios({
    url: api.policy + '/get/' + parameter,
    method: 'get',
    params: ''
  })
}

export function changMarketable (parameter) {
  return axios({
    url: api.policy + '/changeMarketable',
    method: 'post',
    data: parameter,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export const policyExport = api.policy + '/export'
