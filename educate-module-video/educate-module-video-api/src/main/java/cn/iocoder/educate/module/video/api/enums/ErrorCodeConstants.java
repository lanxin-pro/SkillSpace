package cn.iocoder.educate.module.video.api.enums;

import cn.iocoder.educate.framework.common.exception.ErrorCode;

/**
 * @Author: 董伟豪
 * @Date: 2023/5/8 20:53
 * System 错误码枚举类
 * system 系统，使用 1-011-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== 视频Adimin 模块 1002000000 ==========
    ErrorCode VIDEO_DURATION_ERROR = new ErrorCode(1011000001, "视频时长数值错误");

}
