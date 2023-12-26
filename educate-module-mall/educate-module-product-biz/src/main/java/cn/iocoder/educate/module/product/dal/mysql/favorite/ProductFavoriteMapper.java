package cn.iocoder.educate.module.product.dal.mysql.favorite;

import cn.iocoder.educate.module.product.dal.dataobject.favorite.ProductFavoriteDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: j-sentinel
 * @Date: 2023/12/26 17:02
 */
@Mapper
public interface ProductFavoriteMapper extends BaseMapper<ProductFavoriteDO> {

    default ProductFavoriteDO selectByUserIdAndSpuId(Long userId, Long spuId) {
        LambdaQueryWrapper<ProductFavoriteDO> productFavoriteDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        productFavoriteDOLambdaQueryWrapper.eq(ProductFavoriteDO::getSpuId, spuId)
                .eq(ProductFavoriteDO::getUserId, userId);
        return this.selectOne(productFavoriteDOLambdaQueryWrapper);
    }

}
