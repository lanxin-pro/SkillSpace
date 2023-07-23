import request from '@/utils/request'


/**
 * 获得数据源配置列表
 *
 * @returns {*}
 */
export function getDataSourceConfigList() {
    return request({
        url: '/infra/data-source-config/list',
        method: 'get',
    })
}
