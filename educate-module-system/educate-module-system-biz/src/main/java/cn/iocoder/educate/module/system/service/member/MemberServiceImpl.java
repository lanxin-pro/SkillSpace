package cn.iocoder.educate.module.system.service.member;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/6 14:15
 */
@Slf4j
@Service
public class MemberServiceImpl implements MemberService{

    @Value("${lanxin.info.base-package}")
    private String basePackage;

    private volatile Object memberUserApi;

    @Override
    public String getMemberUserMobile(Long userId) {
        Object user = getMemberUser(userId);
        if (user == null) {
            return null;
        }
        return ReflectUtil.invoke(user, "getMobile");
    }

    private Object getMemberUser(Long id) {
        if (id == null) {
            return null;
        }
        return ReflectUtil.invoke(getMemberUserApi(), "getUser", id);
    }

    private Object getMemberUserApi() {
        if (memberUserApi == null) {
            memberUserApi = SpringUtil.getBean(ClassUtil.loadClass(String.format("%s.module.member.api.user.MemberUserApi", basePackage)));
        }
        return memberUserApi;
    }
}
