import request from '@/utils/request'

// 查询收藏记录列表
export function listCollectInfo(query) {
  return request({
    url: '/manage/collectInfo/list',
    method: 'get',
    params: query
  })
}

// 查询收藏记录详细
export function getCollectInfo(id) {
  return request({
    url: '/manage/collectInfo/' + id,
    method: 'get'
  })
}

// 新增收藏记录
export function addCollectInfo(data) {
  return request({
    url: '/manage/collectInfo',
    method: 'post',
    data: data
  })
}

// 修改收藏记录
export function updateCollectInfo(data) {
  return request({
    url: '/manage/collectInfo',
    method: 'put',
    data: data
  })
}

// 删除收藏记录
export function delCollectInfo(id) {
  return request({
    url: '/manage/collectInfo/' + id,
    method: 'delete'
  })
}

// 导入收藏记录
export function importCollectInfo(data) {
  return request({
    url: '/manage/collectInfo/importData',
    method: 'post',
    data: data
  })
}

// 下载收藏记录导入模板
export function importTemplateCollectInfo() {
  return request({
    url: '/manage/collectInfo/importTemplate',
    method: 'post',
    responseType: 'blob'
  })
}
