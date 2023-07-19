import request from '@/utils/request'

/**
 * 查询全部字典数据列表
 *
 * @returns {AxiosPromise}
 */
export function listSimpleDictData() {
    return request({
        url: '/system/dict-data/list-all-simple',
        method: 'get',
    })
}
