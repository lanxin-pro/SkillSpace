package cn.iocoder.educate.module.trade.service.cart;

import cn.iocoder.educate.module.trade.controller.app.cart.vo.AppCartAddReqVO;
import cn.iocoder.educate.module.trade.controller.app.cart.vo.AppCartListRespVO;
import cn.iocoder.educate.module.trade.controller.app.cart.vo.AppCartUpdateSelectedReqVO;

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

    /**
     * 添加商品到购物车
     *
     * @param loginUserId 用户编号
     * @param addCountReqVO 添加信息
     * @return 购物项的编号
     */
    Long addCart(Long loginUserId, AppCartAddReqVO addCountReqVO);

    /**
     * 查询用户的购物车列表
     *
     * @param loginUserId 用户编号
     * @return 购物车列表
     */
    AppCartListRespVO getCartList(Long loginUserId);

    /**
     * 更新购物车选中状态
     *
     * @param loginUserId 用户编号
     * @param updateSelectedReqVO 更新信息
     */
    void updateCartSelected(Long loginUserId, AppCartUpdateSelectedReqVO updateSelectedReqVO);
}
