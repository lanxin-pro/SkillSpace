package cn.iocoder.educate.module.mp.service.user;

import cn.iocoder.educate.module.mp.dal.mysql.user.MpUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * 微信公众号粉丝 Service 实现类
 *
 * @Author: j-sentinel
 * @Date: 2023/9/3 19:29
 */
@Service
@Validated
@Slf4j
public class MpUserServiceImpl implements MpUserService {

    @Resource
    private MpUserMapper mpUserMapper;

}
