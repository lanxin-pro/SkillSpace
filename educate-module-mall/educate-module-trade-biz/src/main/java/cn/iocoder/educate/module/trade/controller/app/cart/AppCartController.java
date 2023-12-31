package cn.iocoder.educate.module.trade.controller.app.cart;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.security.core.annotations.PreAuthenticated;
import cn.iocoder.educate.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.educate.module.trade.controller.app.cart.vo.AppCartAddReqVO;
import cn.iocoder.educate.module.trade.controller.app.cart.vo.AppCartListRespVO;
import cn.iocoder.educate.module.trade.service.cart.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author: j-sentinel
 * @Date: 2023/12/25 22:29
 */
@Tag(name = "用户 App - 购物车")
@RestController
@RequestMapping("/trade/cart")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AppCartController {

    @Resource
    private CartService cartService;

    /**
     * {@link cn.iocoder.educate.framework.security.core.annotations.PreAuthenticated} 声明用户需要登录
     *
     * @return 购物车中商品的数量
     */
    @GetMapping("get-count")
    @Operation(summary = "查询用户在购物车中的商品数量")
    @PreAuthenticated
    public CommonResult<Integer> getCartCount() {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        return CommonResult.success(cartService.getCartCount(loginUserId));
    }

    @PostMapping("/add")
    @Operation(summary = "添加购物车商品")
    @PreAuthenticated
    public CommonResult<Long> addCart(@Valid @RequestBody AppCartAddReqVO addCountReqVO) {
        return CommonResult.success(cartService.addCart(SecurityFrameworkUtils.getLoginUserId(), addCountReqVO));
    }

    @GetMapping("/list")
    @Operation(summary = "查询用户的购物车列表")
    @PreAuthenticated
    public CommonResult<AppCartListRespVO> getCartList() {
        return CommonResult.success(cartService.getCartList(SecurityFrameworkUtils.getLoginUserId()));
    }

}
