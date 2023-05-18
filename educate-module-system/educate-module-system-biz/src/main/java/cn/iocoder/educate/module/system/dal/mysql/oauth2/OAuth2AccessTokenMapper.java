package cn.iocoder.educate.module.system.dal.mysql.oauth2;

import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: 董伟豪
 * @Date: 2023/5/16 11:34
 */
@Mapper
public interface OAuth2AccessTokenMapper extends BaseMapper<OAuth2AccessTokenDO> {

    default OAuth2AccessTokenDO selectByAccessToken(String accessToken){
        LambdaQueryWrapper<OAuth2AccessTokenDO> oAuth2AccessTokenDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        oAuth2AccessTokenDOLambdaQueryWrapper.eq(OAuth2AccessTokenDO::getAccessToken,accessToken);
        return selectOne(oAuth2AccessTokenDOLambdaQueryWrapper);
    }
}
