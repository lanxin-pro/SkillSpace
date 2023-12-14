package cn.iocoder.educate.module.product.dal.mysql.category;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.educate.module.product.controller.admin.category.vo.ProductCategoryListReqVO;
import cn.iocoder.educate.module.product.dal.dataobject.category.ProductCategoryDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 商品分类 Mapper
 *
 * @Author: j-sentinel
 * @Date: 2023/12/14 22:02
 */
@Mapper
public interface ProductCategoryMapper extends BaseMapper<ProductCategoryDO> {

    default List<ProductCategoryDO> selectList(ProductCategoryListReqVO productCategoryListReqVO) {
        LambdaQueryWrapper<ProductCategoryDO> productCategoryDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        productCategoryDOLambdaQueryWrapper.like(StringUtils.hasText(productCategoryListReqVO.getName()),
                ProductCategoryDO::getName, productCategoryListReqVO.getName())
                .eq(ObjectUtil.isNotEmpty(productCategoryListReqVO.getParentId()),
                        ProductCategoryDO::getParentId, productCategoryListReqVO.getParentId())
                .eq(ObjectUtil.isNotEmpty(productCategoryListReqVO.getStatus()),
                        ProductCategoryDO::getStatus, productCategoryListReqVO.getStatus())
                .orderByDesc(ProductCategoryDO::getId);
        return this.selectList(productCategoryDOLambdaQueryWrapper);
    }

}
