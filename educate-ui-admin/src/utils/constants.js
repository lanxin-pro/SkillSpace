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
    // 目录
    DIR: 1,
    // 菜单
    MENU: 2,
    // 按钮
    BUTTON: 3
}

/**
 * 全局通用状态枚举
 */
export const CommonStatusEnum = {
    // 开启
    ENABLE: 0,
    // 禁用
    DISABLE: 1
}

/**
 * 数据权限的范围枚举
 */
export const SystemDataScopeEnum = {
    // 全部数据权限
    ALL: 1,
    // 指定部门数据权限
    DEPT_CUSTOM: 2,
    // 部门数据权限
    DEPT_ONLY: 3,
    // 部门及以下数据权限
    DEPT_AND_CHILD: 4,
    // 仅本人数据权限
    DEPT_SELF: 5
}
