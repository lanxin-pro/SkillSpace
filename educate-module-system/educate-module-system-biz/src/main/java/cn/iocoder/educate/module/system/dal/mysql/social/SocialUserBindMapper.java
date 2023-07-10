package cn.iocoder.educate.module.system.dal.mysql.social;

import cn.iocoder.educate.module.system.dal.dataobject.social.SocialUserBindDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/6/15 22:31
 */
@Mapper
public interface SocialUserBindMapper extends BaseMapper<SocialUserBindDO> {

    default SocialUserBindDO selectByUserTypeAndSocialUserId(Integer userType, Long id){
        LambdaQueryWrapper<SocialUserBindDO> socialUserDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        socialUserDOLambdaQueryWrapper.eq(SocialUserBindDO::getUserType,userType);
        socialUserDOLambdaQueryWrapper.eq(SocialUserBindDO::getSocialUserId,id);
        return selectOne(socialUserDOLambdaQueryWrapper);
    }

    default List<SocialUserBindDO> selectListByUserIdAndUserType(Long userId, Integer userType){
        LambdaQueryWrapper<SocialUserBindDO> socialUserBindDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        socialUserBindDOLambdaQueryWrapper.eq(SocialUserBindDO::getUserId,userId);
        socialUserBindDOLambdaQueryWrapper.eq(SocialUserBindDO::getUserType,userType);
        return selectList(socialUserBindDOLambdaQueryWrapper);
    }
}
