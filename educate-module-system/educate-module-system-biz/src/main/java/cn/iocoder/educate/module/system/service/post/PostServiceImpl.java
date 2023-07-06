package cn.iocoder.educate.module.system.service.post;

import cn.iocoder.educate.module.system.dal.mysql.post.PostMapper;
import cn.iocoder.educate.module.system.dal.post.PostDO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

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
}
