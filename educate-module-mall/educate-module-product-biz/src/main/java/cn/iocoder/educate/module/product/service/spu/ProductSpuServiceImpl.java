package cn.iocoder.educate.module.product.service.spu;

import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.product.controller.admin.category.vo.ProductCategoryListReqVO;
import cn.iocoder.educate.module.product.controller.app.spu.vo.AppProductSpuPageReqVO;
import cn.iocoder.educate.module.product.dal.dataobject.category.ProductCategoryDO;
import cn.iocoder.educate.module.product.dal.dataobject.spu.ProductSpuDO;
import cn.iocoder.educate.module.product.dal.mysql.spu.ProductSpuMapper;
import cn.iocoder.educate.module.product.service.category.ProductCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: j-sentinel
 * @Date: 2023/12/13 22:27
 */
@Slf4j
@Service
@Validated
public class ProductSpuServiceImpl implements ProductSpuService {

    @Resource
    private ProductCategoryService productCategoryService;

    @Resource
    private ProductSpuMapper productSpuMapper;

    @Override
    public PageResult<ProductSpuDO> getSpuPage(AppProductSpuPageReqVO pageReqVO) {
        // 查找时，如果查找某个分类编号，则包含它的子分类。因为顶级分类不包含商品
        Set<Long> categoryIds = new HashSet<>();
        if (pageReqVO.getCategoryId() != null && pageReqVO.getCategoryId() > 0) {
            categoryIds.add(pageReqVO.getCategoryId());
            List<ProductCategoryDO> categoryChildren = productCategoryService.getEnableCategoryList(
                    new ProductCategoryListReqVO()
                            .setParentId(pageReqVO.getCategoryId())
                            .setStatus(CommonStatusEnum.ENABLE.getStatus()));
            categoryIds.addAll(categoryChildren.stream().map(ProductCategoryDO::getId).collect(Collectors.toList()));
        }
        return productSpuMapper.selectPage(pageReqVO, categoryIds);
    }
}
