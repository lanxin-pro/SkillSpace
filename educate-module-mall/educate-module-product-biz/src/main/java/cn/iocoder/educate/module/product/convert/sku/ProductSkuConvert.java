package cn.iocoder.educate.module.product.convert.sku;

import cn.iocoder.educate.module.product.api.sku.dto.ProductSkuRespDTO;
import cn.iocoder.educate.module.product.dal.dataobject.sku.ProductSkuDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 商品 SKU Convert
 *
 * @Author: j-sentinel
 * @Date: 2023/12/31 14:00
 */
@Mapper
public interface ProductSkuConvert {

    ProductSkuConvert INSTANCE = Mappers.getMapper(ProductSkuConvert.class);

    ProductSkuRespDTO convert02(ProductSkuDO bean);

    List<ProductSkuRespDTO> convertList04(List<ProductSkuDO> list);

}
