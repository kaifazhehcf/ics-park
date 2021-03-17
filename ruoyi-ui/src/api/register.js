import { axios } from '@/utils/request'

const api = {
  register: '/user/register'
}

export function getRegisterSms (parameter) {
  return axios({
    url: api.register + '/sms',
    method: 'get',
    params: parameter
  })
}

export function registerSubmit (parameter) {
  return axios({
    url: api.register + '/submit',
    method: 'post',
    params: parameter,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
