import request from '@/utils/request.js'

/**
 * 获取所有国家和时区列表
 *
 * @param params
 * @returns {*}
 */
export const getCountryWithTimeZoneList = params => {
    return request({
        url: `/system/country/getCountryWithTimeZoneList`,
        method: 'get',
        params
    })
}
