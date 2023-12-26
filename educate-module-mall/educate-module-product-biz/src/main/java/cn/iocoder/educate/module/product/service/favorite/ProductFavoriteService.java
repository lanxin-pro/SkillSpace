package cn.iocoder.educate.module.product.service.favorite;

import cn.iocoder.educate.module.product.dal.dataobject.favorite.ProductFavoriteDO;

/**
 * 商品收藏 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/12/26 17:01
 */
public interface ProductFavoriteService {

    /**
     * 创建商品收藏
     *
     * @param userId 用户编号
     * @param spuId SPU 编号
     */
    Long createFavorite(Long userId, Long spuId);

    /**
     * 取消商品收藏
     *
     * @param userId 用户编号
     * @param spuId SPU 编号
     */
    void deleteFavorite(Long userId, Long spuId);

    /**
     * 获取收藏过商品
     *
     * @param userId 用户编号
     * @param spuId SPU 编号
     */
    ProductFavoriteDO getFavorite(Long userId, Long spuId);

}
