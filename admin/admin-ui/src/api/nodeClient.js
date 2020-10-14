import request from '@/utils/request'

export function getNodeClients(params) {
  return request({
    url: '/nodeClients',
    method: 'get',
    params: params
  })
}

export function addNodeClient(data) {
  return request({
    url: '/nodeClient',
    method: 'post',
    data
  })
}

export function updateNodeClient(data) {
  return request({
    url: '/nodeClient',
    method: 'put',
    data
  })
}

export function deleteNodeClient(id) {
  return request({
    url: '/nodeClient/' + id,
    method: 'delete'
  })
}

export function nodeClientLog(id) {
  return request({
    url: '/nodeClient/log/' + id,
    method: 'get'
  })
}
