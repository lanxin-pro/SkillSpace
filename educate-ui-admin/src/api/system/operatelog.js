import request from '@/utils/request'

// 查询操作日志列表
export const listOperateLog = (query) => {
    return request({
        url: '/system/operate-log/page',
        method: 'get',
        params: query
    })
}
