import request from '@/utils/request'

const api_name = '/admin/order/orderInfo'
export default {
  //获取分页列表
  getPageList(page, limit, searchObj) {
    return request({
      url: `${api_name}/${page}/${limit}`,
      method: 'get',
      params: searchObj
    })
  },
  //获取订单状态
  getStatusList() {
    return request({
      url: `${api_name}/getStatusList`,
      method: 'get'
    })
  },
  //获取订单详情
  getById(id) {
    return request({
      url: `${api_name}/show/${id}`,
      method: 'get'
    })
  }

}
