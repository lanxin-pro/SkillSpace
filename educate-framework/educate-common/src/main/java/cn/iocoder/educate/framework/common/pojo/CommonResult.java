package cn.iocoder.educate.framework.common.pojo;

import cn.iocoder.educate.framework.common.exception.ErrorCode;
import cn.iocoder.educate.framework.common.exception.ServerException;
import cn.iocoder.educate.framework.common.exception.ServiceException;
import cn.iocoder.educate.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.util.Assert;
import java.io.Serializable;
import java.util.Objects;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/5 8:15
 * 通用返回
 * @param <T> 数据泛型
 */
@Data
public class CommonResult<T> implements Serializable {

    /**
     * 错误码
     *
     * @see ErrorCode#getCode()
     */
    private Integer code;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 错误提示，用户可阅读
     *
     * @see ErrorCode#getMsg() ()
     */
    private String msg;

    public static <T> CommonResult<T> error(ErrorCode errorCode) {
        return error(errorCode.getCode(), errorCode.getMsg());
    }

    public static <T> CommonResult<T> error(Integer code, String message) {
        Assert.isTrue(!GlobalErrorCodeConstants.SUCCESS.getCode().equals(code), "code 必须是错误的！");
        CommonResult<T> result = new CommonResult<>();
        result.code = code;
        result.msg = message;
        return result;
    }

    public static <T> CommonResult<T> success(T data) {
        CommonResult<T> result = new CommonResult<>();
        result.code = GlobalErrorCodeConstants.SUCCESS.getCode();
        result.data = data;
        result.msg = "";
        return result;
    }

    // ========= 和 Exception 异常体系集成 =========

    /**
     * 判断是否有异常。如果有，则抛出 {@link ServiceException} 异常
     */
    public void checkError() throws ServiceException {
        if (isSuccess()) {
            return;
        }
        // 服务端异常
        if (GlobalErrorCodeConstants.isServerErrorCode(code)) {
            throw new ServerException(code, msg);
        }
        // 业务异常
        throw new ServiceException(code, msg);
    }

    @JsonIgnore // 避免 jackson 序列化
    public boolean isSuccess() {
        return isSuccess(code);
    }

    public static boolean isSuccess(Integer code) {
        return Objects.equals(code, GlobalErrorCodeConstants.SUCCESS.getCode());
    }

}
