package cn.iocoder.educate.module.product.convert.spu;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.product.controller.app.spu.vo.AppProductSpuPageRespVO;
import cn.iocoder.educate.module.product.dal.dataobject.spu.ProductSpuDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 商品 SPU Convert
 *
 * @Author: j-sentinel
 * @Date: 2023/12/13 22:21
 */
@Mapper
public interface ProductSpuConvert {

    ProductSpuConvert INSTANCE = Mappers.getMapper(ProductSpuConvert.class);

    // ========== 用户 App 相关 ==========

    PageResult<AppProductSpuPageRespVO> convertPageForGetSpuPage(PageResult<ProductSpuDO> page);

}
