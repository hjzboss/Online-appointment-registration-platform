import request from '@/utils/request'

const api_name = '/api/feedback'

export default {
  // 提交评论
  postFeedback(feedback) {
    return request({
      url: `${api_name}/postFeedback`,
      method: 'post',
      data: feedback
    })
  },
  // 获取反馈
  getFeedback(page, limit) {
    return request({
      url: `${api_name}/getFeedback/${page}/${limit}`,
      method: 'get'
    })
  }
}
