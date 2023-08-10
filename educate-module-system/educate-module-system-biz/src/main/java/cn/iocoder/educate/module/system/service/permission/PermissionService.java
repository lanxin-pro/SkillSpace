package cn.iocoder.educate.module.system.service.permission;

import cn.iocoder.educate.module.system.dal.dataobject.permission.MenuDO;
import org.springframework.lang.Nullable;
import java.util.List;
import java.util.Set;

/**
 * 权限 Service 接口
 *
 * 提供用户-角色、角色-菜单、角色-部门的关联权限处理
 *
 * @Author: j-sentinel
 * @Date: 2023/6/22 18:02
 */
public interface PermissionService {

    /**
     * 初始化权限的本地缓存
     */
    void initLocalCache();

    /**
     * 获得用户拥有的角色编号集合，从缓存中获取
     *
     * @param loginUserId 用户编号
     * @param status 角色状态集合. 允许为空，为空时不过滤
     * @return 角色编号集合
     */
    Set<Long> getUserRoleIdsFromCache(Long loginUserId, @Nullable Integer status);

    /**
     * 获得用户拥有的角色编号集合
     *
     * @param userId
     * @return
     */
    Set<Long> getUserRoleIdListByUserId(Long userId);

    /**
     * 获得角色们拥有的菜单列表，从缓存中获取
     *
     * 任一参数为空时，则返回为空
     *
     * @param roleIds 角色编号数组
     * @param asSet 菜单类型数组
     * @param status 菜单状态数组
     * @return 菜单列表
     */
    List<MenuDO> getRoleMenuListFromCache(Set<Long> roleIds, Set<Integer> asSet, Integer status);

    /**
     * 处理菜单删除时，删除关联授权数据
     *
     * @param menuId 菜单编号
     */
    void processMenuDeleted(Long menuId);

    /**
     * 处理用户删除是，删除关联授权数据
     *
     * @param userId 用户编号
     */
    void processUserDeleted(Long userId);

    /**
     * 判断是否有权限，任一一个即可
     *
     * @param userId 用户编号
     * @param permissions 权限
     * @return 是否
     */
    boolean hasAnyPermissions(Long userId, String... permissions);

    /**
     * 判断是否有角色，任一一个即可
     *
     * @param roles 角色数组
     * @return 是否
     */
    boolean hasAnyRoles(Long userId, String... roles);

    /**
     * 获得角色拥有的菜单编号集合
     *
     * @param roleId 角色编号
     * @return 菜单编号集合
     */
    Set<Long> getRoleMenuIds(Long roleId);

    /**
     * 处理角色删除时，删除关联授权数据
     *
     * @param roleId 角色编号
     */
    void processRoleDeleted(Long roleId);

}
