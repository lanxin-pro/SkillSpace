package cn.iocoder.educate.module.product.service.sku;

import cn.iocoder.educate.module.product.dal.dataobject.sku.ProductSkuDO;

import java.util.List;

/**
 * 商品 SKU Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/12/18 18:24
 */
public interface ProductSkuService {

    /**
     * 获得商品 SKU 集合
     *
     * @param spuId spu 编号
     * @return 商品sku 集合
     */
    List<ProductSkuDO> getSkuListBySpuId(Long spuId);

    /**
     * 获取商品 SKU 信息
     *
     * @param id 编号
     * @return
     */
    ProductSkuDO getSku(Long id);

    /**
     * 获得商品 SKU 列表
     *
     * @param ids 编号
     * @return 商品sku列表
     */
    List<ProductSkuDO> getSkuList(List<Long> ids);
}
