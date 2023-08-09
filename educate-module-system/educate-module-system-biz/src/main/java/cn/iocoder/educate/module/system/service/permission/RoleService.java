package cn.iocoder.educate.module.system.service.permission;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.permission.vo.role.RolePageReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.permission.RoleDO;

import java.util.List;
import java.util.Set;

/**
 * 角色 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/6/22 18:01
 */
public interface RoleService {

    /**
     * 初始化角色的本地缓存
     */
    void initLocalCache();

    /**
     * 获得角色数组，从缓存中
     *
     * @param roleIds 角色编号数组
     * @return 角色数组
     */
    List<RoleDO> getRoleListFromCache(Set<Long> roleIds);

    /**
     * 获得角色，从缓存中
     *
     * @param roleId 角色编号
     * @return 角色
     */
    RoleDO getRoleFromCache(Long roleId);

    /**
     *  判断角色编号数组中，是否有超级管理员
     *
     * @param roleListFromCache 角色编号数组
     * @return 是否有超级管理员
     */
    boolean hasAnySuperAdmin(List<RoleDO> roleListFromCache);

    /**
     * 获得角色分页
     *
     * @param rolePageReqVO 角色分页查询
     * @return 角色分页结果
     */
    PageResult<RoleDO> getRolePage(RolePageReqVO rolePageReqVO);

    /**
     * 获得角色
     *
     * @param id 角色编号
     * @return 角色
     */
    RoleDO getRole(Long id);

}
