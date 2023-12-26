package cn.iocoder.educate.module.trade.service.cart;

/**
 * 购物车 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/12/25 22:34
 */
public interface CartService {

    /**
     * 查询用户在购物车中的商品数量
     *
     * @param userId 用户编号
     * @return 商品数量
     */
    Integer getCartCount(Long userId);

}
