package cn.iocoder.educate.module.system.service.post;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.dept.vo.post.PostCreateReqVO;
import cn.iocoder.educate.module.system.controller.admin.dept.vo.post.PostPageReqVO;
import cn.iocoder.educate.module.system.controller.admin.dept.vo.post.PostUpdateReqVO;
import cn.iocoder.educate.module.system.convert.post.PostConvert;
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

    @Override
    public PageResult<PostDO> getPostPage(PostPageReqVO reqVO) {
        return postMapper.selectPage(reqVO);
    }

    @Override
    public Long createPost(PostCreateReqVO reqVO) {
        // 校验正确性
        validatePostForCreateOrUpdate(null, reqVO.getName(), reqVO.getCode());

        // 插入岗位
        PostDO post = PostConvert.INSTANCE.convert(reqVO);
        postMapper.insert(post);
        return post.getId();
    }

    @Override
    public void updatePost(PostUpdateReqVO reqVO) {
        // 校验正确性
        validatePostForCreateOrUpdate(reqVO.getId(), reqVO.getName(), reqVO.getCode());

        // 更新岗位
        PostDO postDO = PostConvert.INSTANCE.convert(reqVO);
        postMapper.updateById(postDO);
    }

    @Override
    public void deletePost(Long id) {
        // 校验是否存在
        validatePostExists(id);
        // 删除部门
        postMapper.deleteById(id);
    }

    @Override
    public PostDO getPost(Long id) {
        return postMapper.selectById(id);
    }

    private void validatePostForCreateOrUpdate(Long id, String name, String code) {
        // 校验自己存在
        validatePostExists(id);
        // 校验岗位名的唯一性
        validatePostNameUnique(id, name);
        // 校验岗位编码的唯一性
        validatePostCodeUnique(id, code);
    }

    /**
     * 校验自己存在
     * @param id
     */
    private void validatePostExists(Long id) {
        if (id == null) {
            return;
        }
        if (postMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.POST_NOT_FOUND);
        }
    }

    /**
     * 校验岗位名的唯一性
     * @param id
     * @param name
     */
    private void validatePostNameUnique(Long id, String name) {
        PostDO post = postMapper.selectByName(name);
        if (post == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的岗位
        if (id == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.POST_NAME_DUPLICATE);
        }
        if (!post.getId().equals(id)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.POST_NAME_DUPLICATE);
        }
    }

    /**
     * 校验岗位编码的唯一性
     * @param id
     * @param code
     */
    private void validatePostCodeUnique(Long id, String code) {
        PostDO post = postMapper.selectByCode(code);
        if (post == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的岗位
        if (id == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.POST_CODE_DUPLICATE);
        }
        if (!post.getId().equals(id)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.POST_CODE_DUPLICATE);
        }
    }

}
