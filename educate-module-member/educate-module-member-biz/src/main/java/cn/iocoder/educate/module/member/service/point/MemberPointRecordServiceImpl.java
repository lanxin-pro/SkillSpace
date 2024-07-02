package cn.iocoder.educate.module.member.service.point;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.educate.framework.common.pojo.PageParam;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.object.PageUtils;
import cn.iocoder.educate.module.member.controller.admin.point.vo.recrod.MemberPointRecordPageReqVO;
import cn.iocoder.educate.module.member.dal.dataobject.point.MemberPointRecordDO;
import cn.iocoder.educate.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.educate.module.member.dal.mysql.point.MemberPointRecordMapper;
import cn.iocoder.educate.module.member.dal.mysql.user.MemberUserMapper;
import cn.iocoder.educate.module.member.service.user.MemberUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author j-sentinel
 * @date 2024/1/19 19:50
 */
@Slf4j
@Service
public class MemberPointRecordServiceImpl implements MemberPointRecordService {

    @Resource
    private MemberPointRecordMapper memberPointRecordMapper;

    @Resource
    private MemberUserService memberUserService;

    @Override
    public PageResult<MemberPointRecordDO> getPointRecordPage(MemberPointRecordPageReqVO memberPointRecordPageReqVO) {
        // 根据用户昵称查询出来的用户ids
        Set<Long> userIds = null;
        if(StrUtil.isNotBlank(memberPointRecordPageReqVO.getNickname())) {
            List<MemberUserDO> users = memberUserService.getUserListByNickname(memberPointRecordPageReqVO.getNickname());
            // 如果查询出用户的结果为空，则直接返回无需继续查询
            if(CollectionUtils.isEmpty(users)) {
                return PageResult.empty();
            }
            userIds = users.stream().map(MemberUserDO::getId).collect(Collectors.toSet());
        }
        return memberPointRecordMapper.selectPage(memberPointRecordPageReqVO, userIds);
    }

}
