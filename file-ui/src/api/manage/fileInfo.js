import request from '@/utils/request'

// 查询文件信息列表
export function listFileInfo(query) {
  return request({
    url: '/manage/fileInfo/list',
    method: 'get',
    params: query
  })
}

// 查询文件信息详细
export function getFileInfo(id) {
  return request({
    url: '/manage/fileInfo/' + id,
    method: 'get'
  })
}

// 新增文件信息
export function addFileInfo(data) {
  return request({
    url: '/manage/fileInfo',
    method: 'post',
    data: data
  })
}

// 修改文件信息
export function updateFileInfo(data) {
  return request({
    url: '/manage/fileInfo',
    method: 'put',
    data: data
  })
}

// 删除文件信息
export function delFileInfo(id) {
  return request({
    url: '/manage/fileInfo/' + id,
    method: 'delete'
  })
}

// 导入文件信息
export function importFileInfo(data) {
  return request({
    url: '/manage/fileInfo/importData',
    method: 'post',
    data: data
  })
}

// 下载文件信息导入模板
export function importTemplateFileInfo() {
  return request({
    url: '/manage/fileInfo/importTemplate',
    method: 'post',
    responseType: 'blob'
  })
}
