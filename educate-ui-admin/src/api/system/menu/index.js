import request from '@/utils/request'

// 查询菜单列表
export const listMenu = (query) => {
    return request({
        url: '/system/menu/list',
        method: 'get',
        params: query
    })
}
