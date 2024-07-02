package cn.iocoder.educate.module.course.controller.admin.category;

import cn.iocoder.educate.module.course.controller.admin.category.bo.CategoryBo;
import cn.iocoder.educate.module.course.controller.admin.category.vo.CategoryVo;
import cn.iocoder.educate.module.course.dal.dataobject.category.CategoryDO;
import cn.iocoder.educate.module.course.service.category.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author j-sentinel
 * @date 2024/4/12 14:10
 */
@Tag(name = "管理后台 - 课程-章节")
@RestController
@RequestMapping("/course/category")
@Validated
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     * 查询管理分类
     * @return
     */
    @GetMapping("/tree")
    public List<CategoryBo> findCategoryTree(@RequestParam("keyword") String keyword){
        return categoryService.findCategoryTree(keyword);
    }

    /**
     * 保存和修改文章分类管理
     * @param category
     * @return
     */
    @PostMapping("/saveupdate")
    public CategoryBo saveUpdateCategory(@RequestBody CategoryDO category) {
        return categoryService.saveUpdateCategory(category);
    }

    /**
     * 根据文章分类管理id删除文章分类管理
     * @param id
     * @return
     */
    @PostMapping("/delete/{id}")
    public int deleteCategoryById(@PathVariable("id") String id) {
        return categoryService.deleteCategoryById(new Long(id));
    }

    /**
     * 根据文章分类管理ids批量删除文章分类管理
     * @param categoryVo
     * @return
     */
    @PostMapping("/delBatch")
    public boolean deleteCategoryBatch(@RequestBody CategoryVo categoryVo) {
        return categoryService.deleteBatchCategory(categoryVo.getBatchIds());
    }

}
