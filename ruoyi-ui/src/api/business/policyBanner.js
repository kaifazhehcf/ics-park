import { axios } from '@/utils/request'

const api = {
  policyBanner: '/business/policyBanner'
}

export function getPolicyBannerList (parameter) {
  return axios({
    url: api.policyBanner + '/list',
    method: 'get',
    params: parameter
  })
}

export function savePolicyBanner (parameter) {
  return axios({
    url: api.policyBanner + (parameter.id > 0 ? '/update' : '/save'),
    method: 'post',
    data: parameter,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function delPolicyBanner (parameter) {
  return axios({
    url: api.policyBanner + '/remove',
    method: 'post',
    params: parameter
  })
}

export function getPolicyBanner (parameter) {
  return axios({
    url: api.policyBanner + '/get/' + parameter,
    method: 'get',
    params: ''
  })
}

export function changMarketable (parameter) {
  return axios({
    url: api.policyBanner + '/changeMarketable',
    method: 'post',
    data: parameter,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export const policyBannerExport = api.policyBanner + '/export'
