package cn.iocoder.educate.module.course.service.category;

import cn.iocoder.educate.module.course.controller.admin.category.bo.CategoryBo;
import cn.iocoder.educate.module.course.dal.dataobject.category.CategoryDO;

import java.util.List;

/**
 * @author j-sentinel
 * @date 2024/4/12 14:12
 */
public interface CategoryService {

    List<CategoryBo> findCategoryTree(String keyword);

    /**
     * 保存和修改文章分类管理
     * @param category
     * @return
     */
    CategoryBo saveUpdateCategory(CategoryDO category);

    /**
     * 根据文章分类管理id删除文章分类管理
     * @param id
     * @return
     */
    int deleteCategoryById(Long id);

    /**
     * 根据文章分类管理ids批量删除文章分类管理
     * @param batchIds
     * @return
     */
    boolean deleteBatchCategory(String batchIds);

}

