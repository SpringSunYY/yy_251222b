import request from '@/utils/request'

// 查询下载记录列表
export function listInfo(query) {
  return request({
    url: '/manage/info/list',
    method: 'get',
    params: query
  })
}

// 查询下载记录详细
export function getInfo(id) {
  return request({
    url: '/manage/info/' + id,
    method: 'get'
  })
}

// 新增下载记录
export function addInfo(data) {
  return request({
    url: '/manage/info',
    method: 'post',
    data: data
  })
}

// 修改下载记录
export function updateInfo(data) {
  return request({
    url: '/manage/info',
    method: 'put',
    data: data
  })
}

// 删除下载记录
export function delInfo(id) {
  return request({
    url: '/manage/info/' + id,
    method: 'delete'
  })
}

// 导入下载记录
export function importInfo(data) {
  return request({
    url: '/manage/info/importData',
    method: 'post',
    data: data
  })
}

// 下载下载记录导入模板
export function importTemplateInfo() {
  return request({
    url: '/manage/info/importTemplate',
    method: 'post',
    responseType: 'blob'
  })
}
