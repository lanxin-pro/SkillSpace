package cn.iocoder.educate.module.course.dal.mysql.category;

import cn.iocoder.educate.module.course.controller.admin.category.bo.CategoryBo;
import cn.iocoder.educate.module.course.dal.dataobject.category.CategoryDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import jodd.util.StringUtil;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author j-sentinel
 * @date 2024/4/13 17:39
 */
@Mapper
public interface CategoryMapper extends BaseMapper<CategoryDO> {

    default List<CategoryBo> findCategoryTree(String keyword) {
        LambdaQueryWrapper<CategoryDO> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.like(StringUtil.isNotEmpty(keyword), CategoryDO::getTitle, keyword);
        List<CategoryDO> allList = this.selectList(categoryLambdaQueryWrapper);
        List<CategoryBo> categoryBos = tranferListBo(allList, CategoryBo.class);
        // 找到所有的根节点 pid = 0
        List<CategoryBo> rootList = categoryBos.stream().filter(category -> {
            return category.getPid().equals(0L);
        }).sorted((a,b)->a.getSorted()-b.getSorted()).collect(Collectors.toList());
        List<CategoryBo> subList = categoryBos.stream().filter(category -> {
            return !category.getPid().equals(0L);
        }).collect(Collectors.toList());
        rootList.forEach(root->{
            buckForback(root,subList);
        });
        return rootList;
    }

    default <R,M> List<R> tranferListBo(List<M> userList, Class<R> cla){
        return userList.stream().map(user -> {
            return tranferBo(user,cla);
        }).collect(Collectors.toList());
    }

    default <R,M> R tranferBo(M user,Class<R> userBoClass){
        try {
            // 反射实例化
            R userBo = userBoClass.newInstance();
            BeanUtils.copyProperties(user,userBo);
            return userBo;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void buckForback(CategoryBo root, List<CategoryBo> subList) {
        List<CategoryBo> childrenList = subList.stream().filter(category -> {
            return category.getPid().equals(root.getId());
        }).sorted((a, b) -> a.getSorted() - b.getSorted()).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(childrenList)) {
            root.setChildren(childrenList);
            childrenList.forEach(category -> {
                buckForback(category, subList);
            });
        } else {
            root.setChildren(new ArrayList<>());
        }
    }

    default boolean saveOrUpdate(CategoryDO category) {
        Assert.notNull(category, "category is null");
        if (category.getId() == null) {
            return this.insert(category) > 0;
        } else {
            return this.updateById(category) > 0;
        }
    }
}
