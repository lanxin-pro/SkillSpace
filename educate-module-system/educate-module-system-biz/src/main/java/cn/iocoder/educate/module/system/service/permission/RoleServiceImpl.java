package cn.iocoder.educate.module.system.service.permission;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.permission.vo.role.RolePageReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.permission.RoleDO;
import cn.iocoder.educate.module.system.dal.mysql.permission.RoleMapper;
import cn.iocoder.educate.module.system.enums.permission.RoleCodeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
public class RoleServiceImpl implements RoleService{

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
}
