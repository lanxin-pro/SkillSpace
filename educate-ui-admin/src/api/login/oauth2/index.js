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

export function authorize(responseType, clientId, redirectUri, state,
                          autoApprove, checkedScopes, uncheckedScopes) {
    // 构建 scopes
    const scopes = {}
    for (const scope of checkedScopes) {
        scopes[scope] = true
    }
    for (const scope of uncheckedScopes) {
        scopes[scope] = false
    }
    // 发起请求
    return request({
        url: '/system/oauth2/authorize',
        headers: {
            'Content-type': 'application/x-www-form-urlencoded'
        },
        params: {
            response_type: responseType,
            client_id: clientId,
            redirect_uri: redirectUri,
            state: state,
            auto_approve: autoApprove,
            scope: JSON.stringify(scopes)
        },
        method: 'post'
    })
}
