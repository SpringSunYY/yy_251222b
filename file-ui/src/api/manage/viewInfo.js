import request from '@/utils/request'

// 查询浏览记录列表
export function listViewInfo(query) {
  return request({
    url: '/manage/viewInfo/list',
    method: 'get',
    params: query
  })
}

// 查询浏览记录详细
export function getViewInfo(id) {
  return request({
    url: '/manage/viewInfo/' + id,
    method: 'get'
  })
}

// 新增浏览记录
export function addViewInfo(data) {
  return request({
    url: '/manage/viewInfo',
    method: 'post',
    data: data
  })
}

// 修改浏览记录
export function updateViewInfo(data) {
  return request({
    url: '/manage/viewInfo',
    method: 'put',
    data: data
  })
}

// 删除浏览记录
export function delViewInfo(id) {
  return request({
    url: '/manage/viewInfo/' + id,
    method: 'delete'
  })
}

// 导入浏览记录
export function importViewInfo(data) {
  return request({
    url: '/manage/viewInfo/importData',
    method: 'post',
    data: data
  })
}

// 下载浏览记录导入模板
export function importTemplateViewInfo() {
  return request({
    url: '/manage/viewInfo/importTemplate',
    method: 'post',
    responseType: 'blob'
  })
}
