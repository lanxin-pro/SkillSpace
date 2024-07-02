package cn.iocoder.educate.module.product.api.sku;

import cn.iocoder.educate.module.product.api.sku.dto.ProductSkuRespDTO;
import cn.iocoder.educate.module.product.convert.sku.ProductSkuConvert;
import cn.iocoder.educate.module.product.dal.dataobject.sku.ProductSkuDO;
import cn.iocoder.educate.module.product.service.sku.ProductSkuService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品 SKU API 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/12/31 13:59
 */
@Service
@Validated
public class ProductSkuApiImpl implements ProductSkuApi {

    @Resource
    private ProductSkuService productSkuService;

    @Override
    public ProductSkuRespDTO getSku(Long id) {
        ProductSkuDO sku = productSkuService.getSku(id);
        return ProductSkuConvert.INSTANCE.convert02(sku);
    }

    @Override
    public List<ProductSkuRespDTO> getSkuList(List<Long> ids) {
        List<ProductSkuDO> skus = productSkuService.getSkuList(ids);
        return ProductSkuConvert.INSTANCE.convertList04(skus);
    }

}
