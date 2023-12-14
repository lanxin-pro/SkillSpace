package cn.iocoder.educate.module.product.dal.mysql.spu;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.product.controller.app.spu.vo.AppProductSpuPageReqVO;
import cn.iocoder.educate.module.product.dal.dataobject.spu.ProductSpuDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * @Author: j-sentinel
 * @Date: 2023/12/13 22:28
 */
@Mapper
public interface ProductSpuMapper extends BaseMapper<ProductSpuDO>  {

    /**
     * 获得商品 SPU 分页，提供给用户 App 使用
     *
     * @param pageReqVO
     * @param categoryIds
     * @return
     */
    default PageResult<ProductSpuDO> selectPage(AppProductSpuPageReqVO pageReqVO, Set<Long> categoryIds) {
        LambdaQueryWrapper<ProductSpuDO> productSpuDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 关键字匹配，目前只匹配商品名
        productSpuDOLambdaQueryWrapper.like(StringUtils.hasText(pageReqVO.getKeyword()),
                ProductSpuDO::getName, pageReqVO.getKeyword())
        // 分类
                .in(ObjectUtil.isAllNotEmpty(categoryIds) && CollectionUtil.isNotEmpty(categoryIds),
                        ProductSpuDO::getCategoryId, categoryIds);
        Page<ProductSpuDO> page = new Page<>(pageReqVO.getPageNo(), pageReqVO.getPageSize());
        Page<ProductSpuDO> productSpuDOPage = this.selectPage(page, productSpuDOLambdaQueryWrapper);
        return new PageResult<>(productSpuDOPage.getRecords(), productSpuDOPage.getTotal());
    }

}
