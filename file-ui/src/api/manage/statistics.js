import request from '@/utils/request'

// 浏览记录统计
export function viewStatistics() {
  return request({
    url: '/manage/statistics/view',
    method: 'get'
  })
}
