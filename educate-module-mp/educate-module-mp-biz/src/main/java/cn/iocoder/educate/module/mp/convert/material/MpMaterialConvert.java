package cn.iocoder.educate.module.mp.convert.material;

import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.mp.controller.admin.material.vo.MpMaterialRespVO;
import cn.iocoder.educate.module.mp.controller.admin.material.vo.MpMaterialUploadRespVO;
import cn.iocoder.educate.module.mp.dal.dataobject.account.MpAccountDO;
import cn.iocoder.educate.module.mp.dal.dataobject.material.MpMaterialDO;
import me.chanjar.weixin.mp.bean.material.WxMpMaterial;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.io.File;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/11 15:44
 */
@Mapper
public interface MpMaterialConvert {

    MpMaterialConvert INSTANCE = Mappers.getMapper(MpMaterialConvert.class);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(source = "account.id", target = "accountId"),
            @Mapping(source = "account.appId", target = "appId"),
            @Mapping(source = "name", target = "name")
    })
    MpMaterialDO convert(String mediaId, String type, String url, MpAccountDO account,
                         String name, String title, String introduction, String mpUrl);

    default WxMpMaterial convert(String name, File file, String title, String introduction) {
        return new WxMpMaterial(name, file, title, introduction);
    }

    MpMaterialUploadRespVO convert(MpMaterialDO bean);

    PageResult<MpMaterialRespVO> convertPage(PageResult<MpMaterialDO> page);

}
