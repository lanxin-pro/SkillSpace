package cn.iocoder.educate.module.product.service.category;

import cn.iocoder.educate.module.product.controller.admin.category.vo.ProductCategoryListReqVO;
import cn.iocoder.educate.module.product.dal.dataobject.category.ProductCategoryDO;

import java.util.List;

/**
 * 商品分类 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/12/14 21:50
 */
public interface ProductCategoryService {

    /**
     * 获得商品分类列表
     *
     * @param listReqVO 查询条件
     * @return 商品分类列表
     */
    List<ProductCategoryDO> getEnableCategoryList(ProductCategoryListReqVO listReqVO);

}
