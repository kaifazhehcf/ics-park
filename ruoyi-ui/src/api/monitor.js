import { axios } from '@/utils/request'

const api = {
  operLog: '/monitor/operLog',
  loginLog: '/monitor/loginInfo',
  job: '/monitor/job',
  online: '/monitor/online'
}

// OperLog
export function getOperLogList (parameter) {
  return axios({
    url: api.operLog + '/list',
    method: 'get',
    params: parameter
  })
}

export function delOperLog (parameter) {
  return axios({
    url: api.operLog + '/remove',
    method: 'post',
    params: parameter
  })
}
export function cleanOperLog () {
  return axios({
    url: api.operLog + '/clean',
    method: 'post'
  })
}
// LoginLog
export function getLoginLogList (parameter) {
  return axios({
    url: api.loginLog + '/list',
    method: 'get',
    params: parameter
  })
}
export function delLoginLog (parameter) {
  return axios({
    url: api.loginLog + '/remove',
    method: 'post',
    params: parameter
  })
}
export function cleanLoginLog () {
  return axios({
    url: api.loginLog + '/clean',
    method: 'post'
  })
}

// Job
export function getJobList (parameter) {
  return axios({
    url: api.job + '/list',
    method: 'get',
    params: parameter
  })
}

export function saveJob (parameter) {
  return axios({
    url: api.job + (parameter.jobId > 0 ? '/update' : '/save'),
    method: 'post',
    data: parameter,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function delJob (parameter) {
  return axios({
    url: api.job + '/remove',
    method: 'post',
    params: parameter
  })
}

export function changStatus (parameter) {
  return axios({
    url: api.job + '/changeStatus',
    method: 'post',
    data: parameter
  })
}
// 定时任务立即执行一次
export function runJob (parameter) {
  return axios({
    url: api.job + '/run',
    method: 'post',
    data: parameter
  })
}

// 查询在线用户列表
export function getOnlineList (parameter) {
  return axios({
    url: api.online + '/list',
    method: 'get',
    params: parameter
  })
}

// 强退用户
export function forceLogout (parameter) {
  return axios({
    url: api.online + '/forceLogout',
    method: 'post',
    params: {
      tokenId: parameter.tokenId
    }
  })
}

export const operLogExport = api.operLog + '/export'
