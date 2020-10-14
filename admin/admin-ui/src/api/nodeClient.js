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
//
// export function nodeServerDetail(id) {
//   return request({
//     url: '/nodeServer/' + id,
//     method: 'get'
//   })
// }
//
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
//
// export function startNodeServer(id) {
//   return request({
//     url: '/nodeServer/start/' + id,
//     method: 'put'
//   })
// }
//
// export function stopNodeServer(id) {
//   return request({
//     url: '/nodeServer/stop/' + id,
//     method: 'put'
//   })
// }

export function nodeClientLog(id) {
  return request({
    url: '/nodeClient/log/' + id,
    method: 'get'
  })
}
