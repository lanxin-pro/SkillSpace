package cn.iocoder.educate.module.system.convert.auth;

import cn.iocoder.educate.module.system.api.sms.dto.code.SmsCodeSendReqDTO;
import cn.iocoder.educate.module.system.api.sms.dto.code.SmsCodeUseReqDTO;
import cn.iocoder.educate.module.system.controller.admin.auth.vo.*;
import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import cn.iocoder.educate.module.system.dal.dataobject.permission.MenuDO;
import cn.iocoder.educate.module.system.dal.dataobject.permission.RoleDO;
import cn.iocoder.educate.module.system.dal.dataobject.user.AdminUserDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/12 21:23
 */
@Mapper
public interface AuthConvert {

    AuthConvert INSTANCE = Mappers.getMapper(AuthConvert.class);

    AuthLoginRespVO convert(OAuth2AccessTokenDO bean);

    SmsCodeSendReqDTO convert(AuthSmsSendReqVO bean);

    SmsCodeUseReqDTO convert(AuthSmsLoginReqVO reqVO, Integer scene, String usedIp);

    default AuthPermissionInfoRespVO convert(AdminUserDO user, List<RoleDO> roleList, List<MenuDO> menuList){
        return AuthPermissionInfoRespVO.builder()
                .user(UserVO.builder()
                        .id(user.getId())
                        .nickname(user.getNickname())
                        .avatar(user.getAvatar()).build())
                .roles(roleList.stream()
                        .map(RoleDO::getCode)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet()))
                .permissions(menuList.stream()
                        .map(MenuDO::getPermission)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet()))
                .build();
    }
}
