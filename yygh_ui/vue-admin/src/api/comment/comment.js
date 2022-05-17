import request from '@/utils/request'

const api_name = '/admin/comment'

export default {
  //获取分页列表
  getCommentPageList(page, limit, searchObj) {
    return request({
      url: `${api_name}/${page}/${limit}`,
      method: 'get',
      params: searchObj
    })
  },
  //修改评论状态
  changeStatus(id, status) {
    return request({
      url: `${api_name}/updateStatus/${id}/${status}`,
      method: 'put'
    })
  },
  //获取评论详情
  getById(id) {
    return request({
      url: `${api_name}/getComment/${id}`,
      method: 'get'
    })
  },
  //删除评论
  deleteById(id) {
    return request({
      url: `${api_name}/deleteComment/${id}`,
      method: 'delete'
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
