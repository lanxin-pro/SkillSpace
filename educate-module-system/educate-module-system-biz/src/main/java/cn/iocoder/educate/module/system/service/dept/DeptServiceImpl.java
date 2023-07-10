package cn.iocoder.educate.module.system.service.dept;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.educate.module.system.controller.admin.dept.vo.dept.DeptListReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.dept.DeptDO;
import cn.iocoder.educate.module.system.dal.mysql.dept.DeptMapper;
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
        return deptMapper.selectById(deptId);
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
