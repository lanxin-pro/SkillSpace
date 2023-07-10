package cn.iocoder.educate.module.system.service.post;

import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import cn.iocoder.educate.framework.common.util.collection.SetUtils;
import cn.iocoder.educate.module.system.dal.post.PostDO;
import org.springframework.lang.Nullable;
import java.util.Collection;
import java.util.List;

/**
 * 岗位 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/7/3 18:29
 */
public interface PostService {

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
}
