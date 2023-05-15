package cn.iocoder.educate.module.system.dal.mysql.oauth2;

import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2ClientDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 * OAuth2 客户端 Mapper
 *
 * @Author: j-sentinel
 * @Date: 2023/5/15 20:24
 */
@Mapper
public interface OAuth2ClientMapper extends BaseMapper<OAuth2ClientDO> {

    default OAuth2ClientDO selectByClientId(String clientId) {
        LambdaQueryWrapper<OAuth2ClientDO> oAuth2ClientDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        oAuth2ClientDOLambdaQueryWrapper.eq(OAuth2ClientDO::getClientId,clientId);
        return this.selectOne(oAuth2ClientDOLambdaQueryWrapper);
    }


}
