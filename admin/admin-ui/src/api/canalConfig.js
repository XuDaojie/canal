import request from '@/utils/request'

export function getCanalConfig(clusterId, serverId, params) {
  return request({
    url: '/canal/config/' + clusterId + '/' + serverId,
    method: 'get',
    params: params
  })
}

export function updateCanalConfig(data) {
  return request({
    url: '/canal/config',
    method: 'put',
    data
  })
}

export function getTemplateConfig() {
  return request({
    url: '/canal/config/template',
    method: 'get'
  })
}

export function getClientTemplateConfig() {
  return request({
    url: '/canal/config/client/template',
    method: 'get'
  })
}
