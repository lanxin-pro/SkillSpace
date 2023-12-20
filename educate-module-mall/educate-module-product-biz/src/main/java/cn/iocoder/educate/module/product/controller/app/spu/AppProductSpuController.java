package cn.iocoder.educate.module.product.controller.app.spu;

import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.product.controller.app.spu.vo.AppProductSpuDetailRespVO;
import cn.iocoder.educate.module.product.controller.app.spu.vo.AppProductSpuPageReqVO;
import cn.iocoder.educate.module.product.controller.app.spu.vo.AppProductSpuPageRespVO;
import cn.iocoder.educate.module.product.convert.spu.ProductSpuConvert;
import cn.iocoder.educate.module.product.dal.dataobject.sku.ProductSkuDO;
import cn.iocoder.educate.module.product.dal.dataobject.spu.ProductSpuDO;
import cn.iocoder.educate.module.product.service.sku.ProductSkuService;
import cn.iocoder.educate.module.product.service.spu.ProductSpuService;
import cn.iocoder.yudao.module.product.enums.ErrorCodeConstants;
import cn.iocoder.yudao.module.product.enums.spu.ProductSpuStatusEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.List;

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

    @Resource
    private ProductSkuService productSkuService;

    @GetMapping("/page")
    @Operation(summary = "获得商品 SPU 分页")
    @PermitAll
    public CommonResult<PageResult<AppProductSpuPageRespVO>> getSpuPage(@Valid AppProductSpuPageReqVO pageVO) {
        PageResult<ProductSpuDO> pageResult = productSpuService.getSpuPage(pageVO);
        // 拼接返回
        PageResult<AppProductSpuPageRespVO> voPageResult = ProductSpuConvert.INSTANCE.convertPageForGetSpuPage(pageResult);
        return CommonResult.success(voPageResult);
    }

    /**
     * TODO j-sentinel 严重BUG ，这个SQL必须优化，巨卡
     * @param id
     * @return
     */
    @GetMapping("/get-detail")
    @Operation(summary = "获得商品 SPU 明细")
    @Parameter(name = "id", description = "编号", required = true)
    @PermitAll
    public CommonResult<AppProductSpuDetailRespVO> getSpuDetail(@RequestParam("id") Long id) {
        // 获得商品 SPU
        ProductSpuDO spu = productSpuService.getSpu(id);
        if (spu == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SPU_NOT_EXISTS);
        }
        if (!ProductSpuStatusEnum.isEnable(spu.getStatus())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SPU_NOT_ENABLE);
        }

        // TODO j-sentinel 这里的代码有待考察意思，我先加上去预防报错
        // SKU 拼接返回
        List<ProductSkuDO> skus = productSkuService.getSkuListBySpuId(spu.getId());
        AppProductSpuDetailRespVO detailVO = ProductSpuConvert.INSTANCE.convertForGetSpuDetail(spu, skus);
        // TODO j-sentinel 这里需要处理 vip 价格

        return CommonResult.success(detailVO);
    }


}
