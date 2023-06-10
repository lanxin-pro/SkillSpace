import request from "@/utils/request.js";

export function getAuthorize(clientId) {
    return request({
        url: '/system/oauth2/authorize',
        method: 'get',
        params: {
            clientId
        }
    })
}
