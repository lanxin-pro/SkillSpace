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
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: j-sentinel
 * @Date: 2023/5/12 21:23
 */
@Mapper
public interface AuthConvert {

    AuthMenuRespVO convertTreeNode(MenuDO menu);

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

    /**
     * 将菜单列表，构建成菜单树
     *
     * @param menuList 菜单列表
     * @return 菜单树
     */
    default List<AuthMenuRespVO> buildMenuTree(List<MenuDO> menuList){
        // 排序，保证菜单的有序性
        menuList.sort(Comparator.comparing(MenuDO::getSort));
        // 构建菜单树 键值对的形式来存储
        // 使用 LinkedHashMap 的原因，是为了排序 。实际也可以用 Stream API ，就是太丑了。
        Map<Long, AuthMenuRespVO> treeNodeMap = new LinkedHashMap<>();
        menuList.stream().forEach(menuDO -> {
            AuthMenuRespVO authMenuRespVO = AuthConvert.INSTANCE.convertTreeNode(menuDO);
            treeNodeMap.put(menuDO.getId(),authMenuRespVO);
        });
        // 处理父子关系
        treeNodeMap.values().stream().filter(node -> {
            // !(排除) 根节点，保留子节点
            return !node.getParentId().equals(MenuDO.ID_ROOT);
            // !(排除) 根节点，保留子节点，这里面没有父节点
        }).forEach(childNode -> {
            // 获得父节点
            AuthMenuRespVO parentNode = treeNodeMap.get(childNode.getParentId());
            // 如果为null就是 找不到根节点
            if(parentNode == null){
                LoggerFactory.getLogger(getClass()).error("[buildRouterTree][resource({}) 找不到父资源({})]",
                        childNode.getId(), childNode.getParentId());
                return;
            }
            // 没有子节点自己就是父节点
            if(parentNode.getChildren() == null){
                parentNode.setChildren(new ArrayList<>());
            }
            parentNode.getChildren().add(childNode);
        });
        // 获得到所有的根节点
        List<AuthMenuRespVO> collect = treeNodeMap.values().stream().filter(node -> {
            return MenuDO.ID_ROOT.equals(node.getParentId());
        }).collect(Collectors.toList());
        return collect;
    }
}
