package cn.iocoder.educate.module.product.api.spu;

import cn.iocoder.educate.module.product.api.spu.dto.ProductSpuRespDTO;
import cn.iocoder.educate.module.product.convert.spu.ProductSpuConvert;
import cn.iocoder.educate.module.product.dal.dataobject.spu.ProductSpuDO;
import cn.iocoder.educate.module.product.service.spu.ProductSpuService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 商品 SPU API 接口实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/12/31 16:01
 */
@Service
@Validated
public class ProductSpuApiImpl implements ProductSpuApi {

    @Resource
    private ProductSpuService productSpuService;

    @Override
    public List<ProductSpuRespDTO> getSpuList(Collection<Long> ids) {
        List<ProductSpuDO> productSpuDOList = productSpuService.getSpuList(ids);
        return ProductSpuConvert.INSTANCE.convertList2(productSpuDOList);
    }
}
