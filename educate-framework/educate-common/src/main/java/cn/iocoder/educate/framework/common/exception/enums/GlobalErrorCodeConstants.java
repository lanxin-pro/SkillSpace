package cn.iocoder.educate.framework.common.exception.enums;

import cn.iocoder.educate.framework.common.exception.ErrorCode;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/7 14:51
 */
public interface GlobalErrorCodeConstants {

    ErrorCode SUCCESS = new ErrorCode(0, "成功");



    // ========== 服务端错误段 ==========

    ErrorCode INTERNAL_SERVER_ERROR = new ErrorCode(500, "系统异常");
    ErrorCode NOT_IMPLEMENTED = new ErrorCode(501, "功能未实现/未开启");
}
