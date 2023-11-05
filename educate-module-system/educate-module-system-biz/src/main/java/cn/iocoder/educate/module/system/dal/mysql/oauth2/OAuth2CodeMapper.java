package cn.iocoder.educate.module.system.dal.mysql.oauth2;

import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2CodeDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: j-sentinel
 * @Date: 2023/10/15 19:58
 */
@Mapper
public interface OAuth2CodeMapper extends BaseMapper<OAuth2CodeDO> {

    default OAuth2CodeDO selectByCode(String code) {
        LambdaQueryWrapper<OAuth2CodeDO> oAuth2CodeDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        oAuth2CodeDOLambdaQueryWrapper.eq(OAuth2CodeDO::getCode,code);
        return this.selectOne(oAuth2CodeDOLambdaQueryWrapper);
    }

}
