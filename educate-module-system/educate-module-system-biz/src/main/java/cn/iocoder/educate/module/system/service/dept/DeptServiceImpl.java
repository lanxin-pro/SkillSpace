package cn.iocoder.educate.module.system.service.dept;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.module.system.controller.admin.dept.vo.dept.DeptCreateReqVO;
import cn.iocoder.educate.module.system.controller.admin.dept.vo.dept.DeptListReqVO;
import cn.iocoder.educate.module.system.controller.admin.dept.vo.dept.DeptUpdateReqVO;
import cn.iocoder.educate.module.system.convert.dept.DeptConvert;
import cn.iocoder.educate.module.system.dal.dataobject.dept.DeptDO;
import cn.iocoder.educate.module.system.dal.mysql.dept.DeptMapper;
import cn.iocoder.educate.module.system.dal.mysql.user.AdminUserMapper;
import cn.iocoder.educate.module.system.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.system.enums.dept.DeptIdEnum;
import cn.iocoder.educate.module.system.mq.producer.dept.DeptProducer;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

/**
 * 部门 Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/7/3 17:34
 */
@Service
@Validated
@Slf4j
public class DeptServiceImpl implements DeptService {

    /**
     * 部门缓存
     * key：部门编号 {@link DeptDO#getId()}
     *
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    @Getter
    private volatile Map<Long, DeptDO> deptCache;

    /**
     * 父部门缓存
     * key：部门编号 {@link DeptDO#getParentId()}
     * value: 直接子部门列表
     *
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    @Getter
    private volatile Multimap<Long, DeptDO> parentDeptCache;

    @Resource
    private DeptMapper deptMapper;

    @Resource
    private DeptProducer deptProducer;

    @Override
    @PostConstruct
    public void initLocalCache() {
        // 第一步：查询数据
        List<DeptDO> deptDOS = deptMapper.selectList(new LambdaQueryWrapper<>());
        log.info("[initLocalCache][缓存部门，数量为:{}]", deptDOS.size());

        // 第二步：构建缓存 Map<K,List<V>>
        ImmutableMap.Builder<Long, DeptDO> builder = ImmutableMap.builder();
        ImmutableMultimap.Builder<Long, DeptDO> parentBuilder = ImmutableMultimap.builder();
        deptDOS.forEach(deptDO -> {
            builder.put(deptDO.getId(),deptDO);
            parentBuilder.put(deptDO.getParentId(),deptDO);
        });
        deptCache = builder.build();
        parentDeptCache = parentBuilder.build();
    }

    @Override
    public List<DeptDO> getDeptList(DeptListReqVO deptListReqVO) {
        return deptMapper.selectList(deptListReqVO);
    }

    @Override
    public List<DeptDO> getDeptListByParentIdFromCache(Long parentId, boolean recursive) {
        if(parentId == null){
            return Collections.emptyList();
        }
        ArrayList<DeptDO> result = new ArrayList<>();
        // 递归，简单粗暴
        getDeptsByParentIdFromCache(result, parentId,
                // 如果递归获取，则无限；否则，只递归 1 次
                recursive ? Integer.MAX_VALUE : 1,
                parentDeptCache);
        return result;
    }

    @Override
    public List<DeptDO> getDeptList(Collection<Long> ids) {
        return deptMapper.selectBatchIds(ids);
    }

    @Override
    public DeptDO getDept(Long deptId) {
        return deptCache.get(deptId);
    }

    @Override
    public void validateDeptList(Set<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        ids.forEach(id -> {
            DeptDO deptDO = deptCache.get(id);
            if (deptDO == null) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_NOT_FOUND);
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(deptDO.getStatus())) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_NOT_ENABLE, deptDO.getName());
            }
        });
    }

    @Override
    public Long createDept(DeptCreateReqVO reqVO) {
        // 校验正确性
        validateForCreateOrUpdate(null, reqVO.getParentId(), reqVO.getName());
        // 设置为根节点
        if (reqVO.getParentId() == null) {
            reqVO.setParentId(DeptIdEnum.ROOT.getId());
        }
        // 插入部门
        DeptDO dept = DeptConvert.INSTANCE.convert(reqVO);
        deptMapper.insert(dept);
        // 发送刷新消息
        deptProducer.sendDeptRefreshMessage();
        return dept.getId();
    }

    @Override
    public void updateDept(DeptUpdateReqVO reqVO) {
        // 校验正确性
        if (reqVO.getParentId() == null) {
            reqVO.setParentId(DeptIdEnum.ROOT.getId());
        }
        // 校验正确性
        validateForCreateOrUpdate(reqVO.getId(), reqVO.getParentId(), reqVO.getName());
        // 更新部门
        DeptDO deptDO = DeptConvert.INSTANCE.convert(reqVO);
        deptMapper.updateById(deptDO);
        // 发送刷新消息
        deptProducer.sendDeptRefreshMessage();
    }

    @Override
    public void deleteDept(Long id) {
        // 校验是否存在
        validateDeptExists(id);
        // 校验是否有子部门
        if (deptMapper.selectCountByParentId(id) > 0) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_EXITS_CHILDREN);
        }
        // 删除部门
        deptMapper.deleteById(id);
        // 发送刷新消息
        deptProducer.sendDeptRefreshMessage();
    }

    private void validateForCreateOrUpdate(Long id, Long parentId, String name) {
        // 校验自己存在
        validateDeptExists(id);
        // 校验父部门的有效性
        validateParentDeptEnable(id, parentId);
        // 校验部门名的唯一性
        validateDeptNameUnique(id, parentId, name);
    }

    private void validateDeptNameUnique(Long id, Long parentId, String name) {
        DeptDO menu = deptMapper.selectByParentIdAndName(parentId, name);
        // 数据库中没有重复的dept
        if (menu == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的岗位
        if (id == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_NAME_DUPLICATE);
        }
        if (!menu.getId().equals(id)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_NAME_DUPLICATE);
        }
    }

    private void validateParentDeptEnable(Long id, Long parentId) {
        if (parentId == null || DeptIdEnum.ROOT.getId().equals(parentId)) {
            return;
        }
        // 不能设置自己为父部门
        if (parentId.equals(id)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_PARENT_ERROR);
        }
        // 父岗位不存在
        DeptDO dept = deptMapper.selectById(parentId);
        if (dept == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_PARENT_NOT_EXITS);
        }
        // 父部门被禁用
        if (!CommonStatusEnum.ENABLE.getStatus().equals(dept.getStatus())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_NOT_ENABLE);
        }
        // 父部门不能是原来的子部门
        List<DeptDO> children = getDeptListByParentIdFromCache(id, true);
        if (children.stream().anyMatch(dept1 -> dept1.getId().equals(parentId))) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_PARENT_IS_CHILD);
        }
    }

    private void validateDeptExists(Long id) {
        if (id == null) {
            return;
        }
        DeptDO dept = deptMapper.selectById(id);
        if (dept == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.DEPT_NOT_FOUND);
        }
    }

    /**
     * 递归获取所有的子部门，添加到 result 结果
     *
     * @param result 结果
     * @param parentId 父编号
     * @param recursiveCount 递归次数
     * @param parentDeptMap 父部门 Map，使用缓存，避免变化
     */
    private void getDeptsByParentIdFromCache(ArrayList<DeptDO> result, Long parentId,
                                             int recursiveCount, Multimap<Long, DeptDO> parentDeptMap) {
        // 递归次数为 0，结束！
        if (recursiveCount == 0) {
            return;
        }
        // 获得子部门
        Collection<DeptDO> depts = parentDeptMap.get(parentId);
        if (CollUtil.isEmpty(depts)) {
            return;
        }
        result.addAll(depts);
        // 继续递归
        depts.forEach(dept -> getDeptsByParentIdFromCache(result, dept.getId(),
                recursiveCount - 1, parentDeptMap));
    }

}
