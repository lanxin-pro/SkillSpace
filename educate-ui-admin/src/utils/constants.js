/**
 * 枚举类
 */

// ========== 静态变量 ==========

/**
 * 用户的社交平台的类型枚举
 */
export const SystemUserSocialTypeEnum = {
    DINGTALK: {
        title: "钉钉",
        type: 20,
        source: "dingtalk",
        img: "https://s1.ax1x.com/2022/05/22/OzMDRs.png",
    },
    WECHAT_ENTERPRISE: {
        title: "企业微信",
        type: 30,
        source: "wechat_enterprise",
        img: "https://s1.ax1x.com/2022/05/22/OzMrzn.png",
    }
}

/**
 * 菜单的类型枚举
 */
export const SystemMenuTypeEnum = {
    DIR: 1, // 目录
    MENU: 2, // 菜单
    BUTTON: 3 // 按钮
}

/**
 * 全局通用状态枚举
 */
export const CommonStatusEnum = {
    ENABLE: 0, // 开启
    DISABLE: 1 // 禁用
}
