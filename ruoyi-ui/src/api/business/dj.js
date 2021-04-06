import { axios } from '@/utils/request'

const api = {
  dj: '/business/dj'
}

export function getDjList (parameter) {
  return axios({
    url: api.dj + '/list',
    method: 'get',
    params: parameter
  })
}

export function saveDj (parameter) {
  return axios({
    url: api.dj + (parameter.id > 0 ? '/update' : '/save'),
    method: 'post',
    data: parameter,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function delDj (parameter) {
  return axios({
    url: api.dj + '/remove',
    method: 'post',
    params: parameter
  })
}

export function getDj (parameter) {
  return axios({
    url: api.dj + '/get/' + parameter,
    method: 'get',
    params: ''
  })
}

export function changMarketable (parameter) {
  return axios({
    url: api.dj + '/changeMarketable',
    method: 'post',
    data: parameter,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export const djExport = api.dj + '/export'
