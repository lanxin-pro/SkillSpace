package cn.iocoder.educate.module.system.dal.mysql.oauth2;

import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2RefreshTokenDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: 董伟豪
 * @Date: 2023/5/16 11:31
 */
@Mapper
public interface OAuth2RefreshTokenMapper extends BaseMapper<OAuth2RefreshTokenDO> {

    default int deleteByRefreshToken(String refreshToken){
        LambdaQueryWrapper<OAuth2RefreshTokenDO> oAuth2RefreshTokenDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        oAuth2RefreshTokenDOLambdaQueryWrapper.eq(OAuth2RefreshTokenDO::getRefreshToken,refreshToken);
        return this.delete(oAuth2RefreshTokenDOLambdaQueryWrapper);
    }

}
