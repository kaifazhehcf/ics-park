import { axios } from '@/utils/request'

const api = {
  park: '/admin/park'
}

export function getParkById (id) {
  return axios({
    url: api.park + '/get/' + id,
    method: 'get'
  })
}

export function getParkList (parameter) {
  return axios({
    url: api.park + '/list',
    method: 'get',
    params: parameter
  })
}

export function savePark (parameter) {
  return axios({
    url: api.park + (parameter.id > 0 ? '/update' : '/save'),
    method: 'post',
    data: parameter,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function delPark (parameter) {
  return axios({
    url: api.park + '/remove',
    method: 'post',
    params: parameter
  })
}

export function changMarketable (parameter) {
  return axios({
    url: api.park + '/changeMarketable',
    method: 'post',
    data: parameter,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export const parkExport = api.park + '/export'
