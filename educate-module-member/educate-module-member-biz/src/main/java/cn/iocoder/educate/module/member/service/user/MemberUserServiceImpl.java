package cn.iocoder.educate.module.member.service.user;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import cn.iocoder.educate.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.educate.module.member.dal.mysql.user.MemberUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 会员 User Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/11/21 22:29
 */
@Service
@Valid
@Slf4j
public class MemberUserServiceImpl implements MemberUserService {

    @Resource
    private MemberUserMapper memberUserMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public Long getUserCountByTagId(Long tagId) {
        return 0L;
    }

    @Override
    public Long getUserCountByGroupId(Long groupId) {
        return 0L;
    }

    @Override
    public Long getUserCountByLevelId(Long levelId) {
        return 0L;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MemberUserDO createUserIfAbsent(String mobile, String registerIp, Integer terminal) {
        // 用户已经存在
        MemberUserDO user = memberUserMapper.selectByMobile(mobile);
        if (user != null) {
            return user;
        }
        // 用户不存在，则进行创建
        return createUser(mobile, registerIp, terminal);
    }

    @Override
    public void updateUserLogin(Long userId, String loginIp) {
        memberUserMapper.updateById(new MemberUserDO().setId(userId)
                .setLoginIp(loginIp).setLoginDate(LocalDateTime.now()));
    }

    @Override
    public MemberUserDO getUserByMobile(String mobile) {
        return memberUserMapper.selectByMobile(mobile);
    }

    @Override
    public MemberUserDO getUser(Long loginUserId) {
        return memberUserMapper.selectById(loginUserId);
    }

    @Override
    public List<MemberUserDO> getUserListByNickname(String nickname) {
        return memberUserMapper.selectListByNickname(nickname);
    }

    @Override
    public List<MemberUserDO> getUserList(Set<Long> collectUserIds) {
        if(CollUtil.isEmpty(collectUserIds)) {
            return Collections.emptyList();
        }
        return memberUserMapper.selectBatchIds(collectUserIds);
    }

    private MemberUserDO createUser(String mobile, String registerIp, Integer terminal) {
        // 生成密码
        String password = IdUtil.fastSimpleUUID();
        // 插入用户
        MemberUserDO user = new MemberUserDO();
        // TODO j-sentinel 这里可以考虑给用户默认的用户名和头像，看后续业务是要求怎么给的
        user.setNickname("用户" + password);
        user.setAvatar("http://alanxin.cn:55555/api/v1/buckets/educate-mall/objects/download?preview=true&prefix=ZmI4ZGZmODJiMjc1YmQyOWQ2MWNmZTVmNTJhYTQzODYucG5n&version_id=null");
        user.setMobile(mobile);
        // 默认开启
        user.setStatus(CommonStatusEnum.ENABLE.getStatus());
        // 加密密码
        user.setPassword(encodePassword(password));
        user.setRegisterIp(registerIp);
        user.setRegisterTerminal(terminal);
        memberUserMapper.insert(user);
        // TODO j-sentinel 我应该把用户缓存？？？


        return user;
    }

    /**
     * 对密码进行加密
     *
     * @param password 密码
     * @return 加密后的密码
     */
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

}
