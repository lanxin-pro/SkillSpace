package cn.iocoder.educate.module.system.service.permission;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.intern.InternUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import cn.iocoder.educate.module.system.dal.dataobject.permission.MenuDO;
import cn.iocoder.educate.module.system.dal.dataobject.permission.RoleDO;
import cn.iocoder.educate.module.system.dal.dataobject.permission.RoleMenuDO;
import cn.iocoder.educate.module.system.dal.dataobject.permission.UserRoleDO;
import cn.iocoder.educate.module.system.dal.mysql.permission.RoleMenuMapper;
import cn.iocoder.educate.module.system.dal.mysql.permission.UserRoleMapper;
import cn.iocoder.educate.module.system.mq.producer.permission.PermissionProducer;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.singleton;

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

    @Resource
    private PermissionProducer permissionProducer;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processUserDeleted(Long userId) {
        userRoleMapper.deleteListByUserId(userId);
        // 事务执行成功以后
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
                permissionProducer.sendUserRoleRefreshMessage();
            }

        });
    }

    /**
     * 传递进来的Permissions为请求
     *
     * @param userId 用户编号
     * @param permissions 权限
     * @return
     */
    @Override
    public boolean hasAnyPermissions(Long userId, String... permissions) {
        // 如果为空，说明已经有权限
        if (ArrayUtil.isEmpty(permissions)) {
            return true;
        }
        // 获得当前登录的角色。如果为空，说明没有权限
        Set<Long> roleIds = getUserRoleIdsFromCache(userId, CommonStatusEnum.ENABLE.getStatus());
        if(CollUtil.isEmpty(roleIds)){
            return false;
        }
        List<RoleDO> roleListFromCache = roleService.getRoleListFromCache(roleIds);
        // 判断是否是超管。如果是，当然符合条件
        if (roleService.hasAnySuperAdmin(roleListFromCache)) {
            return true;
        }
        // 遍历权限，判断是否有一个满足
        return Arrays.stream(permissions).anyMatch(permission -> {
            // 查询 权限menu列表
            List<MenuDO> menuList = menuService.getMenuListByPermissionFromCache(permission);
            // 从数据库中 找不到menu菜单就是没有权限
            if (CollUtil.isEmpty(menuList)) {
                return false;
            }
            // 获得是否拥有该权限，任一一个
            return menuList.stream().anyMatch(menu -> {
                // 其中一个集合在另一个集合中是否至少包含一个元素
                return CollUtil.containsAny(roleIds, menuRoleCache.get(menu.getId()));
            });
        });
    }

    @Override
    public boolean hasAnyRoles(Long userId, String... roles) {
        // 如果为空，说明已经有权限
        if(ArrayUtil.isEmpty(roles)){
            return true;
        }
        // 获得当前登录的角色。如果为空，说明没有权限
        Set<Long> roleIds = getUserRoleIdsFromCache(userId, CommonStatusEnum.ENABLE.getStatus());
        if (CollUtil.isEmpty(roleIds)) {
            return false;
        }
        // 判断是否是超管。如果是，当然符合条件
        List<RoleDO> roleListFromCache = roleService.getRoleListFromCache(roleIds);
        if (roleService.hasAnySuperAdmin(roleListFromCache)) {
            return true;
        }
        Set<String> userRoles = roleListFromCache
                .stream()
                .map(RoleDO::getCode)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        return CollUtil.containsAny(userRoles, Sets.newHashSet(roles));
    }

    @Override
    public Set<Long> getRoleMenuIds(Long roleId) {
        // 根据roleId获取RoleDO我主要是想要角色类型的字段
        List<RoleDO> roleListFromCache = roleService.getRoleListFromCache(singleton(roleId));
        // 如果是管理员的情况下，获取全部菜单编号
        if (roleService.hasAnySuperAdmin(roleListFromCache)) {
            return menuService.getMenuList()
                    .stream()
                    .map(MenuDO::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
        }
        // 如果是非管理员的情况下，获得拥有的菜单编号
        return roleMenuMapper.selectListByRoleId(roleId)
                .stream()
                .map(RoleMenuDO::getMenuId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processRoleDeleted(Long roleId) {
        // 标记删除 UserRole
        userRoleMapper.deleteListByRoleId(roleId);
        // 标记删除 RoleMenu
        roleMenuMapper.deleteListByRoleId(roleId);
        // 发送刷新消息. 注意，需要事务提交后，在进行发送刷新消息。不然 db 还未提交，结果缓存先刷新了
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
                permissionProducer.sendUserRoleRefreshMessage();
                permissionProducer.sendRoleMenuRefreshMessage();
            }

        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRoleMenu(Long roleId, Set<Long> menuIds) {
/*
        // TODO j-sentinel 这里考虑优化，实际上不差这点查询
        Collection<Long> longs = roleMenuCache.get(roleId);
        Set<Long> dbMenuIds = new HashSet<>(longs);
*/

        // 获得角色拥有菜单编号
        Set<Long> dbMenuIds = roleMenuMapper.selectListByRoleId(roleId)
                .stream()
                .map(RoleMenuDO::getMenuId)
                .collect(Collectors.toSet());
        // 计算新增和删除的菜单编号
        // 计算集合的单差集，即只返回【集合1】中有，但是【集合2】中没有的元素
        Collection<Long> createMenuIds = CollUtil.subtract(menuIds, dbMenuIds);
        Collection<Long> deleteMenuIds = CollUtil.subtract(dbMenuIds, menuIds);
        // 执行新增和删除。对于已经授权的菜单，不用做任何处理
        if (!CollectionUtil.isEmpty(createMenuIds)) {
            List<RoleMenuDO> roleMenuDOList = createMenuIds
                    .stream()
                    .map(menuId -> {
                        RoleMenuDO entity = new RoleMenuDO();
                        entity.setRoleId(roleId);
                        entity.setMenuId(menuId);
                        return entity;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            roleMenuMapper.insertBatch(roleMenuDOList);
        }
        if (!CollectionUtil.isEmpty(deleteMenuIds)) {
            roleMenuMapper.deleteListByRoleIdAndMenuIds(roleId, deleteMenuIds);
        }

        // 发送刷新消息. 注意，需要事务提交后，在进行发送刷新消息。不然 db 还未提交，结果缓存先刷新了
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
                permissionProducer.sendRoleMenuRefreshMessage();
            }

        });
    }

    public static boolean isAnyEmpty(Collection<?>... collections) {
        return Arrays.stream(collections).anyMatch(CollectionUtil::isEmpty);
    }

}
