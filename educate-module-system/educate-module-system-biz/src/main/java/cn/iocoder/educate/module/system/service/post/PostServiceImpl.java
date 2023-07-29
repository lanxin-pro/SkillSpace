package cn.iocoder.educate.module.system.service.post;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.module.system.dal.mysql.post.PostMapper;
import cn.iocoder.educate.module.system.dal.post.PostDO;
import cn.iocoder.educate.module.system.enums.ErrorCodeConstants;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 岗位 Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/7/3 18:29
 */
@Service
@Validated
public class PostServiceImpl implements PostService{

    @Resource
    private PostMapper postMapper;

    @Override
    public List<PostDO> getPostList(Collection<Long> ids, Collection<Integer> status) {
        return postMapper.selectList(ids,status);
    }

    @Override
    public void validatePostList(Set<Long> postIds) {
        if (CollUtil.isEmpty(postIds)) {
            return;
        }
        // 获得岗位信息
        List<PostDO> posts = postMapper.selectBatchIds(postIds);
        Map<Long, PostDO> postMap = posts.stream()
                .collect(Collectors.toMap(PostDO::getId, Function.identity(), (v1, v2) -> v1));
        postIds.forEach(id -> {
            PostDO postDO = postMap.get(id);
            if(postDO == null){
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.POST_NOT_FOUND);
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(postDO.getStatus())) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.POST_NOT_ENABLE, postDO.getName());
            }
        });
    }

}
