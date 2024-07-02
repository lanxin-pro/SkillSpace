package cn.iocoder.educate.module.product.controller.app.favorite;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.educate.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.educate.module.product.controller.app.favorite.vo.AppFavoriteReqVO;
import cn.iocoder.educate.module.product.dal.dataobject.favorite.ProductFavoriteDO;
import cn.iocoder.educate.module.product.service.favorite.ProductFavoriteService;
import cn.iocoder.educate.module.product.service.spu.ProductSpuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author: j-sentinel
 * @Date: 2023/12/26 17:01
 */
@Tag(name = "用户 APP - 商品收藏")
@RestController
@RequestMapping("/product/favorite")
public class AppFavoriteController {

    @Resource
    private ProductFavoriteService productFavoriteService;

    @Resource
    private ProductSpuService productSpuService;

    @PostMapping(value = "/create")
    @Operation(summary = "添加商品收藏")
    @PreAuthenticated
    public CommonResult<Long> createFavorite(@RequestBody @Valid AppFavoriteReqVO appFavoriteReqVO) {
        return CommonResult.success(productFavoriteService.createFavorite(SecurityFrameworkUtils.getLoginUserId(),
                appFavoriteReqVO.getSpuId()));
    }

    @DeleteMapping(value = "/delete")
    @Operation(summary = "取消单个商品收藏")
    @PreAuthenticated
    public CommonResult<Boolean> deleteFavorite(@RequestBody @Valid AppFavoriteReqVO appFavoriteReqVO) {
        productFavoriteService.deleteFavorite(SecurityFrameworkUtils.getLoginUserId(), appFavoriteReqVO.getSpuId());
        return CommonResult.success(Boolean.TRUE);
    }

    @GetMapping(value = "/exits")
    @Operation(summary = "检查是否收藏过商品")
    @PreAuthenticated
    public CommonResult<Boolean> isFavoriteExists(AppFavoriteReqVO appFavoriteReqVO) {
        ProductFavoriteDO favorite = productFavoriteService.getFavorite(SecurityFrameworkUtils.getLoginUserId(),
                appFavoriteReqVO.getSpuId());
        return CommonResult.success(favorite != null);
    }

}
