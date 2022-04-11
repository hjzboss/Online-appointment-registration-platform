import request from '@/utils/request'

const api_name = `/api/hosp/hospital`
export default {
    // 查询医院列表
    getPageList(page, limit, searchObj) {
        return request({
            url: `${api_name}/findHospList/${page}/${limit}`,
            method: 'get',
            params: searchObj
        })
    },
    // 查询医院名称模糊查询
    getByHosname(hosname) {
        return request({
            url: `${api_name}/findByHosname/${hosname}`,
            method: 'get'
        })
    },
    // 根据医院编号获取详情信息
    show(hoscode) {
        return request({
            url: `${api_name}/${hoscode}`,
            method: 'get'
        })
    },
    // 根据医院编号获取科室信息
    findDepartment(hoscode) {
        return request({
            url: `${api_name}/department/${hoscode}`,
            method: 'get'
        })
    }

}