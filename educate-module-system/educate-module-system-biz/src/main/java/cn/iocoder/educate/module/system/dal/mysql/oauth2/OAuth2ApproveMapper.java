package cn.iocoder.educate.module.system.dal.mysql.oauth2;

import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2ApproveDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/10/15 17:42
 */
@Mapper
public interface OAuth2ApproveMapper extends BaseMapper<OAuth2ApproveDO> {

    default List<OAuth2ApproveDO> selectListByUserIdAndUserTypeAndClientId(Long userId, Integer userType, String clientId){
        LambdaQueryWrapper<OAuth2ApproveDO> oAuth2ApproveDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        oAuth2ApproveDOLambdaQueryWrapper.eq(OAuth2ApproveDO::getUserId, userId)
                .eq(OAuth2ApproveDO::getUserType, userType)
                .eq(OAuth2ApproveDO::getClientId, clientId);
        return this.selectList(oAuth2ApproveDOLambdaQueryWrapper);
    }

    default int update(OAuth2ApproveDO oAuth2ApproveDO) {
        LambdaQueryWrapper<OAuth2ApproveDO> oAuth2ApproveDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        oAuth2ApproveDOLambdaQueryWrapper.eq(OAuth2ApproveDO::getUserId, oAuth2ApproveDO.getUserId())
                .eq(OAuth2ApproveDO::getUserType, oAuth2ApproveDO.getUserType())
                .eq(OAuth2ApproveDO::getClientId, oAuth2ApproveDO.getClientId())
                .eq(OAuth2ApproveDO::getScope, oAuth2ApproveDO.getScope());
        return this.update(oAuth2ApproveDO,oAuth2ApproveDOLambdaQueryWrapper);
    }

}
