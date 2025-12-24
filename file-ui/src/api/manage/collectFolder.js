import request from '@/utils/request'

// 查询收藏夹列表
export function listCollectFolder(query) {
  return request({
    url: '/manage/collectFolder/list',
    method: 'get',
    params: query
  })
}

// 查询收藏夹详细
export function getCollectFolder(id) {
  return request({
    url: '/manage/collectFolder/' + id,
    method: 'get'
  })
}

// 新增收藏夹
export function addCollectFolder(data) {
  return request({
    url: '/manage/collectFolder',
    method: 'post',
    data: data
  })
}

// 修改收藏夹
export function updateCollectFolder(data) {
  return request({
    url: '/manage/collectFolder',
    method: 'put',
    data: data
  })
}

// 删除收藏夹
export function delCollectFolder(id) {
  return request({
    url: '/manage/collectFolder/' + id,
    method: 'delete'
  })
}

// 导入收藏夹
export function importCollectFolder(data) {
  return request({
    url: '/manage/collectFolder/importData',
    method: 'post',
    data: data
  })
}

// 下载收藏夹导入模板
export function importTemplateCollectFolder() {
  return request({
    url: '/manage/collectFolder/importTemplate',
    method: 'post',
    responseType: 'blob'
  })
}
