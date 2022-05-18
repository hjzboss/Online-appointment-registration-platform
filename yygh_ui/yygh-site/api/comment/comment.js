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
  },
  // 获取医院评论和平均分
  getAllCommentAndStar(hoscode, limit) {
    return request({
      url: `${api_name}/getAllComment/${hoscode}/${limit}`,
      method: 'get'
    })
  }
}
