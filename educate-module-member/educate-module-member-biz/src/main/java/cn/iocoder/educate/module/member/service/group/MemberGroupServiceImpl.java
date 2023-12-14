package cn.iocoder.educate.module.member.service.group;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.member.controller.admin.group.vo.MemberGroupCreateReqVO;
import cn.iocoder.educate.module.member.controller.admin.group.vo.MemberGroupPageReqVO;
import cn.iocoder.educate.module.member.controller.admin.group.vo.MemberGroupUpdateReqVO;
import cn.iocoder.educate.module.member.convert.group.MemberGroupConvert;
import cn.iocoder.educate.module.member.dal.dataobject.group.MemberGroupDO;
import cn.iocoder.educate.module.member.dal.mysql.group.MemberGroupMapper;
import cn.iocoder.educate.module.member.service.user.MemberUserService;
import cn.iocoder.educate.module.member.enums.ErrorCodeConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 用户分组 Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/11/21 23:44
 */
@Slf4j
@Service
@Validated
public class MemberGroupServiceImpl implements MemberGroupService {

    @Resource
    private MemberGroupMapper memberGroupMapper;

    @Resource
    private MemberUserService memberUserService;

    @Override
    public Long createGroup(MemberGroupCreateReqVO createReqVO) {
        // 校验名称唯一
        validateGroupNameUnique(null, createReqVO.getName());
        // 插入
        MemberGroupDO group = MemberGroupConvert.INSTANCE.convert(createReqVO);
        memberGroupMapper.insert(group);
        // 返回
        return group.getId();
    }

    @Override
    public void updateGroup(MemberGroupUpdateReqVO updateReqVO) {
        // 校验存在
        validateGroupExists(updateReqVO.getId());
        // 更新
        MemberGroupDO memberGroupDO = MemberGroupConvert.INSTANCE.convert(updateReqVO);
        memberGroupMapper.updateById(memberGroupDO);
    }

    @Override
    public void deleteGroup(Long id) {
        // 校验存在
        validateGroupExists(id);
        // 校验分组下是否有用户
        validateGroupHasUser(id);
        // 删除
        memberGroupMapper.deleteById(id);
    }

    @Override
    public MemberGroupDO getGroup(Long id) {
        return memberGroupMapper.selectById(id);
    }

    @Override
    public List<MemberGroupDO> getGroupList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return memberGroupMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<MemberGroupDO> getGroupPage(MemberGroupPageReqVO pageReqVO) {
        return memberGroupMapper.selectPage(pageReqVO);
    }

    @Override
    public List<MemberGroupDO> getGroupListByStatus(Integer status) {
        return memberGroupMapper.selectListByStatus(status);
    }

    private void validateGroupHasUser(Long id) {
        Long count = memberUserService.getUserCountByGroupId(id);
        if (count > 0) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.GROUP_HAS_USER);
        }
    }

    private void validateGroupExists(Long id) {
        if (memberGroupMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.GROUP_NOT_EXISTS);
        }
    }

    private void validateGroupNameUnique(Long id, String name) {
        if (StrUtil.isBlank(name)) {
            return;
        }
        MemberGroupDO groupDO = memberGroupMapper.selelctByName(name);
        if (groupDO == null) {
            return;
        }

        // 如果 id 为空，说明不用比较是否为相同 id 的标签
        if (id == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.GROUP_NOT_EXISTS);
        }
        if (!groupDO.getId().equals(id)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.GROUP_NOT_EXISTS);
        }
    }


}
