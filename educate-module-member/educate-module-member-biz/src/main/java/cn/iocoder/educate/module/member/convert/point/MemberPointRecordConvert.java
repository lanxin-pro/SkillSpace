package cn.iocoder.educate.module.member.convert.point;

import cn.hutool.core.map.MapUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.common.util.collection.MapUtils;
import cn.iocoder.educate.module.member.controller.admin.point.vo.recrod.MemberPointRecordRespVO;
import cn.iocoder.educate.module.member.dal.dataobject.point.MemberPointRecordDO;
import cn.iocoder.educate.module.member.dal.dataobject.user.MemberUserDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户积分记录 Convert
 *
 * @author j-sentinel
 * @date 2024/1/19 21:12
 */
@Mapper
public interface MemberPointRecordConvert {

    MemberPointRecordConvert INSTANCE = Mappers.getMapper(MemberPointRecordConvert.class);

    default PageResult<MemberPointRecordRespVO> convertPage(PageResult<MemberPointRecordDO> pageResult,
                                                            List<MemberUserDO> users) {
        PageResult<MemberPointRecordRespVO> memberPointRecordRespVOPageResult = convertPage(pageResult);
        // user 拼接
        Map<Long, MemberUserDO> userDOMap = users.stream()
                .collect(Collectors.toMap(MemberUserDO::getId, Function.identity(), (v1, v2) -> v1));
        memberPointRecordRespVOPageResult.getList().forEach(record -> {
            MapUtils.findAndThen(userDOMap, record.getUserId(), memberUserRespDTO -> {
                record.setNickname(memberUserRespDTO.getNickname());
            });
        });
        return memberPointRecordRespVOPageResult;
    }

    PageResult<MemberPointRecordRespVO> convertPage(PageResult<MemberPointRecordDO> pageResult);

}
