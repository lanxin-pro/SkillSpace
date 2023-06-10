package cn.iocoder.educate.module.system.service.oauth2;

import cn.iocoder.educate.module.system.dal.dataobject.oauth2.OAuth2ApproveDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/6/9 10:09
 */
@Slf4j
@Service
public class OAuth2ApproveServiceImpl implements OAuth2ApproveService{
    @Override
    public List<OAuth2ApproveDO> getApproveList(Long userId, Integer userType, String clientId) {
        return null;
    }
}
