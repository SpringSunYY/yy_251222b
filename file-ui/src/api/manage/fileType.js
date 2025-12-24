import request from '@/utils/request'

// 查询文件分类列表
export function listFileType(query) {
  return request({
    url: '/manage/fileType/list',
    method: 'get',
    params: query
  })
}

// 查询文件分类详细
export function getFileType(id) {
  return request({
    url: '/manage/fileType/' + id,
    method: 'get'
  })
}

// 新增文件分类
export function addFileType(data) {
  return request({
    url: '/manage/fileType',
    method: 'post',
    data: data
  })
}

// 修改文件分类
export function updateFileType(data) {
  return request({
    url: '/manage/fileType',
    method: 'put',
    data: data
  })
}

// 删除文件分类
export function delFileType(id) {
  return request({
    url: '/manage/fileType/' + id,
    method: 'delete'
  })
}

// 导入文件分类
export function importFileType(data) {
  return request({
    url: '/manage/fileType/importData',
    method: 'post',
    data: data
  })
}

// 下载文件分类导入模板
export function importTemplateFileType() {
  return request({
    url: '/manage/fileType/importTemplate',
    method: 'post',
    responseType: 'blob'
  })
}
