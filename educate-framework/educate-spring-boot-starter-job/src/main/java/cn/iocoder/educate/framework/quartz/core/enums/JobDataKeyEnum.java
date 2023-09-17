package cn.iocoder.educate.framework.quartz.core.enums;

/**
 * Quartz Job Data 的 key 枚举
 *
 * @Author: j-sentinel
 * @Date: 2023/9/17 16:23
 */
public enum JobDataKeyEnum {

    JOB_ID,
    JOB_HANDLER_NAME,
    JOB_HANDLER_PARAM,
    // 最大重试次数
    JOB_RETRY_COUNT,
    // 每次重试间隔
    JOB_RETRY_INTERVAL,

}
