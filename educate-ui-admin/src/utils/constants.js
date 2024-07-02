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
 * 弹窗的标识
 *
 * @type {{}}
 */
export const SystemCreateOrUpdate = {
    // 创建
    CREATE: "创建",
    // 修改
    UPDATE: "修改"
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


// ========== PAY 模块 ==========
/**
 * 支付渠道枚举
 */
export const PayChannelEnum = {
    WX_PUB: {
        code: 'wx_pub',
        name: '微信 JSAPI 支付'
    },
    WX_LITE: {
        code: 'wx_lite',
        name: '微信小程序支付'
    },
    WX_APP: {
        code: 'wx_app',
        name: '微信 APP 支付'
    },
    WX_BAR: {
        code: 'wx_bar',
        name: '微信条码支付'
    },
    ALIPAY_PC: {
        code: 'alipay_pc',
        name: '支付宝 PC 网站支付'
    },
    ALIPAY_WAP: {
        code: 'alipay_wap',
        name: '支付宝 WAP 网站支付'
    },
    ALIPAY_APP: {
        code: 'alipay_app',
        name: '支付宝 APP 支付'
    },
    ALIPAY_QR: {
        code: 'alipay_qr',
        name: '支付宝扫码支付'
    },
    ALIPAY_BAR: {
        code: 'alipay_bar',
        name: '支付宝条码支付'
    },
    WALLET: {
        code: 'wallet',
        name: '钱包支付'
    },
    MOCK: {
        code: 'mock',
        name: '模拟支付'
    }
}
