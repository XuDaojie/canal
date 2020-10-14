import request from '@/utils/request'

export function getCanalAdapters(params) {
  return request({
    url: '/canal/adapters',
    method: 'get',
    params: params
  })
}

export function canalAdapterDetail(id) {
  return request({
    url: '/canal/adapter/' + id,
    method: 'get'
  })
}

export function updateCanalAdapter(data) {
  return request({
    url: '/canal/adapter',
    method: 'put',
    data
  })
}

export function addCanalAdapter(data) {
  return request({
    url: '/canal/adapter',
    method: 'post',
    data
  })
}

export function deleteCanalAdapter(id) {
  return request({
    url: '/canal/adapter/' + id,
    method: 'delete'
  })
}

export function startInstance(id, nodeId) {
  return request({
    url: '/canal/instance/start/' + id + '/' + nodeId,
    method: 'put'
  })
}

export function stopInstance(id, nodeId) {
  return request({
    url: '/canal/instance/stop/' + id + '/' + nodeId,
    method: 'put'
  })
}

export function instanceLog(id, nodeId) {
  return request({
    url: '/canal/instance/log/' + id + '/' + nodeId,
    method: 'get'
  })
}

export function instanceStatus(id, option) {
  return request({
    url: '/canal/instance/status/' + id + '?option=' + option,
    method: 'put'
  })
}

export function getActiveInstances(serverId) {
  return request({
    url: '/canal/active/instances/' + serverId,
    method: 'get'
  })
}

export function getTemplateAdapter(params) {
  return request({
    url: '/canal/adapter/template',
    method: 'get',
    params: params
  })
}
