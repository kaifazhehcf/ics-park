import { axios } from '@/utils/request'

const api = {
  djBanner: '/business/djBanner'
}

export function getDjBannerList (parameter) {
  return axios({
    url: api.djBanner + '/list',
    method: 'get',
    params: parameter
  })
}

export function saveDjBanner (parameter) {
  return axios({
    url: api.djBanner + (parameter.id > 0 ? '/update' : '/save'),
    method: 'post',
    data: parameter,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function delDjBanner (parameter) {
  return axios({
    url: api.djBanner + '/remove',
    method: 'post',
    params: parameter
  })
}

export function getDjBanner (parameter) {
  return axios({
    url: api.djBanner + '/get/' + parameter,
    method: 'get',
    params: ''
  })
}

export function changMarketable (parameter) {
  return axios({
    url: api.djBanner + '/changeMarketable',
    method: 'post',
    data: parameter,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export const djBannerExport = api.djBanner + '/export'
