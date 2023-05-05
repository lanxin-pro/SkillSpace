package cn.iocoder.educate.framework.common.pojo;

import lombok.Data;
import java.io.Serializable;

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


}
