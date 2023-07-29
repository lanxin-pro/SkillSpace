package cn.iocoder.educate.module.system.service.post;

import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import cn.iocoder.educate.framework.common.util.collection.SetUtils;
import cn.iocoder.educate.module.system.dal.post.PostDO;
import org.springframework.lang.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 岗位 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/7/3 18:29
 */
public interface PostService {

    // TODO 产品经理-蓝欣 这里后期设置一个init

    default List<PostDO> getPostList(@Nullable Collection<Long> ids) {
        return getPostList(ids,
                SetUtils.asSet(CommonStatusEnum.ENABLE.getStatus(), CommonStatusEnum.DISABLE.getStatus()));
    }

    /**
     * 获得符合条件的岗位列表
     *
     * @param ids 岗位编号数组。如果为空，不进行筛选
     * @param status 状态数组。如果为空，不进行筛选
     * @return 部门列表
     */
    List<PostDO> getPostList(@Nullable Collection<Long> ids,@Nullable Collection<Integer> status);

    /**
     * 校验岗位们是否有效。如下情况，视为无效：
     * 1. 岗位编号不存在
     * 2. 岗位被禁用
     *
     * @param postIds 岗位编号数组
     */
    void validatePostList(Set<Long> postIds);

}
