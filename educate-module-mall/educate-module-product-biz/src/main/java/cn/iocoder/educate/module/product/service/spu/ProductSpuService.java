package cn.iocoder.educate.module.product.service.spu;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.product.controller.app.spu.vo.AppProductSpuPageReqVO;
import cn.iocoder.educate.module.product.dal.dataobject.spu.ProductSpuDO;

/**
 * 商品 SPU Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/12/13 22:27
 */
public interface ProductSpuService {

    /**
     * 获得商品 SPU 分页，提供给用户 App 使用
     *
     * @param pageReqVO 分页查询
     * @return 商品 SPU 分页
     */
    PageResult<ProductSpuDO> getSpuPage(AppProductSpuPageReqVO pageReqVO);

}
