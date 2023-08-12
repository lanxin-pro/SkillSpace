import request from '@/utils/request'

// 获得短信日志分页
export function getSmsLogPage(query) {
    return request({
        url: '/system/sms-log/page',
        method: 'get',
        params: query
    })
}
