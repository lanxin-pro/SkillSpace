package cn.iocoder.educate.module.product.api.property.dto;

import lombok.Data;

/**
 * 商品属性项的明细 Response DTO
 *
 * @Author: j-sentinel
 * @Date: 2023/12/31 13:51
 */
@Data
public class ProductPropertyValueDetailRespDTO {

    /**
     * 属性的编号
     */
    private Long propertyId;

    /**
     * 属性的名称
     */
    private String propertyName;

    /**
     * 属性值的编号
     */
    private Long valueId;

    /**
     * 属性值的名称
     */
    private String valueName;

}
