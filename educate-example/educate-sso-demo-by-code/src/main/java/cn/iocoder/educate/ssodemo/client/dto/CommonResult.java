package cn.iocoder.educate.ssodemo.client.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 通用返回
 *
 * @Author: j-sentinel
 * @Date: 2023/5/15 13:22
 */
@Data
public class CommonResult<T> implements Serializable {

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 错误提示，用户可阅读
     */
    private String msg;
}
