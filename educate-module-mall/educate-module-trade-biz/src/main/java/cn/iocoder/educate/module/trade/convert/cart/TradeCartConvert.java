package cn.iocoder.educate.module.trade.convert.cart;

import cn.iocoder.educate.module.product.api.sku.dto.ProductSkuRespDTO;
import cn.iocoder.educate.module.product.api.spu.dto.ProductSpuRespDTO;
import cn.iocoder.educate.module.product.enums.spu.ProductSpuStatusEnum;
import cn.iocoder.educate.module.trade.controller.app.base.sku.AppProductSkuBaseRespVO;
import cn.iocoder.educate.module.trade.controller.app.base.spu.AppProductSpuBaseRespVO;
import cn.iocoder.educate.module.trade.controller.app.cart.vo.AppCartListRespVO;
import cn.iocoder.educate.module.trade.dal.dataobject.cart.CartDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: j-sentinel
 * @Date: 2023/12/31 15:40
*/
@Mapper
public interface TradeCartConvert {

    TradeCartConvert INSTANCE = Mappers.getMapper(TradeCartConvert.class);

    default AppCartListRespVO convertList(List<CartDO> carts,
                                          List<ProductSpuRespDTO> spus, List<ProductSkuRespDTO> skus) {
        Map<Long, ProductSpuRespDTO> spuMap = spus.stream().collect(Collectors.toMap(ProductSpuRespDTO::getId,
                Function.identity(), (v1, v2) -> v1));
        Map<Long, ProductSkuRespDTO> skuMap = skus.stream().collect(Collectors.toMap(ProductSkuRespDTO::getId,
                Function.identity(), (v1, v2) -> v1));
        List<AppCartListRespVO.Cart> validList = new ArrayList<>(carts.size());
        List<AppCartListRespVO.Cart> invalidList = new ArrayList<>();
        // 遍历，开始转换
        carts.forEach(cart -> {
            AppCartListRespVO.Cart cartVO = new AppCartListRespVO.Cart();
            cartVO.setId(cart.getId()).setCount(cart.getCount()).setSelected(cart.getSelected());
            ProductSpuRespDTO spu = spuMap.get(cart.getSpuId());
            ProductSkuRespDTO sku = skuMap.get(cart.getSkuId());
            cartVO.setSpu(convert(spu)).setSku(convert(sku));

            // 如果 SPU 不存在，或者下架，或者库存不足，说明是无效的
            if (spu == null
                    || !ProductSpuStatusEnum.isEnable(spu.getStatus())
                    || spu.getStock() <= 0) {
                // 强制设置成不可选中
                cartVO.setSelected(false);
                invalidList.add(cartVO);
            } else {
                // 虽然 SKU 可能也会不存在，但是可以通过购物车重新选择
                validList.add(cartVO);
            }
        });
        return new AppCartListRespVO().setValidList(validList).setInvalidList(invalidList);
    }
    AppProductSpuBaseRespVO convert(ProductSpuRespDTO spu);
    AppProductSkuBaseRespVO convert(ProductSkuRespDTO sku);

}
