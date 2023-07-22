package cn.iocoder.educate.module.system.service.permission;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.intern.InternUtil;
import cn.iocoder.educate.module.system.dal.dataobject.permission.MenuDO;
import cn.iocoder.educate.module.system.dal.dataobject.permission.RoleDO;
import cn.iocoder.educate.module.system.dal.dataobject.permission.RoleMenuDO;
import cn.iocoder.educate.module.system.dal.dataobject.permission.UserRoleDO;
import cn.iocoder.educate.module.system.dal.mysql.permission.RoleMenuMapper;
import cn.iocoder.educate.module.system.dal.mysql.permission.UserRoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限 Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/6/22 18:02
 */
@Slf4j
@Service
public class PermissionServiceImpl implements PermissionService{

    /**
     * 角色编号与菜单编号的缓存映射
     * key：角色编号
     * value：菜单编号的数组
     *
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    @Getter
    @Setter // 单元测试需要
    private volatile Multimap<Long, Long> roleMenuCache;

    /**
     * 菜单编号与角色编号的缓存映射
     * key：菜单编号
     * value：角色编号的数组
     *
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    @Getter
    @Setter // 单元测试需要
    private volatile Multimap<Long, Long> menuRoleCache;

    /**
     * 用户编号与角色编号的缓存映射
     * key：用户编号
     * value：角色编号的数组
     *
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    @Getter
    @Setter // 单元测试需要
    private volatile Map<Long, Set<Long>> userRoleCache;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private MenuService menuService;

    @Resource
    private RoleService roleService;

    @Override
    @PostConstruct
    public void initLocalCache() {
        initLocalCacheForUserRole();
        initLocalCacheForRoleMenu();
    }

    /**
     * 刷新 UserRole 本地缓存
     */
    private void initLocalCacheForUserRole() {
        // 第一步：加载数据
        List<UserRoleDO> userRoleDOS = userRoleMapper.selectList(new LambdaQueryWrapper<>());
        log.info("[initLocalCacheForUserRole][缓存用户与角色，数量为:{}]", userRoleDOS.size());

        // 第二步：构建缓存
        userRoleCache = userRoleDOS.stream()
            .collect(Collectors.groupingBy(
                        UserRoleDO::getUserId,
                        // 对每个分组中的元素进行映射操作，并将映射结果收集到一个集合中，这里是将角色ID收集到一个 Set 集合中。
                        Collectors.mapping(UserRoleDO::getRoleId,Collectors.toSet())
                    )
            );
    }

    /**
     * 刷新 RoleMenu 本地缓存
     */
    private void initLocalCacheForRoleMenu() {
        // 第一步：查询数据
        List<RoleMenuDO> roleMenuDOS = roleMenuMapper.selectList(new LambdaQueryWrapper<>());
        log.info("[initLocalCacheForRoleMenu][缓存角色与菜单，数量为:{}]", roleMenuDOS.size());

        // 第二步：构建缓存
        ImmutableMultimap.Builder<Long, Long> roleMenuCacheBuilder = ImmutableMultimap.builder();
        ImmutableMultimap.Builder<Long, Long> menuRoleCacheBuilder = ImmutableMultimap.builder();
        roleMenuDOS.forEach(roleMenuDO -> {
            roleMenuCacheBuilder.put(roleMenuDO.getRoleId(),roleMenuDO.getMenuId());
            roleMenuCacheBuilder.put(roleMenuDO.getMenuId(),roleMenuDO.getRoleId());
        });
        roleMenuCache = roleMenuCacheBuilder.build();
        menuRoleCache = menuRoleCacheBuilder.build();
    }

    /**
     * @param loginUserId 用户编号
     * @param status 角色状态集合. 允许为空，为空时不过滤
     * @return
     */
    @Override
    public Set<Long> getUserRoleIdsFromCache(Long loginUserId, Integer status) {
        Set<Long> cacheRoleIds = userRoleCache.get(loginUserId);
        // 创建用户的时候没有分配角色，会存在空指针异常
        if(CollUtil.isEmpty(cacheRoleIds)){
            return Collections.emptySet();
        }
        // 创建一个新的集合
        Set<Long> roleIds = new HashSet<>(cacheRoleIds);
        // 过滤角色状态，真实的角色必须是数据库中的数据
        if(status != null){
            // 查询不到角色，或者是不开启状态 就删除
            roleIds.removeIf(roleId -> {
                RoleDO role = roleService.getRoleFromCache(roleId);
                // 没有该角色就返回null，有该角色必须是开启状态
                return role == null || !status.equals(role.getStatus());
            });
        }
        return roleIds;
    }

    @Override
    public Set<Long> getUserRoleIdListByUserId(Long userId) {
        List<UserRoleDO> userRoleDOS = userRoleMapper.selectListByUserId(userId);
        return userRoleDOS
                .stream()
                .map(UserRoleDO::getRoleId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    /**
     * @param roleIds 角色编号数组
     * @param menusType 菜单类型数组
     * @param status 菜单状态数组
     * @return
     */
    @Override
    public List<MenuDO> getRoleMenuListFromCache(Set<Long> roleIds, Set<Integer> menusType, Integer status) {
        // 任何一个参数为空时，不返回任何菜单
        if(isAnyEmpty(roleIds,menusType,Collections.singleton(status))){
            return Collections.emptyList();
        }
        // 判断角色是否包含超级管理员。如果是超级管理员，获取到全部
        List<RoleDO> roleListFromCache = roleService.getRoleListFromCache(roleIds);
        if(roleService.hasAnySuperAdmin(roleListFromCache)){
            return menuService.getMenuListFromCache(menusType,status);
        }
        List<Long> menuIds = new ArrayList<>();
        roleIds.forEach(roleId -> {
            Collection<Long> values = roleMenuCache.get(roleId);
            if (CollectionUtil.isEmpty(values)) {
                return;
            }
            menuIds.addAll(values);
        });
        return menuService.getMenuListFromCache(menuIds, menusType, status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processMenuDeleted(Long menuId) {
        roleMenuMapper.deleteListByMenuId(menuId);
    }

    public static boolean isAnyEmpty(Collection<?>... collections) {
        return Arrays.stream(collections).anyMatch(CollectionUtil::isEmpty);
    }
}
