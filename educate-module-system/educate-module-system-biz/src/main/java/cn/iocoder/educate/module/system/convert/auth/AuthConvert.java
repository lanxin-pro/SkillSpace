package cn.iocoder.educate.module.system.convert.auth;

import cn.iocoder.educate.module.system.api.sms.dto.code.SmsCodeSendReqDTO;
import cn.iocoder.educate.module.system.controller.admin.auth.vo.AuthLoginRespVO;
import cn.iocoder.educate.module.system.controller.admin.auth.vo.AuthSmsSendReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/12 21:23
 */
@Mapper
public interface AuthConvert {

    AuthConvert INSTANCE = Mappers.getMapper(AuthConvert.class);

    AuthLoginRespVO convert(OAuth2AccessTokenDO bean);

    SmsCodeSendReqDTO convert(AuthSmsSendReqVO bean);
}
