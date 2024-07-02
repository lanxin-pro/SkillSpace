package cn.iocoder.educate.module.trade.service.cart;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.module.product.api.sku.ProductSkuApi;
import cn.iocoder.educate.module.product.api.sku.dto.ProductSkuRespDTO;
import cn.iocoder.educate.module.product.api.spu.ProductSpuApi;
import cn.iocoder.educate.module.product.api.spu.dto.ProductSpuRespDTO;
import cn.iocoder.educate.module.product.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.trade.controller.app.cart.vo.AppCartAddReqVO;
import cn.iocoder.educate.module.trade.controller.app.cart.vo.AppCartListRespVO;
import cn.iocoder.educate.module.trade.controller.app.cart.vo.AppCartUpdateSelectedReqVO;
import cn.iocoder.educate.module.trade.convert.cart.TradeCartConvert;
import cn.iocoder.educate.module.trade.dal.dataobject.cart.CartDO;
import cn.iocoder.educate.module.trade.dal.mysql.cart.CartMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

/**
 * 购物车 Service 实现类
 *
 * // TODO j-sentinel：未来优化：购物车的价格计算，支持营销信息；目前不支持的原因，前端界面需要前端 pr 支持下；例如说：会员价格；
 *
 * @Author: j-sentinel
 * @Date: 2023/12/25 22:34
 */
@Service
@Validated
public class CartServiceImpl implements CartService {

    @Resource
    private CartMapper cartMapper;

    @Resource
    private ProductSpuApi productSpuApi;

    @Resource
    private ProductSkuApi productSkuApi;

    @Override
    public Integer getCartCount(Long userId) {
        return cartMapper.selectSumByUserId(userId);
    }

    @Override
    public Long addCart(Long loginUserId, AppCartAddReqVO addCountReqVO) {
        // 查询 TradeCartDO
        CartDO cart = cartMapper.selectByUserIdAndSkuId(loginUserId, addCountReqVO.getSkuId());
        // 校验 SKU
        Integer count = addCountReqVO.getCount();
        ProductSkuRespDTO sku = checkProductSku(addCountReqVO.getSkuId(), count);

        // 情况一：存在，则进行数量更新
        if (cart != null) {
            cartMapper.updateById(new CartDO().setId(cart.getId()).setSelected(true)
                    .setCount(cart.getCount() + count));
            return cart.getId();
        // 情况二：不存在，则进行插入
        } else {
            cart = new CartDO().setUserId(loginUserId).setSelected(true)
                    .setSpuId(sku.getSpuId()).setSkuId(sku.getId()).setCount(count);
            cartMapper.insert(cart);
        }
        return cart.getId();
    }

    @Override
    public AppCartListRespVO getCartList(Long loginUserId) {
        // 获得购物车的商品
        List<CartDO> carts = cartMapper.selectListByUserId(loginUserId);
        carts.sort(Comparator.comparing(CartDO::getId).reversed());
        // 如果未空，则返回空结果
        if (CollUtil.isEmpty(carts)) {
            return new AppCartListRespVO().setValidList(emptyList())
                    .setInvalidList(emptyList());
        }

        // 查询 SPU、SKU 列表
        List<ProductSpuRespDTO> spus = productSpuApi.getSpuList(
                carts.stream()
                        .map(CartDO::getSpuId)
                        .collect(Collectors.toList())
        );
        List<ProductSkuRespDTO> skus = productSkuApi.getSkuList(
                carts.stream()
                        .map(CartDO::getSkuId)
                        .collect(Collectors.toList())
        );

        // 如果 SPU 被删除，则删除购物车对应的商品。延迟删除
        // 为什么不是 SKU 被删除呢？因为 SKU 被删除时，还可以通过 SPU 选择其它 SKU
        // deleteCartIfSpuDeleted(carts, spus);

        // 拼接数据
        return TradeCartConvert.INSTANCE.convertList(carts, spus, skus);
    }

    @Override
    public void updateCartSelected(Long loginUserId, AppCartUpdateSelectedReqVO updateSelectedReqVO) {
        cartMapper.updateByIds(updateSelectedReqVO.getIds(), loginUserId,
                new CartDO().setSelected(updateSelectedReqVO.getSelected()));
    }

    /**
     * 校验商品 SKU 是否合法
     * 1. 是否存在
     * 2. TODO j-sentinel 是否下架
     * 3. 库存不足
     *
     * @param skuId 商品 SKU 编号
     * @param count 商品数量
     * @return 商品 SKU
     */
    private ProductSkuRespDTO checkProductSku(Long skuId, Integer count) {
        ProductSkuRespDTO sku = productSkuApi.getSku(skuId);
        if (sku == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SKU_NOT_EXISTS);
        }
        if (count > sku.getStock()) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SKU_STOCK_NOT_ENOUGH);
        }
        return sku;
    }

}
