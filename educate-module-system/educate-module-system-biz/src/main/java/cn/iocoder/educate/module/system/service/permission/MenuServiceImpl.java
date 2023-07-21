package cn.iocoder.educate.module.system.service.permission;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.module.system.controller.admin.permission.vo.menu.MenuCreateReqVO;
import cn.iocoder.educate.module.system.controller.admin.permission.vo.menu.MenuListReqVO;
import cn.iocoder.educate.module.system.convert.permission.MenuConvert;
import cn.iocoder.educate.module.system.dal.dataobject.permission.MenuDO;
import cn.iocoder.educate.module.system.dal.mysql.permission.MenuMapper;
import cn.iocoder.educate.module.system.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.system.enums.permission.MenuTypeEnum;
import cn.iocoder.educate.module.system.mq.producer.permission.MenuProducer;
import cn.iocoder.educate.module.system.service.tenant.TenantService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
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

    @Resource
    private MenuProducer menuProducer;

    @Resource
    private TenantService tenantService;

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

    @Override
    public Long createMenu(MenuCreateReqVO reqVO) {
        // 校验父菜单是否存在
        validateParentMenu(reqVO.getParentId(), null);
        // 校验菜单（自己）
        validateMenu(reqVO.getParentId(),reqVO.getName(),null);
        // 插入数据库
        MenuDO menuDO = MenuConvert.INSTANCE.convert(reqVO);
        initMenuProperty(menuDO);
        menuMapper.insert(menuDO);
        menuProducer.sendMenuRefreshMessage();
        // 发送刷新消息
        return menuDO.getId();
    }

    @Override
    public List<MenuDO> getMenuListByTenant(MenuListReqVO menuListReqVO) {
        List<MenuDO> menus = getMenuList(menuListReqVO);
        // TODO j-sentinel 以后的多租户设置

        return menus;
    }

    /**
     * 初始化菜单的通用属性
     *
     * 例如说，只有目录或者菜单类型的菜单，才设置 icon
     *
     * @param menu 菜单
     */
    private void initMenuProperty(MenuDO menu) {
        // 菜单为按钮类型时，无需 component、icon、path 属性，进行置空
        if(MenuTypeEnum.BUTTON.getType().equals(menu.getType())) {
            menu.setComponent("");
            menu.setComponentName("");
            menu.setIcon("");
            menu.setPath("");
        }
    }

    /**
     * 校验菜单是否合法
     *
     * 1. 校验相同父菜单编号下，是否存在相同的菜单名
     *
     * @param parentId 菜单名字
     * @param name 父菜单编号
     * @param id 菜单编号
     */
    void validateMenu(Long parentId, String name, Long id) {
        // 相同父类下面是否有相同的名称
        MenuDO menu = menuMapper.selectByParentIdAndName(parentId,name);
        // menu如果等于null就表明库里面没有该相同的名称
        if(menu == null){
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的菜单
        if(id == null){
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.MENU_NAME_DUPLICATE);
        }
        if(!menu.getId().equals(id)){
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.MENU_NAME_DUPLICATE);
        }
    }

    /**
     * 校验父菜单是否合法
     *
     * 1. 不能设置自己为父菜单
     * 2. 父菜单不存在
     * 3. 父菜单必须是 {@link MenuTypeEnum#MENU} 菜单类型
     *
     * @param parentId 父菜单编号
     * @param childId 当前菜单编号
     */
    private void validateParentMenu(Long parentId, Long childId) {
        if(parentId == null || MenuDO.ID_ROOT.equals(parentId)){
            return;
        }
        // 不能设置自己为父菜单
        if(parentId.equals(childId)){
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.MENU_PARENT_ERROR);
        }
        MenuDO menu = menuMapper.selectById(parentId);
        // 父菜单不存在
        if(menu == null){
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.MENU_PARENT_NOT_EXISTS);
        }
        // 父菜单必须是目录或者菜单类型
        if(!MenuTypeEnum.DIR.getType().equals(menu.getType())
            && !MenuTypeEnum.MENU.getType().equals(menu.getType())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.MENU_PARENT_NOT_DIR_OR_MENU);
        }
    }

    public static boolean isAnyEmpty(Collection<?>... collections) {
        return Arrays.stream(collections).anyMatch(CollectionUtil::isEmpty);
    }
}
