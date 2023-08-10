package cn.iocoder.educate.module.system.service.permission;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.permission.vo.role.RoleCreateReqVO;
import cn.iocoder.educate.module.system.controller.admin.permission.vo.role.RolePageReqVO;
import cn.iocoder.educate.module.system.controller.admin.permission.vo.role.RoleUpdateReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.permission.RoleDO;
import org.springframework.lang.Nullable;

import javax.validation.Valid;
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

    /**
     * 创建角色
     *
     * @param roleCreateReqVO 创建角色信息
     * @param type 角色类型
     * @return 角色编号
     */
    Long createRole(RoleCreateReqVO roleCreateReqVO, Integer type);

    /**
     * 更新角色
     *
     * @param reqVO 更新角色信息
     */
    void updateRole(@Valid RoleUpdateReqVO reqVO);

    /**
     * 删除角色
     *
     * @param id 角色编号
     */
    void deleteRole(Long id);

    /**
     * 更新角色状态
     *
     * @param id 角色编号
     * @param status 状态
     */
    void updateRoleStatus(Long id, Integer status);

    /**
     * 获得角色列表
     *
     * @param statuses 筛选的状态。允许空，空时不筛选
     * @return 角色列表
     */
    List<RoleDO> getRoleListByStatus(@Nullable Integer statuses);

}
