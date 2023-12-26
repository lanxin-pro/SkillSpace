package cn.iocoder.educate.module.product.convert.favorite;

import cn.iocoder.educate.module.product.dal.dataobject.favorite.ProductFavoriteDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Author: j-sentinel
 * @Date: 2023/12/26 17:16
 */
@Mapper
public interface ProductFavoriteConvert {

    ProductFavoriteConvert INSTANCE = Mappers.getMapper(ProductFavoriteConvert.class);

    ProductFavoriteDO convert(Long userId, Long spuId);

}
