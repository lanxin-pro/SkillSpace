package cn.iocoder.educate.module.course.service.category;

import cn.iocoder.educate.module.course.controller.admin.category.bo.CategoryBo;
import cn.iocoder.educate.module.course.dal.dataobject.category.CategoryDO;
import cn.iocoder.educate.module.course.dal.mysql.category.CategoryMapper;
import cn.iocoder.educate.module.course.service.BaseService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author j-sentinel
 * @date 2024/4/12 14:12
 */
@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService, BaseService {

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public List<CategoryBo> findCategoryTree(String keyword) {
        return categoryMapper.findCategoryTree(keyword);
    }

    @Override
    public CategoryBo saveUpdateCategory(CategoryDO category) {
        boolean flag = categoryMapper.saveOrUpdate(category);
        return flag ? tranferBo(category, CategoryBo.class) : null;
    }

    /**
     * 只能删除两级，递归删除得去 findCategoryTreeByPid()
     * 根据文章分类管理id删除文章分类管理
     * 写了事务的方法一定不能加try{}catch
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteCategoryById(Long id) {
        LambdaQueryWrapper<CategoryDO> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 全部的数据
        List<CategoryDO> allList = categoryMapper.selectList(categoryLambdaQueryWrapper);
        // 需要删除数据
        CategoryDO category = categoryMapper.selectById(id);
        allList.forEach(root-> buckForbackDelete(root,category));
        // 删除本体
        CategoryDO newCategory = new CategoryDO();
        newCategory.setId(id);
        newCategory.setIsdelete(1);
        categoryMapper.updateById(newCategory);
        return 0;
    }

    @Override
    public boolean deleteBatchCategory(String batchIds) {
        String[] split = batchIds.split(",");
        List<CategoryDO> categoryList = Arrays.stream(split).map(idStr -> {
            CategoryDO category = new CategoryDO();
            category.setId(new Long(idStr));
            category.setIsdelete(1);
            return category;
        }).collect(Collectors.toList());
        int i = categoryMapper.deleteBatchIds(categoryList);
        return i > 0;
    }

    /**
     * 只能删除两级
     * @param root 全部数据
     * @param category 删除数据
     */
    private void buckForbackDelete(CategoryDO root, CategoryDO category) {
        // 获取到删除项的所有子集
        boolean flag = root.getPid().equals(category.getId());
        if(flag){
            CategoryDO newCategory = new CategoryDO();
            newCategory.setId(root.getId());
            newCategory.setIsdelete(1);
            categoryMapper.updateById(newCategory);
        }
    }

}
