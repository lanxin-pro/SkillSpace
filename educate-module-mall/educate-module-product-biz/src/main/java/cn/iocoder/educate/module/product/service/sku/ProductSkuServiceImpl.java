package cn.iocoder.educate.module.product.service.sku;

import cn.iocoder.educate.module.product.dal.dataobject.sku.ProductSkuDO;
import cn.iocoder.educate.module.product.dal.mysql.sku.ProductSkuMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品 SKU Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/12/18 18:25
 */
@Slf4j
@Service
@Validated
public class ProductSkuServiceImpl implements ProductSkuService {

    @Resource
    private ProductSkuMapper productSkuMapper;

    @Override
    public List<ProductSkuDO> getSkuListBySpuId(Long spuId) {
        return productSkuMapper.selectListBySpuId(spuId);
    }
}
