package cn.iocoder.educate.module.member.convert.config;

import cn.iocoder.educate.module.member.controller.admin.config.vo.MemberConfigRespVO;
import cn.iocoder.educate.module.member.controller.admin.config.vo.MemberConfigSaveReqVO;
import cn.iocoder.educate.module.member.dal.dataobject.config.MemberConfigDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 会员配置 Convert
 *
 * @Author: j-sentinel
 * @Date: 2023/11/21 14:35
 */
@Mapper
public interface MemberConfigConvert {

    MemberConfigConvert INSTANCE = Mappers.getMapper(MemberConfigConvert.class);

    MemberConfigRespVO convert(MemberConfigDO bean);

    MemberConfigDO convert(MemberConfigSaveReqVO bean);

}
