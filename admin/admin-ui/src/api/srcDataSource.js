import request from '@/utils/request'

export function getSchemas() {
  return request({
    url: '/canal/srcDataSource/schemas',
    method: 'get'
  })
}

export function getTables(schema) {
  return request({
    url: '/canal/srcDataSource/schemas/' + schema + '/tables',
    method: 'get'
  })
}

