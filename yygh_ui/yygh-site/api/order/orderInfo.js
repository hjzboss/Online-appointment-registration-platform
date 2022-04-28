import request from '@/utils/request'

const api_name = `/api/order/orderInfo`

export default {
  //提交订单
  submitOrder(scheduleId, patientId) {
    return request({
      url: `${api_name}/auth/submitOrder/${scheduleId}/${patientId}`,
      method: 'post'
    })
  },

  //获取订单列表，条件查询带分页
  getPageList(page, limit, searchObj) {
    return request({
      url: `${api_name}/auth/${page}/${limit}`,
      method: 'get',
      params: searchObj
    })
  },

  //订单状态
  getStatusList() {
    return request({
      url: `${api_name}/auth/getStatusList`,
      method: 'get'
    })
  },


  getOrders(orderId) {
    return request({
      url: `${api_name}/auth/getOrders/${orderId}`,
      method: 'get'
    })
  },

  cancelOrder(orderId) {
    return request({
      url: `${api_name}/auth/cancelOrder/${orderId}`,
      method: 'get'
    })
  }
}
