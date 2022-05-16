import request from '@/utils/request'

const api_name = '/api/comment'

export default {
  // 提交评论
  postHospitalComment(hospitalComment) {
    return request({
      url: `${api_name}/postComment`,
      method: 'post',
      data: hospitalComment
    })
  },
  // 根据orderId获取评论
  getHospitalComment(orderId) {
    return request({
      url: `${api_name}/getComment/${orderId}`,
      method: 'get'
    })
  }
}
