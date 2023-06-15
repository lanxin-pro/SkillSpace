package cn.iocoder.educate.module.system.dal.mysql.social;

import cn.iocoder.educate.module.system.dal.dataobject.social.SocialUserDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: j-sentinel
 * @Date: 2023/6/15 21:36
 */
@Mapper
public interface SocialUserMapper extends BaseMapper<SocialUserDO> {

    default SocialUserDO selectByTypeAndCodeAnState(Integer type, String code, String state){
        LambdaQueryWrapper<SocialUserDO> socialUserDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        socialUserDOLambdaQueryWrapper.eq(SocialUserDO::getType,type);
        socialUserDOLambdaQueryWrapper.eq(SocialUserDO::getCode,code);
        socialUserDOLambdaQueryWrapper.eq(SocialUserDO::getState,state);
        return this.selectOne(socialUserDOLambdaQueryWrapper);
    }

    default SocialUserDO selectByTypeAndOpenid(Integer type, String uuid){
        LambdaQueryWrapper<SocialUserDO> socialUserDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        socialUserDOLambdaQueryWrapper.eq(SocialUserDO::getType,type);
        socialUserDOLambdaQueryWrapper.eq(SocialUserDO::getOpenid,uuid);
        return this.selectOne(socialUserDOLambdaQueryWrapper);
    }
}
