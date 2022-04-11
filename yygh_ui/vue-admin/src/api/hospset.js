import request from '@/utils/request'

export default {
  // 获取医院设置
  getHospSetList(current, limit, searchObj) {
    return request({
      url: `/admin/hosp/hospitalSet/findPage/${current}/${limit}`,
      method: 'post',
      data: searchObj // 用data则使用json传递，不用json则改成params
    })
  },
  // 删除医院设置
  deleteHospSet(id) {
    return request({
      url: `/admin/hosp/hospitalSet/${id}`,
      method: 'delete'
    })
  },
  // 批量删除
  batchHospSet(idList) {
    return request({
      url: `/admin/hosp/hospitalSet/batchRemove`,
      method: 'delete',
      data: idList
    })
  },
  // 锁定和取消锁定
  lockHospSet(id, status) {
    return request({
      url: `/admin/hosp/hospitalSet/lockHospitalSet/${id}/${status}`,
      method: 'put'
    })
  },
  // 添加医院设置
  saveHospSet(hospitalSet) {
    return request({
      url: `/admin/hosp/hospitalSet/saveHosp`,
      method: 'post',
      data: hospitalSet
    })
  },
  // 设置id查询
  getHospset(id) {
    return request({
      url: `/admin/hosp/hospitalSet/getHospSet/${id}`,
      method: 'get'
    })
  },
  // 修改医院设置
  updateHospset(hospitalSet) {
    return request({
      url: `/admin/hosp/hospitalSet/updateHospitalSet`,
      method: 'post',
      data: hospitalSet
    })
  }
}
