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
 * API 异常数据的处理状态
 */
export const InfraApiErrorLogProcessStatusEnum = {
    // 未处理
    INIT: 0,
    // 已处理
    DONE: 1,
    // 已忽略
    IGNORE: 2
}

/**
 * 任务状态的枚举
 */
export const InfraJobStatusEnum = {
    // 初始化中
    INIT: 0,
    // 运行中
    NORMAL: 1,
    // 暂停运行
    STOP: 2
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

/**
 * 消息类型（Follow: 关注时回复；Message: 消息回复；Keyword: 关键词回复）
 * 作为 tab.name，enum 的数字不能随意修改，与 api 参数相关
 *
 * @type {{Follow: number, Message: number, Keyword: number}}
 */
export const MsgType = {
    Follow: 1,
    Message: 2,
    Keyword: 3
}

export const ReplyType = {
    News: 'news',
    Image: 'image',
    Voice: 'voice',
    Video: 'video',
    Music: 'music',
    Text: 'text'
}

export const NewsType = {
    Published: '1',
    Draft: '2'
}

// tip全部的类型
export const UploadType = {
    Image: 'image',
    Voice: 'voice',
    Video: 'video'
}
