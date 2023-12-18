package cn.iocoder.educate.module.product.dal.mysql.sku;

import cn.iocoder.educate.module.product.dal.dataobject.sku.ProductSkuDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/12/18 18:25
 */
@Mapper
public interface ProductSkuMapper extends BaseMapper<ProductSkuDO> {

    default List<ProductSkuDO> selectListBySpuId(Long spuId) {
        LambdaQueryWrapper<ProductSkuDO> productSkuDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        productSkuDOLambdaQueryWrapper.eq(ProductSkuDO::getSpuId, spuId);
        return this.selectList(productSkuDOLambdaQueryWrapper);
    }

}
