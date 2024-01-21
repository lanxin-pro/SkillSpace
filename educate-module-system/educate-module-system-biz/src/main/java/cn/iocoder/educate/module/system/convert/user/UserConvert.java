package cn.iocoder.educate.module.system.convert.user;

import cn.hutool.core.bean.BeanUtil;
import cn.iocoder.educate.module.system.api.user.dto.AdminUserRespDTO;
import cn.iocoder.educate.module.system.controller.admin.user.vo.user.*;
import cn.iocoder.educate.module.system.controller.admin.user.vo.profile.UserProfileRespVO;
import cn.iocoder.educate.module.system.dal.dataobject.dept.DeptDO;
import cn.iocoder.educate.module.system.dal.dataobject.permission.RoleDO;
import cn.iocoder.educate.module.system.dal.dataobject.social.SocialUserDO;
import cn.iocoder.educate.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.educate.module.system.dal.post.PostDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: j-sentinel
 * @Date: 2023/7/6 12:01
 */
@Mapper
public interface UserConvert {

    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    UserPageItemRespVO convert(AdminUserDO bean);

    AdminUserDO convert(UserCreateReqVO bean);

    UserPageItemRespVO.Dept convert(DeptDO bean);

    AdminUserDO convert(UserUpdateReqVO bean);

    UserProfileRespVO convert03(AdminUserDO bean);

    List<UserProfileRespVO.Role> convertList(List<RoleDO> list);

    default List<UserExcelRespVO> convertList(List<AdminUserDO> list, Map<Long, DeptDO> deptMap) {
        return list.stream().map(user -> {
            UserExcelRespVO userExcelRespVO = BeanUtil.toBean(user, UserExcelRespVO.class);
            DeptDO deptDO = deptMap.get(user.getDeptId());
            if(deptDO != null) {
                userExcelRespVO.setDeptName(deptDO.getName());
            }
            return userExcelRespVO;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    UserProfileRespVO.Dept convert02(DeptDO bean);

    List<UserProfileRespVO.Post> convertList02(List<PostDO> list);

    List<UserProfileRespVO.SocialUser> convertList03(List<SocialUserDO> list);

    List<UserSimpleRespVO> convertList04(List<AdminUserDO> list);

    List<UserSimpleRespVO> convertList05(List<UserSimpleRespVO> list);

    AdminUserRespDTO convert4(AdminUserDO bean);

}
