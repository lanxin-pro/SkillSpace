package cn.iocoder.educate.module.system.dal.mysql.post;

import cn.iocoder.educate.module.system.dal.dataobject.dept.UserPostDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/29 13:15
 */
@Mapper
public interface UserPostMapper extends BaseMapper<UserPostDO> {

    /**
     * 批量插入，适合大量数据插入
     *
     * @param userPostCollect 实体们
     */
    default void insertBatch(List<UserPostDO> userPostCollect){
        Db.saveBatch(userPostCollect);
    }

    default List<UserPostDO> selectListByUserId(Long userId){
        LambdaQueryWrapper<UserPostDO> userPostDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userPostDOLambdaQueryWrapper.eq(UserPostDO::getUserId, userId);
        return selectList(userPostDOLambdaQueryWrapper);
    }

    default void deleteByUserIdAndPostId(Long userId, Collection<Long> postIds) {
        LambdaQueryWrapper<UserPostDO> userPostDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userPostDOLambdaQueryWrapper.eq(UserPostDO::getUserId, userId)
                .in(UserPostDO::getPostId, postIds);
        delete(userPostDOLambdaQueryWrapper);
    }

    default void deleteByUserId(Long userId){
        LambdaQueryWrapper<UserPostDO> userPostDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userPostDOLambdaQueryWrapper.eq(UserPostDO::getUserId, userId);
        this.delete(userPostDOLambdaQueryWrapper);
    }

}
