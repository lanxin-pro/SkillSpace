import request from "@/utils/request.js"

/**
 * 静默绑定推广人
 * @param {Object} puid
 */
export function spread(puid) {
    return request.get("user/bindSpread?spreadPid="+ puid);
}
