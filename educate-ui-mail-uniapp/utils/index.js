import { spread } from "@/api/user.js"

/**
 * 静默授权绑定上下级，使用在已经登录后扫描了别人的推广二维码
 *
 * @param {Object} puid
 */
export function silenceBindingSpread() {
    let puid = Cache.get('spread');
    puid = parseInt(puid);
    if (Number.isNaN(puid)) {
        puid = 0;
    }
    if (puid) {
        spread(puid).then(res => {}).catch(res => {
          console.log('静默推广失败')
        })
        Cache.clear('spread');
    } else {
        Cache.set('spread', 0);
    }
}
