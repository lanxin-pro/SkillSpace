package cn.iocoder.educate.module.product.controller.app.spu;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.product.controller.app.spu.vo.AppProductSpuPageReqVO;
import cn.iocoder.educate.module.product.controller.app.spu.vo.AppProductSpuPageRespVO;
import cn.iocoder.educate.module.product.convert.spu.ProductSpuConvert;
import cn.iocoder.educate.module.product.dal.dataobject.spu.ProductSpuDO;
import cn.iocoder.educate.module.product.service.spu.ProductSpuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;

/**
 * @Author: j-sentinel
 * @Date: 2023/12/13 22:22
 */
@Tag(name = "用户 APP - 商品 SPU")
@RestController
@RequestMapping("/product/spu")
@Validated
public class AppProductSpuController {

    @Resource
    private ProductSpuService productSpuService;

    @GetMapping("/page")
    @Operation(summary = "获得商品 SPU 分页")
    @PermitAll
    public CommonResult<PageResult<AppProductSpuPageRespVO>> getSpuPage(@Valid AppProductSpuPageReqVO pageVO) {
        PageResult<ProductSpuDO> pageResult = productSpuService.getSpuPage(pageVO);
        // 拼接返回
        PageResult<AppProductSpuPageRespVO> voPageResult = ProductSpuConvert.INSTANCE.convertPageForGetSpuPage(pageResult);
        return CommonResult.success(voPageResult);
    }

}
