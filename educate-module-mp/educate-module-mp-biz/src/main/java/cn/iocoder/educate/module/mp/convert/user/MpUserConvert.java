package cn.iocoder.educate.module.mp.convert.user;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.iocoder.educate.framework.common.enums.CommonStatusEnum;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.mp.controller.admin.user.vo.MpUserRespVO;
import cn.iocoder.educate.module.mp.controller.admin.user.vo.MpUserUpdateReqVO;
import cn.iocoder.educate.module.mp.dal.dataobject.account.MpAccountDO;
import cn.iocoder.educate.module.mp.dal.dataobject.user.MpUserDO;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/3 19:50
 */
@Mapper
public interface MpUserConvert {

    MpUserConvert INSTANCE = Mappers.getMapper(MpUserConvert.class);

    MpUserRespVO convert(MpUserDO bean);

    List<MpUserRespVO> convertList(List<MpUserDO> list);

    PageResult<MpUserRespVO> convertPage(PageResult<MpUserDO> page);

    MpUserDO convert(MpUserUpdateReqVO bean);

    @Mappings(value = {
            @Mapping(source = "openId", target = "openid"),
            @Mapping(source = "headImgUrl", target = "headImageUrl"),
            // 单独转换
            @Mapping(target = "subscribeTime", ignore = true),
    })
    MpUserDO convert(WxMpUser wxMpUser);

    default List<MpUserDO> convertList(MpAccountDO account, List<WxMpUser> wxMpUsers) {
        return wxMpUsers.stream().map(wxUser -> {
            MpUserDO user = this.convert(wxUser);
            user.setSubscribeStatus(wxUser.getSubscribe() ? CommonStatusEnum.ENABLE.getStatus()
                    : CommonStatusEnum.DISABLE.getStatus());
            user.setSubscribeTime(LocalDateTimeUtil.of(wxUser.getSubscribeTime() * 1000L));
            if (account != null) {
                user.setAccountId(account.getId());
                user.setAppId(account.getAppId());
            }
            return user;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

}
