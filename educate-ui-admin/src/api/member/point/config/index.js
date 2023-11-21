import request from '@/utils/request.js'

/**
 * 查询积分设置详情
 *
 * @param params
 * @returns {*}
 */
export const getDBConfig = () => {
    return request({
        url: '/member/point/config/get',
        method: 'get'
    })
}

/**
 * 新增修改积分设置
 *
 * @param data
 * @returns {*}
 */
export const saveConfig = (data) => {
    return request({
        url: '/member/point/config/save',
        method: 'put',
        data: data
    })
}
