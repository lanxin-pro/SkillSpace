package cn.iocoder.educate.module.mp.service.message;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.mp.controller.admin.message.vo.autoreply.MpMessagePageReqVO;
import cn.iocoder.educate.module.mp.dal.dataobject.message.MpAutoReplyDO;
import cn.iocoder.educate.module.mp.dal.mysql.message.MpAutoReplyMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/1 20:23
 */
@Slf4j
@Service
@Validated
public class MpAutoReplyServiceImpl implements MpAutoReplyService {

    @Resource
    private MpAutoReplyMapper mpAutoReplyMapper;

    @Override
    public PageResult<MpAutoReplyDO> getAutoReplyPage(MpMessagePageReqVO mpMessagePageReqVO) {
        return mpAutoReplyMapper.selectPage(mpMessagePageReqVO);
    }
}
