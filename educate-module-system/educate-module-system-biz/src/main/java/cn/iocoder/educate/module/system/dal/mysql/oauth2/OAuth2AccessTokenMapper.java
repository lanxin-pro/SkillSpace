package cn.iocoder.educate.module.system.dal.mysql.oauth2;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.oauth2.vo.token.OAuth2AccessTokenPageReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2ClientDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

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

    default List<OAuth2AccessTokenDO> selectListByRefreshToken(String refreshToken){
        LambdaQueryWrapper<OAuth2AccessTokenDO> oAuth2AccessTokenDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        oAuth2AccessTokenDOLambdaQueryWrapper.eq(OAuth2AccessTokenDO::getRefreshToken,refreshToken);
        return selectList(oAuth2AccessTokenDOLambdaQueryWrapper);
    }

    default PageResult<OAuth2AccessTokenDO> selectPage(OAuth2AccessTokenPageReqVO oAuth2AccessTokenPageReqVO) {
        LambdaQueryWrapper<OAuth2AccessTokenDO> oAuth2ClientDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        oAuth2ClientDOLambdaQueryWrapper
                .like(StringUtils.hasText(oAuth2AccessTokenPageReqVO.getClientId()),
                        OAuth2AccessTokenDO::getClientId,oAuth2AccessTokenPageReqVO.getClientId())
                .eq(ObjectUtil.isNotEmpty(oAuth2AccessTokenPageReqVO.getUserId()),
                        OAuth2AccessTokenDO::getUserId,oAuth2AccessTokenPageReqVO.getUserId())
                .eq(ObjectUtil.isNotEmpty(oAuth2AccessTokenPageReqVO.getUserType()),
                        OAuth2AccessTokenDO::getUserType,oAuth2AccessTokenPageReqVO.getUserType())
                // and 过期时间大于当前时间
                .gt(OAuth2AccessTokenDO::getExpiresTime, LocalDateTime.now())
                .orderByDesc(OAuth2AccessTokenDO::getId);
        Page<OAuth2AccessTokenDO> page = new Page<>(oAuth2AccessTokenPageReqVO.getPageNo(), oAuth2AccessTokenPageReqVO.getPageSize());
        Page<OAuth2AccessTokenDO> auth2AccessTokenDOPage = this.selectPage(page, oAuth2ClientDOLambdaQueryWrapper);
        return new PageResult<>(auth2AccessTokenDOPage.getRecords(),auth2AccessTokenDOPage.getTotal());
    }

}
