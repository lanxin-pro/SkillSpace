package cn.iocoder.educate.module.product.api.spu;

import cn.iocoder.educate.module.product.api.spu.dto.ProductSpuRespDTO;

import java.util.Collection;
import java.util.List;

/**
 * 商品 SPU API 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/12/31 15:38
 */
public interface ProductSpuApi {

    /**
     * 批量查询 SPU 数组
     *
     * @param ids SPU 编号列表
     * @return SPU 数组
     */
    List<ProductSpuRespDTO> getSpuList(Collection<Long> ids);
}
