package cn.iocoder.educate.module.system.dal.mysql.post;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.dept.vo.post.PostPageReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.educate.module.system.dal.post.PostDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/3 18:30
 */
@Mapper
public interface PostMapper extends BaseMapper<PostDO> {

    default List<PostDO> selectList(Collection<Long> ids, Collection<Integer> statuses){
        LambdaQueryWrapper<PostDO> postDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        postDOLambdaQueryWrapper.in(CollectionUtil.isNotEmpty(ids),PostDO::getId,ids);
        postDOLambdaQueryWrapper.in(CollectionUtil.isNotEmpty(statuses),PostDO::getStatus,statuses);
        return this.selectList(postDOLambdaQueryWrapper);
    }

    default PageResult<PostDO> selectPage(PostPageReqVO postPageReqVO) {
        LambdaQueryWrapper<PostDO> postDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        postDOLambdaQueryWrapper
                .like(StringUtils.hasText(postPageReqVO.getCode()),PostDO::getCode,postPageReqVO.getCode())
                .like(StringUtils.hasText(postPageReqVO.getName()),PostDO::getName,postPageReqVO.getName())
                .eq(ObjectUtil.isNotEmpty(postPageReqVO.getStatus()),PostDO::getStatus,postPageReqVO.getStatus())
                .orderByDesc(PostDO::getId);
        Page<PostDO> mpPage = new Page<>(postPageReqVO.getPageNo(), postPageReqVO.getPageSize());
        Page<PostDO> adminUserDOPage = this.selectPage(mpPage, postDOLambdaQueryWrapper);
        return new PageResult<>(adminUserDOPage.getRecords(),adminUserDOPage.getTotal());
    }

    default PostDO selectByName(String name){
        LambdaQueryWrapper<PostDO> postDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        postDOLambdaQueryWrapper.eq(PostDO::getName,name);
        return this.selectOne(postDOLambdaQueryWrapper);
    }

    default PostDO selectByCode(String code){
        LambdaQueryWrapper<PostDO> postDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        postDOLambdaQueryWrapper.eq(PostDO::getCode,code);
        return this.selectOne(postDOLambdaQueryWrapper);
    }

}
