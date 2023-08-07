package cn.iocoder.educate.module.system.dal.mysql.oauth2;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.collection.ArrayUtils;
import cn.iocoder.educate.module.system.controller.admin.oauth2.vo.client.OAuth2ClientPageReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2ClientDO;
import cn.iocoder.educate.module.system.dal.dataobject.user.AdminUserDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

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

    default PageResult<OAuth2ClientDO> selectPage(OAuth2ClientPageReqVO oAuth2ClientPageReqVO) {
        LambdaQueryWrapper<OAuth2ClientDO> oAuth2ClientDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
        oAuth2ClientDOLambdaQueryWrapper
                .like(StringUtils.hasText(oAuth2ClientPageReqVO.getName()),
                        OAuth2ClientDO::getName,oAuth2ClientPageReqVO.getName())
                .eq(ObjectUtil.isNotEmpty(oAuth2ClientPageReqVO.getStatus()),
                        OAuth2ClientDO::getStatus,oAuth2ClientPageReqVO.getStatus())
                .orderByDesc(OAuth2ClientDO::getId);
        Page<OAuth2ClientDO> page = new Page<>(oAuth2ClientPageReqVO.getPageNo(), oAuth2ClientPageReqVO.getPageSize());
        Page<OAuth2ClientDO> oAuth2ClientDOPage = this.selectPage(page, oAuth2ClientDOLambdaQueryWrapper);
        return new PageResult<>(oAuth2ClientDOPage.getRecords(),oAuth2ClientDOPage.getTotal());
    }


}
