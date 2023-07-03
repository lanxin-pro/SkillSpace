package cn.iocoder.educate.module.system.service.permission;

import cn.iocoder.educate.module.system.dal.dataobject.permission.MenuDO;

import java.util.List;
import java.util.Set;

/**
 * 菜单 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/6/22 18:03
 */
public interface MenuService {

    /**
     * 初始化菜单的本地缓存
     */
    void initLocalCache();

    /**
     * 获得所有菜单，从缓存中
     *
     * 任一参数为空时，则返回为空
     *
     * @param menusType 菜单类型数组
     * @param status 菜单状态
     * @return 菜单列表
     */
    List<MenuDO> getMenuListFromCache(Set<Integer> menusType, Integer status);

    /**
     * 获得指定编号的菜单数组，从缓存中
     *
     * 任一参数为空时，则返回为空
     *
     * @param menuIds 菜单编号数组
     * @param menusType 菜单类型数组
     * @param status 菜单状态数组
     * @return 菜单数组
     */
    List<MenuDO> getMenuListFromCache(List<Long> menuIds, Set<Integer> menusType, Integer status);
}