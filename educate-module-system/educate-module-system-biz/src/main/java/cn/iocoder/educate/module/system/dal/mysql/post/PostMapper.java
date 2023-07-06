package cn.iocoder.educate.module.system.dal.mysql.post;

import cn.hutool.core.collection.CollectionUtil;
import cn.iocoder.educate.module.system.dal.post.PostDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
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
}
