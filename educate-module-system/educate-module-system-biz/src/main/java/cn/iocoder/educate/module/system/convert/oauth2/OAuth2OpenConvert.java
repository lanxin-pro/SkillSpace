package cn.iocoder.educate.module.system.convert.oauth2;

import cn.iocoder.educate.framework.common.core.KeyValue;
import cn.iocoder.educate.module.system.controller.admin.oauth2.vo.open.OAuth2OpenAuthorizeInfoRespVO;
import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2ApproveDO;
import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2ClientDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: 董伟豪
 * @Date: 2023/6/9 10:11
 */
@Mapper
public interface OAuth2OpenConvert {

    OAuth2OpenConvert INSTANCE = Mappers.getMapper(OAuth2OpenConvert.class);

    default OAuth2OpenAuthorizeInfoRespVO convert(OAuth2ClientDO client, List<OAuth2ApproveDO> approves) {
        // 构建 scopes
        List<KeyValue<String, Boolean>> scopes = new ArrayList<>(client.getScopes().size());
        // List->Map
        Map<String, OAuth2ApproveDO> approveMap = approves
                .stream()
                .collect(Collectors.toMap(OAuth2ApproveDO::getScope, Function.identity(), (oldValue, newValue) -> newValue));
        // 每一个授权范围 从 approveMap 中获取对应的 OAuth2ApproveDO 对象，并判断是否为空。如果不为空，
        // 则取出对应的 approve.getApproved() 值，否则设为 false。
        client.getScopes().forEach(scope -> {
            OAuth2ApproveDO approve = approveMap.get(scope);
            scopes.add(new KeyValue<>(scope, approve != null ? approve.getApproved() : false));
        });
        // 拼接返回
        return new OAuth2OpenAuthorizeInfoRespVO(
                new OAuth2OpenAuthorizeInfoRespVO.Client(client.getName(), client.getLogo()), scopes);
    }
}
