package cn.iocoder.educate.module.member.service.tag;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.member.controller.admin.tag.vo.MemberTagCreateReqVO;
import cn.iocoder.educate.module.member.controller.admin.tag.vo.MemberTagPageReqVO;
import cn.iocoder.educate.module.member.controller.admin.tag.vo.MemberTagUpdateReqVO;
import cn.iocoder.educate.module.member.convert.tag.MemberTagConvert;
import cn.iocoder.educate.module.member.dal.dataobject.tag.MemberTagDO;
import cn.iocoder.educate.module.member.dal.mysql.tag.MemberTagMapper;
import cn.iocoder.educate.module.member.service.user.MemberUserService;
import cn.iocoder.educate.module.member.enums.ErrorCodeConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 会员标签 Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/11/21 17:58
 */
@Slf4j
@Service
@Validated
public class MemberTagServiceImpl implements MemberTagService {

    @Resource
    private MemberTagMapper memberTagMapper;

    @Resource
    private MemberUserService memberUserService;

    @Override
    public Long createTag(MemberTagCreateReqVO createReqVO) {
        // 校验名称唯一
        validateTagNameUnique(null, createReqVO.getName());
        // 插入
        MemberTagDO tag = MemberTagConvert.INSTANCE.convert(createReqVO);
        memberTagMapper.insert(tag);
        // 返回
        return tag.getId();
    }

    @Override
    public void updateTag(MemberTagUpdateReqVO updateReqVO) {
        // 校验存在
        validateTagExists(updateReqVO.getId());
        // 校验名称唯一
        validateTagNameUnique(updateReqVO.getId(), updateReqVO.getName());
        // 更新
        MemberTagDO updateObj = MemberTagConvert.INSTANCE.convert(updateReqVO);
        memberTagMapper.updateById(updateObj);
    }

    @Override
    public void deleteTag(Long id) {
        // 校验存在
        validateTagExists(id);
        // 校验标签下是否有用户
        validateTagHasUser(id);
        // 删除
        memberTagMapper.deleteById(id);
    }

    @Override
    public MemberTagDO getTag(Long id) {
        return memberTagMapper.selectById(id);
    }

    @Override
    public List<MemberTagDO> getTagList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return memberTagMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<MemberTagDO> getTagPage(MemberTagPageReqVO pageReqVO) {
        return memberTagMapper.selectPage(pageReqVO);
    }

    @Override
    public List<MemberTagDO> getTagList() {
        return memberTagMapper.selectList(new LambdaQueryWrapper<>());
    }

    void validateTagHasUser(Long id) {
        Long count = memberUserService.getUserCountByTagId(id);
        if (count > 0) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.TAG_HAS_USER);
        }
    }

    private void validateTagExists(Long id) {
        if (memberTagMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.TAG_NOT_EXISTS);
        }
    }

    private void validateTagNameUnique(Long id, String name) {
        if (StrUtil.isBlank(name)) {
            return;
        }
        MemberTagDO tag = memberTagMapper.selelctByName(name);
        if (tag == null) {
            return;
        }

        // 如果 id 为空，说明不用比较是否为相同 id 的标签
        if (id == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.TAG_NAME_EXISTS);
        }
        if (!tag.getId().equals(id)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.TAG_NAME_EXISTS);
        }
    }


}
