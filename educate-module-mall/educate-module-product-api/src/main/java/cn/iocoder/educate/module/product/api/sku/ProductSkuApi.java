package cn.iocoder.educate.module.product.api.sku;

import cn.iocoder.educate.module.product.api.sku.dto.ProductSkuRespDTO;
import java.util.List;

/**
 * 商品 SKU API 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/12/31 13:58
 */
public interface ProductSkuApi {

    /**
     * 查询 SKU 信息
     *
     * @param id SKU 编号
     * @return SKU 信息
     */
    ProductSkuRespDTO getSku(Long id);

    /**
     * 批量查询 SKU 数组
     *
     * @param ids SKU 编号列表
     * @return SKU 数组
     */
    List<ProductSkuRespDTO> getSkuList(List<Long> ids);

}
