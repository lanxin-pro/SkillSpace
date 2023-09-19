package cn.iocoder.educate.module.infra.enums.job;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务日志的状态枚举
 *
 * @Author: j-sentinel
 * @Date: 2023/9/19 14:59
 */
@Getter
@AllArgsConstructor
public enum JobLogStatusEnum {

    // 运行中
    RUNNING(0),

    // 成功
    SUCCESS(1),

    // 失败
    FAILURE(2);

    /**
     * 状态
     */
    private final Integer status;

}
