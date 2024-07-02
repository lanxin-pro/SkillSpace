package cn.iocoder.educate.module.member.convert.user;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.member.controller.admin.user.vo.MemberUserRespVO;
import cn.iocoder.educate.module.member.controller.app.user.vo.AppMemberUserInfoRespVO;
import cn.iocoder.educate.module.member.dal.dataobject.group.MemberGroupDO;
import cn.iocoder.educate.module.member.dal.dataobject.level.MemberLevelDO;
import cn.iocoder.educate.module.member.dal.dataobject.tag.MemberTagDO;
import cn.iocoder.educate.module.member.dal.dataobject.user.MemberUserDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    // TODO j-sentinel 这里需要转换地区ID为地区名称
    // @Mapping(source = "areaId", target = "areaName", qualifiedByName = "convertAreaIdToAreaName")
    MemberUserRespVO convert(MemberUserDO memberUserDO);

    PageResult<MemberUserRespVO> convertPage(PageResult<MemberUserDO> page);

    default PageResult<MemberUserRespVO> convertPage(PageResult<MemberUserDO> pageResult, List<MemberTagDO> tags, List<MemberLevelDO> levels, List<MemberGroupDO> groups) {
        PageResult<MemberUserRespVO> result = convertPage(pageResult);
        // 处理关联数据
        Map<Long, String> tagMap = tags.stream()
                .collect(Collectors.toMap(MemberTagDO::getId, MemberTagDO::getName,
                        (v1, v2) -> v1,
                        HashMap::new));
        Map<Long, String> levelMap = levels.stream()
                .collect(Collectors.toMap(MemberLevelDO::getId, MemberLevelDO::getName,
                        (v1, v2) -> v1,
                        HashMap::new));
        Map<Long, String> groupMap = groups.stream()
                .collect(Collectors.toMap(MemberGroupDO::getId, MemberGroupDO::getName,
                        (v1, v2) -> v1,
                        HashMap::new));
        // 填充关联数据
        result.getList().forEach( user -> {
            user.setTagNames(user.getTagIds().stream().map(tagMap::get).collect(Collectors.toList()));
            user.setLevelName(levelMap.get(user.getLevelId()));
            user.setGroupName(groupMap.get(user.getGroupId()));
        });
        return result;
    }
}
