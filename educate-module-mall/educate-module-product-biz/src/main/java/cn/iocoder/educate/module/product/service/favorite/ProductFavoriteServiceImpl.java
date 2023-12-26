package cn.iocoder.educate.module.product.service.favorite;

import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.module.product.convert.favorite.ProductFavoriteConvert;
import cn.iocoder.educate.module.product.dal.dataobject.favorite.ProductFavoriteDO;
import cn.iocoder.educate.module.product.dal.mysql.favorite.ProductFavoriteMapper;
import cn.iocoder.educate.module.product.enums.ErrorCodeConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * @Author: j-sentinel
 * @Date: 2023/12/26 17:02
 */
@Slf4j
@Service
@Validated
public class ProductFavoriteServiceImpl implements ProductFavoriteService {

    @Resource
    private ProductFavoriteMapper productFavoriteMapper;

    @Override
    public Long createFavorite(Long userId, Long spuId) {
        ProductFavoriteDO favorite = productFavoriteMapper.selectByUserIdAndSpuId(userId, spuId);
        if (favorite != null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.FAVORITE_EXISTS);
        }
        ProductFavoriteDO entity = ProductFavoriteConvert.INSTANCE.convert(userId, spuId);
        productFavoriteMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public void deleteFavorite(Long userId, Long spuId) {
        ProductFavoriteDO favorite = productFavoriteMapper.selectByUserIdAndSpuId(userId, spuId);
        if (favorite == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.FAVORITE_NOT_EXISTS);
        }
        productFavoriteMapper.deleteById(favorite.getId());
    }

    @Override
    public ProductFavoriteDO getFavorite(Long userId, Long spuId) {
        return productFavoriteMapper.selectByUserIdAndSpuId(userId, spuId);
    }

}
