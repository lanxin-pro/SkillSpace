package cn.iocoder.educate.module.system.enums;

import cn.iocoder.educate.framework.common.exception.ErrorCode;

/**
 * Course 错误码枚举类
 * course 系统，使用 1-008-000-000 段
 *
 * @author j-sentinel
 * @date 2024/1/30 14:23
 */
public interface ErrorCodeConstants {

    // ========== Course 章 模块 1008000000 ==========
    ErrorCode COURSE_CHAPTER_NOT_EXISTS = new ErrorCode(1008000001, "课程的章不存在");

}
