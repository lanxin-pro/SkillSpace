import request from "@/utils/request.js";

/**
 * 查询文件配置列表
 *
 * @param params
 * @returns {*}
 */
export function getFileConfigPage(params) {
    return request({
        url: '/infra/file-config/page',
        method: 'get',
        params: params
    })
}
