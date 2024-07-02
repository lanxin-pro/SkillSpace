package cn.iocoder.educate.module.member.convert.tag;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.member.controller.admin.tag.vo.MemberTagCreateReqVO;
import cn.iocoder.educate.module.member.controller.admin.tag.vo.MemberTagRespVO;
import cn.iocoder.educate.module.member.controller.admin.tag.vo.MemberTagUpdateReqVO;
import cn.iocoder.educate.module.member.dal.dataobject.tag.MemberTagDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Author: j-sentinel
 * @Date: 2023/11/21 18:00
 */
@Mapper
public interface MemberTagConvert {

    MemberTagConvert  INSTANCE = Mappers.getMapper(MemberTagConvert.class);

    MemberTagDO convert(MemberTagCreateReqVO bean);

    MemberTagDO convert(MemberTagUpdateReqVO bean);

    MemberTagRespVO convert(MemberTagDO bean);

    List<MemberTagRespVO> convertList(List<MemberTagDO> list);

    PageResult<MemberTagRespVO> convertPage(PageResult<MemberTagDO> page);

}
