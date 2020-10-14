import request from '@/utils/request'

// export function getCanalInstances(params) {
//   return request({
//     url: '/canal/instances',
//     method: 'get',
//     params: params
//   })
// }
//
// export function canalInstanceDetail(id) {
//   return request({
//     url: '/canal/instance/' + id,
//     method: 'get'
//   })
// }
//
// export function updateCanalInstance(data) {
//   return request({
//     url: '/canal/instance',
//     method: 'put',
//     data
//   })
// }
//
// export function addCanalInstance(data) {
//   return request({
//     url: '/canal/instance',
//     method: 'post',
//     data
//   })
// }
//
// export function deleteCanalInstance(id) {
//   return request({
//     url: '/canal/instance/' + id,
//     method: 'delete'
//   })
// }
//
export function startInstance(instance, nodeId) {
  return request({
    url: '/canalClient/instance/' + instance + "/start?nodeId=" + nodeId,
    method: 'put'
  })
}

export function stopInstance(instance, nodeId) {
  return request({
    url: '/canalClient/instance/' + instance + "/stop?nodeId=" + nodeId,
    method: 'put'
  })
}
//
// export function instanceLog(id, nodeId) {
//   return request({
//     url: '/canal/instance/log/' + id + '/' + nodeId,
//     method: 'get'
//   })
// }
//
// export function instanceStatus(id, option) {
//   return request({
//     url: '/canal/instance/status/' + id + '?option=' + option,
//     method: 'put'
//   })
// }

export function getInstances(clientId) {
  return request({
    url: '/canalClient/instances/' + clientId,
    method: 'get'
  })
}

// export function getTemplateInstance() {
//   return request({
//     url: '/canal/instance/template',
//     method: 'get'
//   })
// }
