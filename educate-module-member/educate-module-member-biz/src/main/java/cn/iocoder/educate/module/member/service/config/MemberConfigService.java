package cn.iocoder.educate.module.member.service.config;

import cn.iocoder.educate.module.member.controller.admin.config.vo.MemberConfigSaveReqVO;
import cn.iocoder.educate.module.member.dal.dataobject.config.MemberConfigDO;

/**
 * 会员配置 Service 接口
 *
 * @Author: j-sentinel
 * @Date: 2023/11/21 14:32
 */
public interface MemberConfigService {

    /**
     * 保存会员配置
     *
     * @param saveReqVO 更新信息
     */
    void saveConfig(MemberConfigSaveReqVO saveReqVO);

    /**
     * 获得会员配置
     *
     * @return 积分配置
     */
    MemberConfigDO getConfig();

}
