import request from '@/utils/request'

const api_name = '/admin/feedback'

export default {
  // 条件获取反馈
  getFeedbackPageList(page, limit, searchObj) {
    return request({
      url: `${api_name}/${page}/${limit}`,
      method: 'get',
      params: searchObj
    })
  },
  // 删除反馈
  deleteFeedback(id) {
    return request({
      url: `${api_name}/${id}`,
      method: 'delete',
    })
  },
  // 根据id获取反馈
  getFeedback(id) {
    return request({
      url: `${api_name}/${id}`,
      method: 'get',
    })
  },

  //批量删除
  batchRemoveHospitalSet(idList) {
    return request({
      url: `${api_name}/batchRemove`,
      method: 'delete',
      data: idList
    })
  }
}
