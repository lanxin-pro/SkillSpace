package cn.iocoder.educate.module.system.convert.user;

import cn.iocoder.educate.module.system.api.user.dto.AdminUserRespDTO;
import cn.iocoder.educate.module.system.controller.admin.user.vo.user.UserCreateReqVO;
import cn.iocoder.educate.module.system.controller.admin.user.vo.user.UserPageItemRespVO;
import cn.iocoder.educate.module.system.controller.admin.user.vo.profile.UserProfileRespVO;
import cn.iocoder.educate.module.system.controller.admin.user.vo.user.UserSimpleRespVO;
import cn.iocoder.educate.module.system.controller.admin.user.vo.user.UserUpdateReqVO;
import cn.iocoder.educate.module.system.dal.dataobject.dept.DeptDO;
import cn.iocoder.educate.module.system.dal.dataobject.permission.RoleDO;
import cn.iocoder.educate.module.system.dal.dataobject.social.SocialUserDO;
import cn.iocoder.educate.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.educate.module.system.dal.post.PostDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

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

    UserProfileRespVO.Dept convert02(DeptDO bean);

    List<UserProfileRespVO.Post> convertList02(List<PostDO> list);

    List<UserProfileRespVO.SocialUser> convertList03(List<SocialUserDO> list);

    List<UserSimpleRespVO> convertList04(List<AdminUserDO> list);

    List<UserSimpleRespVO> convertList05(List<UserSimpleRespVO> list);

    AdminUserRespDTO convert4(AdminUserDO bean);

}
