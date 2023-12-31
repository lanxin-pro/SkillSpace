package cn.iocoder.educate.module.member.convert.user;

import cn.iocoder.educate.module.member.controller.app.user.vo.AppMemberUserInfoRespVO;
import cn.iocoder.educate.module.member.dal.dataobject.level.MemberLevelDO;
import cn.iocoder.educate.module.member.dal.dataobject.user.MemberUserDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @Author: j-sentinel
 * @Date: 2023/12/31 12:36
 */
@Mapper
public interface MemberUserConvert {

    MemberUserConvert INSTANCE = Mappers.getMapper(MemberUserConvert.class);

    /**
     * 因为目前两个实体都会存在experience，我应该使用的是MemberUserDO的属性
     * @param bean
     * @param level
     * @return
     */
    @Mapping(source = "level", target = "level")
    @Mapping(source = "bean.experience", target = "experience")
    AppMemberUserInfoRespVO convert(MemberUserDO bean, MemberLevelDO level);

}
