package cn.iocoder.educate.module.product.service.category;

import cn.iocoder.educate.module.product.controller.admin.category.vo.ProductCategoryListReqVO;
import cn.iocoder.educate.module.product.dal.dataobject.category.ProductCategoryDO;
import cn.iocoder.educate.module.product.dal.mysql.category.ProductCategoryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/12/14 21:51
 */
@Slf4j
@Service
@Validated
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Resource
    private ProductCategoryMapper productCategoryMapper;

    @Override
    public List<ProductCategoryDO> getEnableCategoryList(ProductCategoryListReqVO listReqVO) {
        return productCategoryMapper.selectList(listReqVO);
    }
}
