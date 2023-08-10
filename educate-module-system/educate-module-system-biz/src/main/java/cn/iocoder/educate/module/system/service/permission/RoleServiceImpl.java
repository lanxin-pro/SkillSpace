package cn.iocoder.educate.module.system.service.permission;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.permission.vo.role.RoleCreateReqVO;
import cn.iocoder.educate.module.system.controller.admin.permission.vo.role.RolePageReqVO;
import cn.iocoder.educate.module.system.controller.admin.permission.vo.role.RoleUpdateReqVO;
import cn.iocoder.educate.module.system.convert.permission.RoleConvert;
import cn.iocoder.educate.module.system.dal.dataobject.permission.RoleDO;
import cn.iocoder.educate.module.system.dal.mysql.permission.RoleMapper;
import cn.iocoder.educate.module.system.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.system.enums.permission.DataScopeEnum;
import cn.iocoder.educate.module.system.enums.permission.RoleCodeEnum;
import cn.iocoder.educate.module.system.enums.permission.RoleTypeEnum;
import cn.iocoder.educate.module.system.mq.producer.permission.RoleProducer;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 角色 Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/6/22 18:02
 */
@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    /**
     * 角色缓存
     * key：角色编号 {@link RoleDO#getId()}
     *
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    @Getter
    private volatile Map<Long, RoleDO> roleCache;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleProducer roleProducer;

    @Resource
    private PermissionService permissionService;


    /**
     * 初始化 {@link #roleCache} 缓存
     */
    @Override
    @PostConstruct
    public void initLocalCache() {
        // 第一步：查询数据
        List<RoleDO> roleDOS = roleMapper.selectList(new LambdaQueryWrapper<>());
        log.info("[initLocalCache][缓存角色，数量为:{}]", roleDOS.size());

        // 第二步：构建缓存
        Map<Long, RoleDO> collect = roleDOS.stream().collect(Collectors.toMap(RoleDO::getId,
                Function.identity(), (v1, v2) -> v1));
        roleCache = collect;
    }

    @Override
    public List<RoleDO> getRoleListFromCache(Set<Long> roleIds) {
        // 是空就返回空
        if(CollUtil.isEmpty(roleIds)){
            return Collections.emptyList();
        }
        List<RoleDO> collect = roleCache.values().stream().
                filter(roleDO -> roleIds.contains(roleDO.getId())).collect(Collectors.toList());
        return collect;
    }

    @Override
    public RoleDO getRoleFromCache(Long roleId) {
        return roleCache.get(roleId);
    }

    @Override
    public boolean hasAnySuperAdmin(List<RoleDO> roleListFromCache) {
        if(CollUtil.isEmpty(roleListFromCache)){
            return false;
        }
        return roleListFromCache.stream().anyMatch(role -> {
            return RoleCodeEnum.isSuperAdmin(role.getCode());
        });
    }

    @Override
    public PageResult<RoleDO> getRolePage(RolePageReqVO rolePageReqVO) {
        return roleMapper.selectPage(rolePageReqVO);
    }

    @Override
    public RoleDO getRole(Long id) {
        return roleCache.get(id);
    }

    @Override
    @Transactional
    public Long createRole(RoleCreateReqVO roleCreateReqVO, Integer type) {
        // 校验角色
        validateRoleDuplicate(roleCreateReqVO.getName(), roleCreateReqVO.getCode(), null);
        // 插入到数据库
        RoleDO role = RoleConvert.INSTANCE.convert(roleCreateReqVO);
        // 默认的类型，创建的时候无法选择
        role.setType(ObjectUtil.defaultIfNull(type, RoleTypeEnum.CUSTOM.getType()));
        role.setStatus(CommonStatusEnum.ENABLE.getStatus());
        // 默认可查看所有数据。原因是，可能一些项目不需要项目权限
        role.setDataScope(DataScopeEnum.ALL.getScope());
        roleMapper.insert(role);
        // 发送刷新消息
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
                roleProducer.sendRoleRefreshMessage();
            }

        });
        // 返回
        return role.getId();
    }

    @Override
    public void updateRole(RoleUpdateReqVO roleUpdateReqVO) {
        // 校验是否可以更新
        validateRoleForUpdate(roleUpdateReqVO.getId());
        // 校验角色的唯一字段是否重复
        validateRoleDuplicate(roleUpdateReqVO.getName(), roleUpdateReqVO.getCode(), roleUpdateReqVO.getId());

        // 更新到数据库
        RoleDO roleDO = RoleConvert.INSTANCE.convert(roleUpdateReqVO);
        roleMapper.updateById(roleDO);
        // 发送刷新消息
        roleProducer.sendRoleRefreshMessage();
    }

    @Override
    public void deleteRole(Long id) {
        // 校验是否可以删除
        validateRoleForUpdate(id);
        // 标记删除
        roleMapper.deleteById(id);
        // 删除相关数据
        permissionService.processRoleDeleted(id);
        // 发送刷新消息. 注意，需要事务提交后，在进行发送刷新消息。不然 db 还未提交，结果缓存先刷新了
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
                roleProducer.sendRoleRefreshMessage();
            }

        });
    }

    @Override
    public void updateRoleStatus(Long id, Integer status) {
        // 校验是否可以更新
        validateRoleForUpdate(id);

        // 更新状态
        RoleDO roleDO = new RoleDO().setId(id).setStatus(status);
        roleMapper.updateById(roleDO);
        // 发送刷新消息
        roleProducer.sendRoleRefreshMessage();
    }

    /**
     * 校验角色的唯一字段是否重复
     *
     * 1. 是否存在相同名字的角色
     * 2. 是否存在相同编码的角色
     *
     * @param name 角色名字
     * @param code 角色额编码
     * @param id 角色编号
     */
    void validateRoleDuplicate(String name, String code, Long id) {
        // 0. 超级管理员，不允许创建
        if (RoleCodeEnum.isSuperAdmin(code)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.ROLE_ADMIN_CODE_ERROR, code);
        }
        // 1. 该 name 名字被其它角色所使用
        RoleDO role = roleMapper.selectByName(name);
        if (role != null && !role.getId().equals(id)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.ROLE_NAME_DUPLICATE, name);
        }
        // 2. 是否存在相同编码的角色
        if (!StringUtils.hasText(code)) {
            return;
        }
        // 该 code 编码被其它角色所使用
        role = roleMapper.selectByCode(code);
        if (role != null && !role.getId().equals(id)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.ROLE_CODE_DUPLICATE, code);
        }
    }

    void validateRoleForUpdate(Long id) {
        RoleDO roleDO = roleCache.get(id);
        if (roleDO == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.ROLE_NOT_EXISTS);
        }
        // 内置角色，不允许删除
        if (RoleTypeEnum.SYSTEM.getType().equals(roleDO.getType())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.ROLE_CAN_NOT_UPDATE_SYSTEM_TYPE_ROLE);
        }
    }

}
