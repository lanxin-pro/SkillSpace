package cn.iocoder.educate.module.system.api.dict.dto;

import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import lombok.Data;

/**
 * 字典数据 Response DTO
 *
 * @author j-sentinel
 * @date 2024/1/26 10:10
 */
@Data
public class DictDataRespDTO {

    /**
     * 字典标签
     */
    private String label;

    /**
     * 字典值
     */
    private String value;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;

}
