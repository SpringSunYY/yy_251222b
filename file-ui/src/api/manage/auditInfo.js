import request from '@/utils/request'

// 查询审核信息列表
export function listAuditInfo(query) {
  return request({
    url: '/manage/auditInfo/list',
    method: 'get',
    params: query
  })
}

// 查询审核信息详细
export function getAuditInfo(id) {
  return request({
    url: '/manage/auditInfo/' + id,
    method: 'get'
  })
}

// 新增审核信息
export function addAuditInfo(data) {
  return request({
    url: '/manage/auditInfo',
    method: 'post',
    data: data
  })
}

// 修改审核信息
export function updateAuditInfo(data) {
  return request({
    url: '/manage/auditInfo',
    method: 'put',
    data: data
  })
}

// 删除审核信息
export function delAuditInfo(id) {
  return request({
    url: '/manage/auditInfo/' + id,
    method: 'delete'
  })
}

// 导入审核信息
export function importAuditInfo(data) {
  return request({
    url: '/manage/auditInfo/importData',
    method: 'post',
    data: data
  })
}

// 下载审核信息导入模板
export function importTemplateAuditInfo() {
  return request({
    url: '/manage/auditInfo/importTemplate',
    method: 'post',
    responseType: 'blob'
  })
}
