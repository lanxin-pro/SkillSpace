package cn.iocoder.educate.module.system.service.dept;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.iocoder.educate.module.system.controller.admin.dept.vo.dept.DeptListReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.dept.DeptDO;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 部门 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/7/3 17:34
 */
public interface DeptService {

    /**
     * 初始化部门的本地缓存
     */
    void initLocalCache();

    /**
     * 筛选部门列表
     *
     * @param deptListReqVO 筛选条件请求 VO
     * @return 部门列表
     */
    List<DeptDO> getDeptList(DeptListReqVO deptListReqVO);

    /**
     * 获得所有子部门，从缓存中
     *
     * @param deptId 部门编号
     * @param recursive 是否递归获取所有
     * @return 子部门列表
     */
    List<DeptDO>  getDeptListByParentIdFromCache(Long deptId, boolean recursive);

    /**
     * 获得部门信息数组
     *
     * @param ids 部门编号数组
     * @return 部门信息数组
     */
    List<DeptDO> getDeptList(Collection<Long> ids);

    /**
     * 获得指定编号的部门 Map
     *
     * @param deptIds 部门编号数组
     * @return 部门 Map
     */
    default Map<Long, DeptDO> getDeptMap(Collection<Long> deptIds){
        if(CollUtil.isEmpty(deptIds)){
            return Collections.emptyMap();
        }
        List<DeptDO> deptList = getDeptList(deptIds);
        return deptList.stream().collect(Collectors.toMap(DeptDO::getId, Function.identity(),(v1,v2) -> v1));
    }

    /**
     * 获得部门信息
     *
     * @param deptId 部门编号
     * @return 部门信息
     */
    DeptDO getDept(Long deptId);
}
