package cn.iocoder.educate.module.member.convert.level;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.member.controller.admin.level.vo.level.MemberLevelCreateReqVO;
import cn.iocoder.educate.module.member.controller.admin.level.vo.level.MemberLevelRespVO;
import cn.iocoder.educate.module.member.controller.admin.level.vo.level.MemberLevelSimpleRespVO;
import cn.iocoder.educate.module.member.controller.admin.level.vo.level.MemberLevelUpdateReqVO;
import cn.iocoder.educate.module.member.dal.dataobject.level.MemberLevelDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 会员等级 Convert
 *
 * @Author: j-sentinel
 * @Date: 2023/11/22 21:36
 */
@Mapper
public interface MemberLevelConvert {

    MemberLevelConvert INSTANCE = Mappers.getMapper(MemberLevelConvert.class);

    MemberLevelDO convert(MemberLevelCreateReqVO bean);

    MemberLevelDO convert(MemberLevelUpdateReqVO bean);

    MemberLevelRespVO convert(MemberLevelDO bean);

    PageResult<MemberLevelRespVO> convertPage(PageResult<MemberLevelDO> list);

    List<MemberLevelSimpleRespVO> convertSimpleList(List<MemberLevelDO> list);

}
