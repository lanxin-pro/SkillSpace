package cn.iocoder.educate.module.system.service.permission;

import cn.hutool.core.collection.CollectionUtil;
import cn.iocoder.educate.module.system.controller.admin.permission.vo.menu.MenuListReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.permission.MenuDO;
import cn.iocoder.educate.module.system.dal.mysql.permission.MenuMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单 Service 实现
 *
 * @Author: j-sentinel
 * @Date: 2023/6/22 18:03
 */
@Slf4j
@Service
public class MenuServiceImpl implements MenuService{

    /**
     * 菜单缓存
     * key：菜单编号
     *
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    @Getter
    @Setter
    private volatile Map<Long, MenuDO> menuCache;

    /**
     * 权限与菜单缓存
     * key：权限 {@link MenuDO#getPermission()}
     * value：MenuDO 数组，因为一个权限可能对应多个 MenuDO 对象
     *
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    @Getter
    @Setter
    private volatile Multimap<String, MenuDO> permissionMenuCache;

    @Resource
    private MenuMapper menuMapper;

    @Override
    @PostConstruct
    public void initLocalCache() {
        // 第一步：查询数据
        List<MenuDO> menuList = menuMapper.selectList(new LambdaQueryWrapper<>());
        log.info("[initLocalCache][缓存菜单，数量为:{}]", menuList.size());

        // 第二步：构建缓存
        ImmutableMap.Builder<Long, MenuDO> menuCacheBuilder = ImmutableMap.builder();

        menuList.forEach(menuDO -> {
            menuCacheBuilder.put(menuDO.getId(),menuDO);
        });
        menuCache = menuCacheBuilder.build();
    }

    @Override
    public List<MenuDO> getMenuListFromCache(Set<Integer> menusType, Integer status) {
        // 任一一个参数为空，则返回空
        if(isAnyEmpty(menusType, Collections.singleton(status))){
            return Collections.emptyList();
        }
        // 创建新数组，避免缓存被修改
        List<MenuDO> collect = menuCache.values().stream().filter(menuDO -> {
            return menusType.contains(menuDO.getType())
                    && Collections.singleton(status).contains(menuDO.getStatus());
        }).collect(Collectors.toList());
        return collect;
    }

    /**
     * @param menuIds 菜单编号数组
     * @param menusType 菜单类型数组
     * @param status 菜单状态数组
     * @return
     */
    @Override
    public List<MenuDO> getMenuListFromCache(List<Long> menuIds, Set<Integer> menusType, Integer status) {
        if(isAnyEmpty(menuIds,menusType,Collections.singleton(status))){
            return Collections.emptyList();
        }
        return menuCache.values().stream().filter(menu -> {
            return menuIds.contains(menu.getId())
                    && menusType.contains(menu.getType())
                    && Collections.singleton(status).contains(menu.getStatus());
        }).collect(Collectors.toList());
    }

    @Override
    public List<MenuDO> getMenuList(MenuListReqVO reqVO) {
        return menuMapper.selectList(reqVO);
    }

    public static boolean isAnyEmpty(Collection<?>... collections) {
        return Arrays.stream(collections).anyMatch(CollectionUtil::isEmpty);
    }
}
