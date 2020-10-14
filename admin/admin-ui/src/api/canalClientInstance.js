import request from '@/utils/request'

export function startInstance(instance, nodeId) {
  return request({
    url: '/canalClient/instance/' + instance + '/start?nodeId=' + nodeId,
    method: 'put'
  })
}

export function stopInstance(instance, nodeId) {
  return request({
    url: '/canalClient/instance/' + instance + '/stop?nodeId=' + nodeId,
    method: 'put'
  })
}

export function getInstances(clientId) {
  return request({
    url: '/canalClient/instances/' + clientId,
    method: 'get'
  })
}
