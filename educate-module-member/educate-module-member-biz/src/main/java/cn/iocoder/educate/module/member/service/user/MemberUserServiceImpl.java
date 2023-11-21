package cn.iocoder.educate.module.member.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

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

    @Override
    public Long getUserCountByTagId(Long tagId) {
        return 0L;
    }

}
